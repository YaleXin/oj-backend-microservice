package top.yalexin.backendmodel.model.dto.questionsubmit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.yalexin.backendcommon.common.PageRequest;


import java.io.Serializable;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 编程语言类型
     */
    private String language;

    /**
     * 评测状态
     */
    private Integer status;

    /**
     * 用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}