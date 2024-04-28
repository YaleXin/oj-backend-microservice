package top.yalexin.ojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.yalexin.backendcommon.common.ErrorCode;
import top.yalexin.backendcommon.constant.CommonConstant;
import top.yalexin.backendcommon.exception.BusinessException;
import top.yalexin.backendcommon.utils.SqlUtils;
import top.yalexin.backendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import top.yalexin.backendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import top.yalexin.backendmodel.model.entity.Question;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;

import top.yalexin.backendmodel.model.entity.User;
import top.yalexin.backendmodel.model.enums.QuestionSubmitLanguageEnum;
import top.yalexin.backendmodel.model.enums.QuestionSubmitStatusEnum;
import top.yalexin.backendmodel.model.vo.QuestionSubmitVO;
import top.yalexin.ojbackendquestionservice.mapper.QuestionSubmitMapper;
import top.yalexin.ojbackendquestionservice.service.QuestionService;
import top.yalexin.ojbackendquestionservice.service.QuestionSubmitService;
import top.yalexin.ojbackendserviceclient.service.JudgeService;
import top.yalexin.ojbackendserviceclient.service.UserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Yalexin
 * @description 针对表【question_submit(用户提交的代码评测)】的数据库操作Service实现
 * @createDate 2024-04-13 23:00:56
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    //     @Lazy 避免循环依赖
    @Lazy
    private JudgeService judgeService;

    /**
     * 提交代码
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言选择错误");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交代码
        long userId = loginUser.getId();
        // 每个用户串行提交代码
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户提交代码插入失败");
        }
        Long submitId = questionSubmit.getId();
        //  调用判题服务（异步）
        CompletableFuture.runAsync(() ->{
            judgeService.doJudge(submitId);
        });

        return submitId;
    }

    /**
     * 获取查询包装类
     * 用户根据修改字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String language = questionSubmitQueryRequest.getLanguage();
        Long userId = questionSubmitQueryRequest.getUserId();
        Integer status = questionSubmitQueryRequest.getStatus();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);

        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 给 QuestionVO 补充一些信息
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 只有自己或者管理员能查看自己提交的 code ，自己能看到别人的评测信息有限
        long questionSubmitUserId = questionSubmit.getUserId();
        // 如果评测记录不是当前登录用户提交的，或者当前用户不是管理者，则代码设置为空
        if (loginUser.getId() != questionSubmitUserId && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser) {
        List<QuestionSubmit> questionList = questionPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionSubmitVOPage;
        }

        List<QuestionSubmitVO> questionSubmitVOList = questionList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser)).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }


}




