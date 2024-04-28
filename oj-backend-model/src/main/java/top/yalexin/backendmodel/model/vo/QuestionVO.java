package top.yalexin.backendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.yalexin.backendmodel.model.dto.question.JudgeConfig;
import top.yalexin.backendmodel.model.entity.Question;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目（用于返回给前端）
 **/

@Data
public class QuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

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
     * 累计提交数目
     */
    private Integer submitNum;

    /**
     * 累计通过数目
     */
    private Integer acceptNum;


    /**
     * 判题配置 例如内存、时间限制（json对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

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

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            // 将 List<Ojb> 转为 字符串
            String jsonStr = JSONUtil.toJsonStr(tagList);
            question.setTags(jsonStr);
        }
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        String tags = question.getTags();
        List<String> tagList = JSONUtil.toList(tags, String.class);
        questionVO.setTags(tagList);
        String judgeConfigStr = question.getJudgeConfig();
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));

        return questionVO;
    }
}