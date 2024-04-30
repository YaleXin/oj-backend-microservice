package top.yalexin.ojbackendjudgeservice.judge.codesandbox.impl;

import lombok.extern.slf4j.Slf4j;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;
import top.yalexin.backendmodel.model.enums.QuestionSubmitStatusEnum;
import top.yalexin.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;


import java.util.List;

/**
 * 示例代码沙箱
 */
@Slf4j
public class ExampleCodesandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.debug("ExampleCodesandbox...");
        // 先获取判题信息
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        List<String> inputList = executeCodeRequest.getInputList();


        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemoryCost(100L);
        judgeInfo.setTimeCost(100L);
        judgeInfo.setMessage("AC");

        return executeCodeResponse;
    }
}
