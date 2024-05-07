package top.yalexin.ojbackendcodesandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

// 由于模块不需要和数据库打交道，因此要将数据库自动配置取消
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// 由于全局异常类不在本模块中，因此需要手动指定扫描
@ComponentScan("top.yalexin")
// Nacos服务注册与发现
@EnableDiscoveryClient
public class OjBackendCodeSandboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjBackendCodeSandboxApplication.class, args);
    }

}
