package top.yalexin.ojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
// 由于全局异常类不在本模块中，因此需要手动指定扫描
@ComponentScan("top.yalexin")
public class OjBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjBackendJudgeServiceApplication.class, args);
    }

}
