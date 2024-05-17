package top.yalexin.ojbackendcodesandbox;

import cn.hutool.core.io.file.FileReader;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.ojbackendcodesandbox.model.SandboxEntry;
import top.yalexin.ojbackendcodesandbox.service.SandboxService;
import top.yalexin.ojbackendcodesandbox.utils.CodeSandBoxUtils;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class OjBackendCodeSandboxApplicationTests {

    @Autowired
    private SandboxService sandboxService;

    @Autowired
    private SandboxEntry sandboxEntry;

    @Test
    void contextLoads() {
        List<String> supportLanguages = sandboxService.getSupportLanguages();
        System.out.println("supportLanguages = " + supportLanguages);
    }


    @Test
    void testDefault() throws InterruptedException {
        final String DOCKER_HOST = "172.22.108.1";
        final int DOCKER_PORT = 2375;
        String serverUrl = String.format("tcp://%s:%d", DOCKER_HOST, DOCKER_PORT);
        DockerClient dockerClient = DockerClientBuilder.getInstance(serverUrl).build();

        // 容器ID
        String containerId = "613ed37d5346";
        // 模拟输入
        String inputString = "2 99\n";
        String[] userCodeCmd = {"sh", "-c", String.format("echo \"%s\" | %s", inputString, "java Main")};
        System.out.println(Arrays.stream(userCodeCmd)
                .collect(Collectors.joining(" ")));
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withCmd(userCodeCmd)
                .exec();
        // 由于连接容器是一个异步操作，因此要传入回调函数
        ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
            @Override
            public void onNext(Frame frame) {
                if (frame.getStreamType() == StreamType.STDERR) {
                    System.out.println("ERROR -> " + new String(frame.getPayload()));
                } else if (frame.getStreamType() == StreamType.STDOUT) {
                    System.out.println("STDOUT -> " + new String(frame.getPayload()));
                }
                super.onNext(frame);
            }
        };
        dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .exec(execStartResultCallback).awaitCompletion();
    }
    @Test
    void testHttpClient() throws URISyntaxException {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        // 初始化连接
        URI uri = new URI("tcp://172.22.108.1:2375");
        DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(uri)
                .maxConnections(3000)
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).withDockerHttpClient(dockerHttpClient).build();
        // 容器ID
        String containerId = "613ed37d5346";
        // 进入容器后要执行的命令
        String[] cmdArgs = new String[] { "java", "Main"};
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withCmd(cmdArgs)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();
        System.out.println( execCreateCmdResponse.getRawValues());
        // 由于连接容器是一个异步操作，因此要传入回调函数
        ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
            @Override
            public void onNext(Frame frame) {
                if (frame.getStreamType() == StreamType.STDERR) {
                    System.out.println("ERROR -> " + new String(frame.getPayload()));
                } else {
                    System.out.println("STDOUT -> " + new String(frame.getPayload()));
                }
                super.onNext(frame);
            }
        };
        // 模拟输入
        String inputString = "2 5\n";
        // 转为输入流
        byte[] inputBytes = inputString.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        // 传入回调函数并执行
        try {
            dockerClient.execStartCmd(execCreateCmdResponse.getId())
                    .withStdIn(inputStream)
                    .exec(execStartResultCallback)
                    .awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
