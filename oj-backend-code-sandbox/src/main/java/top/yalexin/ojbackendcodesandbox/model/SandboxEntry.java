package top.yalexin.ojbackendcodesandbox.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "code-sandbox")
public class SandboxEntry {
    private List<DockerInfo> sandboxList;

    @Data
    public static class  DockerInfo{
        private String name;
        private String image;
        private String compile;
        private String run;
    }
}