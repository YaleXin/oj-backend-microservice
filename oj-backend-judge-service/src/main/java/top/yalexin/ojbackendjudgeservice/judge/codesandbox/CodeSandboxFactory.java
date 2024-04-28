package top.yalexin.ojbackendjudgeservice.judge.codesandbox;

import top.yalexin.ojbackendjudgeservice.judge.codesandbox.impl.ExampleCodesandbox;
import top.yalexin.ojbackendjudgeservice.judge.codesandbox.impl.RemoteCodesandbox;
import top.yalexin.ojbackendjudgeservice.judge.codesandbox.impl.ThirtyPartCodesandbox;

/**
 * 静态工厂模式：根据指定的类型，生成对应的代码沙箱类型
 */
public class CodeSandboxFactory {
    public static CodeSandbox newInstance(String type) {
        CodeSandbox codeSandbox;
        switch (type) {
            case "example":
                codeSandbox = new ExampleCodesandbox();
                break;
            case "remote":
                codeSandbox = new RemoteCodesandbox();
                break;
            case "thirtyPart":
                codeSandbox = new ThirtyPartCodesandbox();
                break;
            default:
                codeSandbox = new ExampleCodesandbox();
        }
        return codeSandbox;
    }
}
