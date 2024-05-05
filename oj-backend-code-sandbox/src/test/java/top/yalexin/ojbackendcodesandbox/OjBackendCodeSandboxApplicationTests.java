package top.yalexin.ojbackendcodesandbox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yalexin.ojbackendcodesandbox.service.SandboxService;

import java.util.List;

@SpringBootTest
class OjBackendCodeSandboxApplicationTests {

    @Autowired
    private SandboxService sandboxService;

    @Test
    void contextLoads() {
        List<String> supportLanguages = sandboxService.getSupportLanguages();
        System.out.println("supportLanguages = " + supportLanguages);
    }

}
