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
        // 编程语言名字
        private String name;
        // 保存的代码文件名
        private String filename;
        // 对应的镜像
        private String image;
        // 编译命令（如果是解释型语言，该命令可以为空）
        private String compile;
        // 运行可执行代码的命令
        private String run;
    }

    public DockerInfo getDockerInfoByName(String language){
        for (SandboxEntry.DockerInfo dockerInfo:sandboxList) {
            if(dockerInfo.getName().equals(language)){
                return dockerInfo;
            }
        }
        return null;
    }
}