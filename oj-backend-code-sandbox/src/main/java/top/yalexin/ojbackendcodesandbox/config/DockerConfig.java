package top.yalexin.ojbackendcodesandbox.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 dockerClient
 */
@Data
@Configuration
public class DockerConfig {
    @Value("${code-sandbox.host}")
    private String sandboxHost;

    @Value("${code-sandbox.port}")
    private String sandboxPort;

    /**
     * 初始化 DockerClient实例
     * @return
     */
    @Bean
    public DockerClient getDockerInstance() {
        String serverUrl = String.format("tcp://%s:%s", sandboxHost, sandboxPort);
        DockerClient dockerClient = DockerClientBuilder.getInstance(serverUrl).build();
        return dockerClient;
    }
}
