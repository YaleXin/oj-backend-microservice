package top.yalexin.ojbackendcodesandbox.sandbox;

import cn.hutool.core.io.FileUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import top.yalexin.backendcommon.common.ErrorCode;
import top.yalexin.backendcommon.exception.BusinessException;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.backendmodel.model.codesandbox.ExecuteMessage;
import top.yalexin.backendmodel.model.enums.SandBoxStatusEnum;
import top.yalexin.ojbackendcodesandbox.model.SandboxEntry;
import top.yalexin.ojbackendcodesandbox.utils.CodeSandBoxUtils;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
// 每个 session 创建一个实例
//@Scope("session")
public class DockerCodeSandboxTemplate extends CodeSandboxTemplate {

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private SandboxEntry sandboxEntry;

    private final String DOCKER_CODE_DIR = "/";

    // 限制运行时间
    private static final long TIME_OUT = 10 * 1000L;


    @Override
    List<ExecuteMessage> compileAndRun(String language, File userCodeFile, List<String> inputList) {
        // 创建指定编程语言的容器
        String containerId = createContainer(language, userCodeFile);
        // 编译代码（如果是解释性编程语言，则编译过程啥都不做，具体由配置文件配置该命令）
        ExecuteMessage compileExecuteMessage = compileCode(containerId, language);
        // 编译失败
        if (SandBoxStatusEnum.ERROR.getValue().equals(compileExecuteMessage.getExitCode())) {
            log.error("compile failed!: {} ", compileExecuteMessage.getErrorMessage());
            int size = inputList.size();
            List<ExecuteMessage> executeMessageArrayList = new ArrayList<>();
            for (int i = 1; i <= size; i++) {
                executeMessageArrayList.add(compileExecuteMessage);
            }
            return executeMessageArrayList;
        }
        // 根据输入用例执行代码
        List<ExecuteMessage> executeMessages = runCode(containerId, language, inputList);

        // 删除容器
        if (containerId != null) {
            boolean destroyStatus = destroyContainer(containerId);
        }

        return executeMessages;
    }

    /**
     * 创建容器(根据指定的编程语言)
     *
     * @param userCodeFile
     * @param language
     * @return 容器id
     */
    private String createContainer(String language, File userCodeFile) {
        SandboxEntry.DockerInfo dockerInfoByName = sandboxEntry.getDockerInfoByName(language);
        if (dockerInfoByName == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的docker");
        }
        // 镜像
        String image = dockerInfoByName.getImage();
        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        // 最大内存100 MB
        hostConfig.withMemory(100 * 1024 * 1024L);
        hostConfig.withMemorySwap(0L);
        // TODO 设置容器大小
//        HashMap<String, String> storeConfig = new HashMap<>();
//        storeConfig.put("size", "200M");
//        hostConfig.withStorageOpt(storeConfig);

        // 1 核 CPU
        hostConfig.withCpuCount(1L);
        // 通过安全策略限制
        // hostConfig.withSecurityOpts(Arrays.asList("seccomp=xxxxx"));
        CreateContainerResponse createContainerResponse = containerCmd
                .withAttachStdin(true) // 开启标准输入输出流
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withTty(true) // 开启交互式
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true) // 禁止联网
                .exec();
        String containerId = createContainerResponse.getId();
        System.out.println("containerId = " + containerId);

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();
        // 重命名
        File newFile = new File(userCodeFile.getParentFile().getPath() + File.separator + dockerInfoByName.getFilename());
        boolean renameTo = userCodeFile.renameTo(newFile);
        // 复制文件到容器
        String userCodeParentPath = newFile.getPath();

