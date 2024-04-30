package top.yalexin.ojbackendjudgeservice.controller.inner;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;
import top.yalexin.ojbackendjudgeservice.judge.JudgeService;
import top.yalexin.ojbackendserviceclient.service.JudgeFeignClient;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class InnerJudgeController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") Long questionSubmitId){
        return judgeService.doJudge(questionSubmitId);
    }
}
