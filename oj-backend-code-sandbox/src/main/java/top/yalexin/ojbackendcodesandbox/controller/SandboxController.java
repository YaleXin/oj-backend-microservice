package top.yalexin.ojbackendcodesandbox.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yalexin.backendcommon.common.BaseResponse;
import top.yalexin.backendcommon.common.ResultUtils;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.ojbackendcodesandbox.sandbox.DockerCodeSandboxTemplate;
import top.yalexin.ojbackendcodesandbox.service.SandboxService;


import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController("/")
public class SandboxController {

    @Resource
    @Autowired
    private SandboxService sandboxService;

    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest){
        if(executeCodeRequest == null){
            throw new RuntimeException("executeCodeRequest == null");
        }
        log.info("receive executeCodeRequest = {}", executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = sandboxService.executeCode(executeCodeRequest);
        log.info("generate executeCodeResponse = {}", executeCodeResponse);
        return executeCodeResponse;
    }

    @GetMapping("/languages")
    BaseResponse<List<String>> getLanguages(){
        List<String> supportLanguages = sandboxService.getSupportLanguages();
        return ResultUtils.success(supportLanguages);
    }
}