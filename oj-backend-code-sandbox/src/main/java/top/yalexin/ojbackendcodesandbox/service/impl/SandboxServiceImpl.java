package top.yalexin.ojbackendcodesandbox.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.ojbackendcodesandbox.model.SandboxEntry;
import top.yalexin.ojbackendcodesandbox.sandbox.CodeSandbox;
import top.yalexin.ojbackendcodesandbox.sandbox.DockerCodeSandboxTemplate;
import top.yalexin.ojbackendcodesandbox.service.SandboxService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SandboxServiceImpl implements SandboxService {


    @Autowired
    private SandboxEntry sandboxEntry;

    @Autowired
    private DockerCodeSandboxTemplate dockerCodeSandboxTemplate;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return dockerCodeSandboxTemplate.executeCode(executeCodeRequest);
    }

    @Override
    public List<String> getSupportLanguages() {
        List<SandboxEntry.DockerInfo> sandboxList = sandboxEntry.getSandboxList();
        List<String> languages = sandboxList.stream().map(SandboxEntry.DockerInfo::getName).collect(Collectors.toList());
        return languages;
    }
}
