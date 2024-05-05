package top.yalexin.ojbackendcodesandbox.init;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.yalexin.ojbackendcodesandbox.model.SandboxEntry;


import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
@Slf4j
// 确定优先级
@Order(1)
public class DockerImageInit implements CommandLineRunner {
    @Autowired
    private SandboxEntry sandboxEntry;

    @Override
    public void run(String... args) throws Exception {
        List<SandboxEntry.DockerInfo> sandboxList = sandboxEntry.getSandboxList();
        log.info("sandbox = {}", sandboxList);
        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://172.22.108.1:2375").build();
        List<Image> images = dockerClient.listImagesCmd().exec();

        String[] repoTags = images.get(4).getRepoTags();
        List<String> collect = Arrays.stream(repoTags).map(String::toString).collect(Collectors.toList());



        List<String[]> imageListArray = images.stream().map(Image::getRepoTags).collect(Collectors.toList());

        Set<String> imageSet = imageListArray.stream()
                .flatMap(array -> Arrays.stream(array))
                .collect(Collectors.toSet());
        log.info("imageSet = {}", imageSet);
        for (SandboxEntry.DockerInfo entry : sandboxList) {
            String image = entry.getImage();
            if (!imageSet.contains(image)) {
                downDockerImage(dockerClient, image);
            }
        }
    }

    private boolean downDockerImage(DockerClient dockerClient, String image) {
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                System.out.println("下载镜像" + item.getStatus());
                super.onNext(item);
            }
        };
        try {
            pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }
}
