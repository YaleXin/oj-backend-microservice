package top.yalexin.ojbackendjudgeservice.judge;

import org.springframework.stereotype.Service;
import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;
import top.yalexin.ojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import top.yalexin.ojbackendjudgeservice.judge.strategy.JavaJudgeStrategy;
import top.yalexin.ojbackendjudgeservice.judge.strategy.JudgeContext;
import top.yalexin.ojbackendjudgeservice.judge.strategy.JudgeStrategy;

@Service
public class JudgeManager {
    public JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if("java".equals(language)){
            judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
