package top.yalexin.ojbackendcodesandbox.sandbox;

import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
