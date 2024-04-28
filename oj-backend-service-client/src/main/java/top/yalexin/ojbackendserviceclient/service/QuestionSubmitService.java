package top.yalexin.ojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yalexin.backendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import top.yalexin.backendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;
import top.yalexin.backendmodel.model.entity.User;
import top.yalexin.backendmodel.model.vo.QuestionSubmitVO;


/**
* @author Yalexin
* @description 针对表【question_submit(用户提交的代码评测)】的数据库操作Service
* @createDate 2024-04-13 23:00:56
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取评测封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取评测封装
     *
     * @param questionPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser);
}
