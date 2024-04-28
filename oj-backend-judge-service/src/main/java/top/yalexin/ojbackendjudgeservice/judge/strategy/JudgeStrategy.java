package top.yalexin.ojbackendjudgeservice.judge.strategy;

import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
