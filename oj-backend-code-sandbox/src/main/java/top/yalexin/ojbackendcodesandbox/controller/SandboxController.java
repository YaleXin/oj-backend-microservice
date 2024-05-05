package top.yalexin.ojbackendcodesandbox.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.ojbackendcodesandbox.sandbox.DockerCodeSandboxTemplate;


import javax.annotation.Resource;
@Slf4j
@RestController("/")
public class SandboxController {

    @Resource
    private DockerCodeSandboxTemplate javaDockerCodeSandbox;

    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest){
        if(executeCodeRequest == null){
            throw new RuntimeException("executeCodeRequest == null");
        }
        log.info("receive executeCodeRequest = {}", executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSandbox.executeCode(executeCodeRequest);
        log.info("generate executeCodeResponse = {}", executeCodeResponse);
        return executeCodeResponse;
    }
}