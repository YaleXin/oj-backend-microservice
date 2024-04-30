package top.yalexin.backendmodel.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    // 长度和输入长度一致，即每个输入对应有一个ExecuteMessage
    private List<ExecuteMessage> executeMessages;
}
