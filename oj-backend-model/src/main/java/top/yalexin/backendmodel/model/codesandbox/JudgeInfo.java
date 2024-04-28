package top.yalexin.backendmodel.model.codesandbox;

import lombok.Data;

/**
 * 评判信息
 */
@Data
public class JudgeInfo {
    /**
     * 花费的时间，单位为 ms
     */
    private Long timeCost;
    /**
     * 花费的内存，单位为 kb
     */
    private Long memoryCost;
    /**
     * 程序执行的信息
     */
    private String message;
}