        dockerClient.copyArchiveToContainerCmd(containerId)
                .withHostResource(userCodeParentPath)
                .withRemotePath(DOCKER_CODE_DIR)
                .withNoOverwriteDirNonDir(false)
                .exec();
        return containerId;
    }

    /**
     * 编译代码
     *
     * @param containerId
     * @param language
     * @return 编译成功与否
     */
    private ExecuteMessage compileCode(String containerId, String language) {
        SandboxEntry.DockerInfo dockerInfoByName = sandboxEntry.getDockerInfoByName(language);
        String compileCmd = dockerInfoByName.getCompile();
        ExecuteMessage executeMessage = runCmdWithDocker(containerId, compileCmd, null);
        return executeMessage;
    }

    private List<ExecuteMessage> runCode(String containerId, String language, List<String> inputList) {
        List<ExecuteMessage> executeMessageArrayList = new ArrayList<>();
        SandboxEntry.DockerInfo dockerInfoByName = sandboxEntry.getDockerInfoByName(language);
        String runCmd = dockerInfoByName.getRun();
        for (String inputStr :
                inputList) {
            ExecuteMessage executeMessage = runCmdWithDocker(containerId, runCmd, inputStr);
            executeMessageArrayList.add(executeMessage);
        }
        return executeMessageArrayList;
    }

    /**
     * 在某个容器中执行命令
     *
     * @param containerId 容器id
     * @param cmd         要运行的命令
     * @param inputStr    执行命令过程要传递的参数，如果是编译命令，该值一般为空，如果是运行程序，该值一般是输入用例
     * @return 执行结果
     */
    private ExecuteMessage runCmdWithDocker(String containerId, String cmd, String inputStr) {
        String[] cmdArray = cmd.split(" ");
        final boolean[] timeout = {true};
        ExecuteMessage executeMessage = new ExecuteMessage();
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withCmd(cmdArray)
                .exec();
        // 执行 exec 命令
        try {
            ExecStartResultCallback execStartResultCallback = new MyExecStartResultCallback(timeout, executeMessage);
            final Long[] maxUse = {0L};
            // 监控信息
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback resultCallback = new MyResultCallback(maxUse);
            statsCmd.exec(resultCallback);


            StopWatch stopWatch = new StopWatch();
            // 开启计时器
            stopWatch.start();
            if (inputStr == null) {
                dockerClient.execStartCmd(execCreateCmdResponse.getId())
                        .exec(execStartResultCallback)
                        .awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS); // 最多等待 TIME_OUT MILLISECONDS
            } else {
                // 转为输入流
                byte[] inputBytes = inputStr.getBytes(StandardCharsets.UTF_8);
                InputStream inputStream = new ByteArrayInputStream(inputBytes);
                dockerClient.execStartCmd(execCreateCmdResponse.getId())
                        .withStdIn(inputStream)
                        .exec(execStartResultCallback)
                        .awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS); // 最多等待 TIME_OUT MILLISECONDS
            }

            // 关闭计时器
            stopWatch.stop();
            if (timeout[0]) {
                executeMessage.setExitCode(SandBoxStatusEnum.ERROR.getValue());
                executeMessage.setErrorMessage("Time out");
            }
            long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
            executeMessage.setTimeCost(lastTaskTimeMillis);
            executeMessage.setMemoryCost(maxUse[0]);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return executeMessage;
    }

    /**
     * 销毁指定容器
     *
     * @param containerId
     * @return
     */
    private boolean destroyContainer(String containerId) {
        try {
            // 停止容器
            StopContainerCmd stopContainerCmd = dockerClient.stopContainerCmd(containerId);
            stopContainerCmd.exec();
            // 删除容器
            RemoveContainerCmd removeContainerCmd = dockerClient.removeContainerCmd(containerId);
            removeContainerCmd.exec();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

class MyResultCallback implements ResultCallback<Statistics> {
    private Long[] maxUse;

    public MyResultCallback(Long[] maxUse) {
        this.maxUse = maxUse;
    }

    @Override
    public void onStart(Closeable closeable) {

    }


    /**
     * 记录下使用内存的最大值
     *
     * @param statistics
     */
    @Override
    public void onNext(Statistics statistics) {
        Long curUsage = statistics.getMemoryStats().getUsage();
        maxUse[0] = Math.max(maxUse[0], curUsage);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void close() throws IOException {

    }
}

class MyExecStartResultCallback extends ExecStartResultCallback {
    private boolean[] timeout;
    private ExecuteMessage executeMessage;

    MyExecStartResultCallback(boolean[] timeout, ExecuteMessage executeMessage) {
        this.timeout = timeout;
        this.executeMessage = executeMessage;
    }

    @Override
    public void onComplete() {
        timeout[0] = false;
        super.onComplete();
    }

    @Override
    public void onNext(Frame item) {
        StreamType streamType = item.getStreamType();
        // 处理错误流
        if (streamType.equals(StreamType.STDERR)) {
            executeMessage.setExitCode(SandBoxStatusEnum.ERROR.getValue());
            executeMessage.setErrorMessage(new String(item.getPayload()));
        } else {
            // 处理标准输出
            executeMessage.setExitCode(SandBoxStatusEnum.OK.getValue());
            executeMessage.setMessage(new String(item.getPayload()));
        }

    }
}
