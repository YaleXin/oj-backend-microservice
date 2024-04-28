package top.yalexin.ojbackendjudgeservice.judge.strategy;

import lombok.Data;
import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;
import top.yalexin.backendmodel.model.dto.question.JudgeCase;
import top.yalexin.backendmodel.model.entity.Question;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;

import java.util.List;

@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
