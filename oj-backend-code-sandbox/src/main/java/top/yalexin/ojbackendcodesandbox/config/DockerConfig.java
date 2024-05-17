package top.yalexin.ojbackendcodesandbox.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

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

    @Value("${code-sandbox.max-connections}")
    private Integer maxConnections;


    /**
     * 初始化 DockerClient实例
     * @return
     */
    @Bean
    public DockerClient getDockerInstance() throws URISyntaxException {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        // 初始化连接
        URI uri = new URI(String.format("tcp://%s:%s",sandboxHost, sandboxPort));
        DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(uri)
                .maxConnections(maxConnections)
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).withDockerHttpClient(dockerHttpClient).build();
        return dockerClient;
    }
}
