package top.yalexin.ojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import top.yalexin.backendcommon.common.ErrorCode;
import top.yalexin.backendcommon.exception.BusinessException;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeRequest;
import top.yalexin.backendmodel.model.codesandbox.ExecuteCodeResponse;
import top.yalexin.ojbackendjudgeservice.judge.codesandbox.CodeSandbox;

/**
 * 远程代码沙箱
 */
@Slf4j
public class RemoteCodesandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.debug("RemoteCodesandbox...");
        String url = "http://172.22.108.1:8080/executeCode";
        String jsonStr = JSONUtil.toJsonStr(executeCodeRequest);
        String body = HttpUtil.createPost(url)
                .body(jsonStr)
                .execute()
                .body();
        if(StringUtils.isBlank(body)){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "remote code sandbox error");
        }
        return JSONUtil.toBean(body, ExecuteCodeResponse.class);
    }
}
