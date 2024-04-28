package top.yalexin.ojbackendjudgeservice.judge.codesandbox;

import lombok.extern.slf4j.Slf4j;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox {
    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox){
        this.codeSandbox = codeSandbox;
    }
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.debug("before execute, executeCodeRequest = " + executeCodeRequest);

        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);

        log.debug("after execute, executeCodeResponse = " + executeCodeResponse);
        return executeCodeResponse;
    }
}
