package top.yalexin.ojbackendquestionservice.controller.inner;

import org.springframework.web.bind.annotation.*;
import top.yalexin.backendmodel.model.entity.Question;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;
import top.yalexin.ojbackendquestionservice.service.QuestionService;
import top.yalexin.ojbackendquestionservice.service.QuestionSubmitService;
import top.yalexin.ojbackendserviceclient.service.QuestionFeignClient;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class InnerQuestionController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") Long questionId) {
        return questionService.getById(questionId);
    }

    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId){
        return questionSubmitService.getById(questionSubmitId);
    }


    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit){
        return questionSubmitService.updateById(questionSubmit);
    }
}
