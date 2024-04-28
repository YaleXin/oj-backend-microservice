package top.yalexin.ojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;
import top.yalexin.backendmodel.model.dto.question.JudgeCase;
import top.yalexin.backendmodel.model.dto.question.JudgeConfig;
import top.yalexin.backendmodel.model.entity.Question;
import top.yalexin.backendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;


/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();

        JudgeInfo outputJudgeInfo = judgeContext.getJudgeInfo();
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);

        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemoryCost(outputJudgeInfo.getMemoryCost());
        judgeInfoResponse.setTimeCost(outputJudgeInfo.getTimeCost());


        // 判断逻辑：
        // 5.1 判断执行结果是否和预期结果数量是否一致
        // 5.2 判断每一项是否都一致
        // 5.3 判断资源是否超限
        JudgeInfoMessageEnum judgeInfoMessage = JudgeInfoMessageEnum.AC;
        // 答案错误
        if (outputList.size() != inputList.size()){
            judgeInfoMessage = JudgeInfoMessageEnum.WA;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        // 答案错误
        for (int i = 0; i < outputList.size(); i++) {
            if(!outputList.get(i).equals(inputList.get(i))){
                judgeInfoMessage = JudgeInfoMessageEnum.WA;
                judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
                return judgeInfoResponse;
            }
        }
        // 时间超限
        if(outputJudgeInfo.getTimeCost() > judgeConfig.getTimeLimit()){
            judgeInfoMessage = JudgeInfoMessageEnum.TLE;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        // 内存超限
        if(outputJudgeInfo.getMemoryCost() > judgeConfig.getMemoryLimit()){
            judgeInfoMessage = JudgeInfoMessageEnum.MLE;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
        return judgeInfoResponse;
    }
}
