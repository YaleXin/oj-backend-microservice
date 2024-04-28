package top.yalexin.ojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.yalexin.backendcommon.common.ErrorCode;
import top.yalexin.backendcommon.exception.BusinessException;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;
import top.yalexin.backendmodel.model.dto.question.JudgeCase;
import top.yalexin.backendmodel.model.entity.Question;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;
import top.yalexin.backendmodel.model.enums.QuestionSubmitStatusEnum;
import top.yalexin.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import top.yalexin.ojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import top.yalexin.ojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import top.yalexin.ojbackendjudgeservice.judge.strategy.JudgeContext;
import top.yalexin.ojbackendserviceclient.service.QuestionService;
import top.yalexin.ojbackendserviceclient.service.QuestionSubmitService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Value("${code-sandbox.type}")
    private String codeSandboxType;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1. 根据传入的 id 获取提交信息
        // 2. 如果提交信息的状态不是 “等待中”，则不需要重复执行
        // 3. 否则改为“判题中”
        // 4. 调用沙箱服务，获取执行信息
        // 5. 根据执行信息，判断判题结果
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);

        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 如果提交信息的状态不是 “等待中”
        if (!Objects.equals(questionSubmit.getStatus(), QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交信息还在判题中");
        }

        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.JUDGING.getValue());
        // 更新指定 id
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交信息状态更新失败");
        }
        // 调用沙箱
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(codeSandboxType);
        // 将题目中的测试用力传递给沙箱
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().
                code(code).
                inputList(inputList).
                language(language).build();
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        JudgeInfo responseJudgeInfo = executeCodeResponse.getJudgeInfo();
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(responseJudgeInfo);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);


        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 修改数据库中的 “提交记录”
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        boolean updateById = questionSubmitService.updateById(questionSubmitUpdate);
        if(!updateById){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交信息状态更新失败");
        }
        QuestionSubmit byId = questionSubmitService.getById(questionSubmitId);
        return byId;
    }
}
