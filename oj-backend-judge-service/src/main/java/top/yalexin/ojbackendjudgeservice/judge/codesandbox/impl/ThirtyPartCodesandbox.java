package top.yalexin.ojbackendjudgeservice.judge.codesandbox.impl;

import lombok.extern.slf4j.Slf4j;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;

/**
 * 第三方代码沙箱
 */
@Slf4j
public class ThirtyPartCodesandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.debug("ThirtyPartCodesandbox...");
        return null;
    }
}
