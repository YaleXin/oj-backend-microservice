package top.yalexin.ojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("top.yalexin.ojbackenduserservice.mapper")
@EnableScheduling
// 由于全局异常类不在本模块中，因此需要手动指定扫描
@ComponentScan("top.yalexin")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class OjBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjBackendUserServiceApplication.class, args);
    }

}
