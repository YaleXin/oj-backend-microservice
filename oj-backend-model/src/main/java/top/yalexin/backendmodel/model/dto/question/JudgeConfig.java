package top.yalexin.backendmodel.model.dto.question;

import lombok.Data;

/**
 * 题目的配置
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制，单位为 ms
     */
    private Long timeLimit;
    /**
     * 内存限制，单位为 kb
     */
    private Long memoryLimit;
    /**
     * 堆栈限制，单位为 kb
     */
    private Long stackLimit;
}
