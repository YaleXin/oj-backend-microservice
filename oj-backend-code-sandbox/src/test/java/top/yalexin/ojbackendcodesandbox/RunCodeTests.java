package top.yalexin.ojbackendcodesandbox;

import cn.hutool.core.io.file.FileReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.backendmodel.model.codesandbox.ExecuteMessage;
import top.yalexin.backendmodel.model.enums.SandBoxStatusEnum;
import top.yalexin.ojbackendcodesandbox.service.SandboxService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class RunCodeTests {

    @Autowired
    private SandboxService sandboxService;


    @Test
    void testJavaCode() {

        FileReader fileReader = new FileReader("C:\\Users\\Yalexin\\IdeaProjects\\oj-backend-microservice\\oj-backend-code-sandbox\\src\\test\\java\\top\\yalexin\\ojbackendcodesandbox\\Main.java");
        String result = fileReader.readString();

        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        List<String> input = Arrays.asList("1 2\n", "4 55\n");
        List<String> output = Arrays.asList("3\n", "59\n");
        executeCodeRequest.setCode(result);
        executeCodeRequest.setInputList(input);
        executeCodeRequest.setLanguage("Java");
        ExecuteCodeResponse executeCodeResponse = sandboxService.executeCode(executeCodeRequest);
        System.out.println("executeCodeResponse = " + executeCodeResponse);
        List<ExecuteMessage> executeMessages = executeCodeResponse.getExecuteMessages();
        for(int index = 0; index<executeMessages.size(); index++) {
            ExecuteMessage executeMessage = executeMessages.get(index);
            assertEquals("非正常退出", SandBoxStatusEnum.OK.getValue(), executeMessage.getExitCode());
            assertEquals("运行结果出错",  output.get(index).trim(), executeMessage.getMessage().trim());
        }

    }


    @Test
    void testCCode() {

        FileReader fileReader = new FileReader("C:\\Users\\Yalexin\\IdeaProjects\\oj-backend-microservice\\oj-backend-code-sandbox\\src\\test\\java\\top\\yalexin\\ojbackendcodesandbox\\Main.c");
        String result = fileReader.readString();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        List<String> input = Arrays.asList("1 11\n", "44 55\n");
        List<String> output = Arrays.asList("12\n", "99\n");
        executeCodeRequest.setCode(result);
        executeCodeRequest.setInputList(input);
        executeCodeRequest.setLanguage("C");
        ExecuteCodeResponse executeCodeResponse = sandboxService.executeCode(executeCodeRequest);
        System.out.println("executeCodeResponse = " + executeCodeResponse);
        List<ExecuteMessage> executeMessages = executeCodeResponse.getExecuteMessages();
        for(int index = 0; index<executeMessages.size(); index++) {
            ExecuteMessage executeMessage = executeMessages.get(index);
            assertEquals("非正常退出", SandBoxStatusEnum.OK.getValue(), executeMessage.getExitCode());
            assertEquals("运行结果出错",  output.get(index).trim(), executeMessage.getMessage().trim());
        }

    }

    @Test
    void testCPlusCode() {

        FileReader fileReader = new FileReader("C:\\Users\\Yalexin\\IdeaProjects\\oj-backend-microservice\\oj-backend-code-sandbox\\src\\test\\java\\top\\yalexin\\ojbackendcodesandbox\\Main.cpp");
        String result = fileReader.readString();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        List<String> input = Arrays.asList("1 111\n", "44 55\n");
        List<String> output = Arrays.asList("112\n", "99\n");
        executeCodeRequest.setCode(result);
        executeCodeRequest.setInputList(input);
        executeCodeRequest.setLanguage("C++");
        ExecuteCodeResponse executeCodeResponse = sandboxService.executeCode(executeCodeRequest);
        System.out.println("executeCodeResponse = " + executeCodeResponse);
        List<ExecuteMessage> executeMessages = executeCodeResponse.getExecuteMessages();
        for(int index = 0; index<executeMessages.size(); index++) {
            ExecuteMessage executeMessage = executeMessages.get(index);
            assertEquals("非正常退出", SandBoxStatusEnum.OK.getValue(), executeMessage.getExitCode());
            assertEquals("运行结果出错",  output.get(index).trim(), executeMessage.getMessage().trim());
        }

    }

    @Test
    void testPythonCode() {


        FileReader fileReader = new FileReader("C:\\Users\\Yalexin\\IdeaProjects\\oj-backend-microservice\\oj-backend-code-sandbox\\src\\test\\java\\top\\yalexin\\ojbackendcodesandbox\\Main.py");
        String result = fileReader.readString();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        List<String> input = Arrays.asList("1 11111\n", "44 55\n");
        List<String> output = Arrays.asList("11112\n", "99\n");
        executeCodeRequest.setCode(result);
        executeCodeRequest.setInputList(input);
        executeCodeRequest.setLanguage("Python");
        ExecuteCodeResponse executeCodeResponse = sandboxService.executeCode(executeCodeRequest);
        System.out.println("executeCodeResponse = " + executeCodeResponse);
        List<ExecuteMessage> executeMessages = executeCodeResponse.getExecuteMessages();
        for(int index = 0; index<executeMessages.size(); index++) {
            ExecuteMessage executeMessage = executeMessages.get(index);
            assertEquals("非正常退出", SandBoxStatusEnum.OK.getValue(), executeMessage.getExitCode());
            assertEquals("运行结果出错",  output.get(index).trim(), executeMessage.getMessage().trim());
        }

    }

}
