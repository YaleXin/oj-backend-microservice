package top.yalexin.ojbackendserviceclient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
@FeignClient(name = "oj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") Long questionSubmitId);
}
