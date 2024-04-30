package top.yalexin.ojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("top.yalexin.ojbackendquestionservice.mapper")
@EnableScheduling
// 由于全局异常类不在本模块中，因此需要手动指定扫描
@ComponentScan("top.yalexin")
// Nacos服务注册与发现
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"top.yalexin.ojbackendserviceclient.service"})
public class OjBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjBackendQuestionServiceApplication.class, args);
    }

}
