package top.yalexin.ojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import top.yalexin.backendmodel.model.codesandbox.ExecuteMessage;
import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;
import top.yalexin.backendmodel.model.dto.question.JudgeCase;
import top.yalexin.backendmodel.model.dto.question.JudgeConfig;
import top.yalexin.backendmodel.model.entity.Question;
import top.yalexin.backendmodel.model.enums.JudgeInfoMessageEnum;
import top.yalexin.backendmodel.model.enums.SandBoxStatusEnum;

import java.util.List;

public class JavaJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<String> expectOutputList = judgeContext.getExpectOutputList();
        List<ExecuteMessage> executeMessages = judgeContext.getExecuteMessages();


        Question question = judgeContext.getQuestion();
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        JudgeInfo judgeInfoResponse = new JudgeInfo();




        // 判断逻辑：
        // 5.1 判断执行结果是否和预期结果数量是否一致
        // 5.2 判断每一项是否都一致
        // 5.3 判断资源是否超限（只要有一个测试用例的时间或者内存超限，则判为超限）
        JudgeInfoMessageEnum judgeInfoMessage = JudgeInfoMessageEnum.AC;
        // 输出长度和预期输出长度不一致，则答案错误
        if (expectOutputList.size() != executeMessages.size()){
            judgeInfoMessage = JudgeInfoMessageEnum.WA;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }

        for (int i = 0; i < expectOutputList.size(); i++) {
            String expectOutput = expectOutputList.get(i);
            ExecuteMessage executeMessage = executeMessages.get(i);
            // 运行时错误
            if (SandBoxStatusEnum.ERROR.getValue().equals(executeMessage.getExitCode())){
                judgeInfoMessage = JudgeInfoMessageEnum.RE;
                judgeInfoResponse.setMessage(judgeInfoMessage.getText());
                return judgeInfoResponse;
            }
            // 答案错误
            if(!expectOutput.equals(executeMessage.getMessage())){
                judgeInfoMessage = JudgeInfoMessageEnum.WA;
                judgeInfoResponse.setMessage(judgeInfoMessage.getText());
                return judgeInfoResponse;
            }
            // 时间超限
            if(executeMessage.getTimeCost() > judgeConfig.getTimeLimit()){
                judgeInfoMessage = JudgeInfoMessageEnum.TLE;
                judgeInfoResponse.setMessage(judgeInfoMessage.getText());
                return judgeInfoResponse;
            }
            // 内存超限
            if(executeMessage.getMemoryCost() > judgeConfig.getMemoryLimit()){
                judgeInfoMessage = JudgeInfoMessageEnum.MLE;
                judgeInfoResponse.setMessage(judgeInfoMessage.getText());
                return judgeInfoResponse;
            }
            // 默认最后一个执行信息作为最终返回的信息
            judgeInfoResponse.setMessage("");
            judgeInfoResponse.setTimeCost(executeMessage.getTimeCost());
            judgeInfoResponse.setMemoryCost(executeMessage.getMemoryCost());
        }
        judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
        return judgeInfoResponse;
    }
}
