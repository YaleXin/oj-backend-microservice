package top.yalexin.backendmodel.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 */
@Data
public class QuestionAddRequest implements Serializable {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 答案
     */
    private String answer;

    /**
     * 判题示例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置 例如内存、时间限制
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}