package top.yalexin.backendmodel.model.codesandbox;

import lombok.Data;

@Data
public class ExecuteMessage {
    private Integer exitCode;

    private String message;

    private String errorMessage;

    private Long timeCost;

    private Long memoryCost;
}
