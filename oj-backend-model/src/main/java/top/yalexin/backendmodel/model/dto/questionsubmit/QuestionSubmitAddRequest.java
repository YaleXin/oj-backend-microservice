package top.yalexin.backendmodel.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 编程语言类型
     */
    private String language;

    /**
     * 用户的代码
     */
    private String code;


    private static final long serialVersionUID = 1L;
}