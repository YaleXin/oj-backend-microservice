package top.yalexin.ojbackendcodesandbox.service;

import org.springframework.web.bind.annotation.RequestBody;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;

import java.util.List;

public interface SandboxService {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

    List<String> getSupportLanguages();
}
