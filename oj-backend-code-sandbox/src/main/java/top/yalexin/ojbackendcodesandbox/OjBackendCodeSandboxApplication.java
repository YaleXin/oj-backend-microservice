package top.yalexin.ojbackendcodesandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// 由于模块不需要和数据库打交道，因此要将数据库自动配置取消
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OjBackendCodeSandboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjBackendCodeSandboxApplication.class, args);
    }

}
