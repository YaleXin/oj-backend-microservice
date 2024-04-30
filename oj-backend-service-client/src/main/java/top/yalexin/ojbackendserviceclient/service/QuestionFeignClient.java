package top.yalexin.ojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.yalexin.backendmodel.model.dto.question.QuestionQueryRequest;
import top.yalexin.backendmodel.model.entity.Question;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;
import top.yalexin.backendmodel.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yalexin
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2024-04-13 22:56:32
 */
// name 指明所在模块，path指定 url 前缀
@FeignClient(name = "oj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") Long questionId);


    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId);


    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
