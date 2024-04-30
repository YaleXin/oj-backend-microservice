package top.yalexin.ojbackendserviceclient.service;


import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.yalexin.backendcommon.common.ErrorCode;
import top.yalexin.backendcommon.exception.BusinessException;
import top.yalexin.backendmodel.model.entity.User;
import top.yalexin.backendmodel.model.enums.UserRoleEnum;
import top.yalexin.backendmodel.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static top.yalexin.backendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 */
// 将接口暴露出去
@FeignClient(name = "oj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient  {

    // userService.getById(userId)
    // userService.getUserVO(user)
    // userService.listByIds(userIdSet)
    // userService.isAdmin(loginUser)
    // userService.getLoginUser(request)

    /**
     * 根据 id 查询用户
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") Long userId);

    /**
     * 根据 id 获取用户列表
     * @param idList
     * @return
     */
    @GetMapping("/get/ids")
    List<User>listByIds(@RequestParam("idList")Collection<Long> idList);

    // -------- 下面几个方法由于不涉及数据库操作，实现逻辑也比较简单，
    // -------- 因此可直接使用default方式定义，而无需子类去实现
    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user){
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }


    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }


}
