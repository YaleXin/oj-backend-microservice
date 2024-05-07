package top.yalexin.ojbackendcodesandbox;

import cn.hutool.core.io.file.FileReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.ojbackendcodesandbox.model.SandboxEntry;
import top.yalexin.ojbackendcodesandbox.service.SandboxService;
import top.yalexin.ojbackendcodesandbox.utils.CodeSandBoxUtils;


import java.util.Arrays;
import java.util.List;

@SpringBootTest
class OjBackendCodeSandboxApplicationTests {

    @Autowired
    private SandboxService sandboxService;

    @Autowired
    private SandboxEntry sandboxEntry;

    @Test
    void contextLoads() {
        List<String> supportLanguages = sandboxService.getSupportLanguages();
        System.out.println("supportLanguages = " + supportLanguages);
    }

    @Test
    void testJavaCode() {
//        SandboxEntry.DockerInfo java = sandboxEntry.getDockerInfoByName("java");
//        System.out.println("java = " + java);
        FileReader fileReader = new FileReader("C:\\Users\\Yalexin\\IdeaProjects\\oj-backend-microservice\\oj-backend-code-sandbox\\src\\test\\java\\top\\yalexin\\ojbackendcodesandbox\\Main.java");
        String result = fileReader.readString();
        String replace = result.replace("\r\n", "");
        String replace1 = replace.replace("\n", "");

        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        List<String> input = Arrays.asList("1 2", "4 8");
        executeCodeRequest.setCode(replace1);
        executeCodeRequest.setInputList(input);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = sandboxService.executeCode(executeCodeRequest);
        System.out.println("executeCodeResponse = " + executeCodeResponse);
    }


    @Test
    void testCCode() {
//        SandboxEntry.DockerInfo java = sandboxEntry.getDockerInfoByName("java");
//        System.out.println("java = " + java);
        FileReader fileReader = new FileReader("C:\\Users\\Yalexin\\IdeaProjects\\oj-backend-microservice\\oj-backend-code-sandbox\\src\\test\\java\\top\\yalexin\\ojbackendcodesandbox\\Main.c");
        String result = fileReader.readString();


        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        List<String> input = Arrays.asList("1 12", "14 8");
        executeCodeRequest.setCode(result);
        executeCodeRequest.setInputList(input);
        executeCodeRequest.setLanguage("C");
        ExecuteCodeResponse executeCodeResponse = sandboxService.executeCode(executeCodeRequest);
        System.out.println("executeCodeResponse = " + executeCodeResponse);
    }

    @Test
    void testCPlusCode() {
//        SandboxEntry.DockerInfo java = sandboxEntry.getDockerInfoByName("java");
//        System.out.println("java = " + java);
        FileReader fileReader = new FileReader("C:\\Users\\Yalexin\\IdeaProjects\\oj-backend-microservice\\oj-backend-code-sandbox\\src\\test\\java\\top\\yalexin\\ojbackendcodesandbox\\Main.c");
        String result = fileReader.readString();


        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        List<String> input = Arrays.asList("111 12", "114 8");
        executeCodeRequest.setCode(result);
        executeCodeRequest.setInputList(input);
        executeCodeRequest.setLanguage("C++");
        ExecuteCodeResponse executeCodeResponse = sandboxService.executeCode(executeCodeRequest);
        System.out.println("executeCodeResponse = " + executeCodeResponse);
    }
}
