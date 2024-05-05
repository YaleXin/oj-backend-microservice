package top.yalexin.ojbackendcodesandbox.sandbox;

import cn.hutool.core.io.FileUtil;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.backendmodel.model.codesandbox.ExecuteMessage;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public abstract class CodeSandboxTemplate {

    private static final String GLOBAL_CODE_DIR = "tmpCode";
    private static final String GLOBAL_CODE_FILE_NAME = "Main";

    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        List<String> inputList = executeCodeRequest.getInputList();

        File userCodeFile = saveFile(code);
        // 保存用户代码为文件
        String userCodeParentPath = userCodeFile.getParentFile().getPath();

        // 编译代码并执行代码
        List<ExecuteMessage> executeMessageArrayList = compileAndRun(userCodeFile, inputList);

        // 整理输出信息
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessageArrayList);

        // 文件清理
        clearFile(userCodeFile);
        return outputResponse;
    }

    abstract List<ExecuteMessage> compileAndRun(File userCodeFile, List<String> inputList);

    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageArrayList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setExecuteMessages(executeMessageArrayList);
        return executeCodeResponse;
    }

    public boolean clearFile(File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getPath();
        boolean del = false;
        if (userCodeFile.getParentFile() != null) {
            del = FileUtil.del(userCodeParentPath);
        }
        return del;
    }
    public File saveFile(String code) {
        String userDir = System.getProperty("user.dir");
        String codePath = userDir + File.separator + GLOBAL_CODE_DIR;
        // 如果存放代码的目录不存在，则创建
        if (!FileUtil.exist(codePath)) {
            FileUtil.mkdir(codePath);
        }
        // 将代码保存到文件中
        String userCodeParentPath = codePath + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_CODE_FILE_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

}
