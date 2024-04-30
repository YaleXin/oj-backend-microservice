package top.yalexin.ojbackendjudgeservice.judge.strategy;

import lombok.Data;
import top.yalexin.backendmodel.model.codesandbox.ExecuteMessage;
import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;
import top.yalexin.backendmodel.model.dto.question.JudgeCase;
import top.yalexin.backendmodel.model.entity.Question;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;

import java.util.List;

@Data
public class JudgeContext {
    // 执行信息
    List<ExecuteMessage> executeMessages;
    // 预期输出
    private List<String> expectOutputList;

//    private List<JudgeCase> judgeCaseList;
    // 对应的题目信息
    private Question question;
    // 对应的提交信息
    private QuestionSubmit questionSubmit;
}
