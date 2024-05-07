package top.yalexin.ojbackendcodesandbox.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.yalexin.ojbackendcodesandbox.model.SandboxEntry;

import java.util.List;

@Component
@Data
public class CodeSandBoxUtils {
    @Autowired
    private static SandboxEntry sandboxEntry;

    public static SandboxEntry.DockerInfo getDockerInfoByName(String language){
        List<SandboxEntry.DockerInfo> sandboxList = sandboxEntry.getSandboxList();
        for (SandboxEntry.DockerInfo dockerInfo:sandboxList) {
            if(dockerInfo.getName().equals(language)){
                return dockerInfo;
            }
        }
        return null;
    }
}
