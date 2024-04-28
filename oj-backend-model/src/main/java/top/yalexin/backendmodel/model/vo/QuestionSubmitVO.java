package top.yalexin.backendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.yalexin.backendmodel.model.codesandbox.JudgeInfo;
import top.yalexin.backendmodel.model.entity.QuestionSubmit;


import java.io.Serializable;
import java.util.Date;

/**
 * 评测（用于返回给前端）
 **/

@Data
public class QuestionSubmitVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 编程语言类型
     */
    private String language;

    /**
     * 用户的代码
     */
    private String code;

    /**
     * 判题信息，例如花费的时间和内存（json）
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态 0：待判题，1：判题中，2：成功，3：失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建题目者的信息
     */
    private UserVO userVO;

    /**
     * 所提交的代码对应的题目信息
     */
    private QuestionVO questionVO;

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo judgeInfo1 = questionSubmitVO.getJudgeInfo();
        if (judgeInfo1 != null) {
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo1));
        }
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionVO);
        String judgeInfoStr = questionSubmit.getJudgeInfo();
        questionVO.setJudgeInfo(JSONUtil.toBean(judgeInfoStr, JudgeInfo.class));
        return questionVO;
    }
}