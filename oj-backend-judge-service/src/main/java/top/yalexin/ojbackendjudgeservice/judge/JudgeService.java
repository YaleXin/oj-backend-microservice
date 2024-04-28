package top.yalexin.ojbackendjudgeservice.judge;

import top.yalexin.backendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
