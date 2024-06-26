package top.yalexin.backendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 */
public enum UserRoleEnum {

    USER("用户", "user", 100),
    ADMIN("管理员", "admin", 200),
    BAN("被封号", "ban", -1);

    private final String text;

    private final String value;

    // 权限等级，值越大，等级越高，拥有的权限越大
    private final Integer level;

    UserRoleEnum(String text, String value, Integer level) {
        this.text = text;
        this.value = value;
        this.level = level;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public Integer getLevel(){
        return level;
    }
}
