package top.yalexin.ojbackenduserservice.controller.inner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.yalexin.backendmodel.model.entity.User;
import top.yalexin.ojbackendserviceclient.service.UserFeignClient;
import top.yalexin.ojbackenduserservice.service.UserService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/inner")
public class InnerUserController implements UserFeignClient {


    @Resource
    private UserService userService;

    /**
     * 根据 id 查询用户
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    @Override
    public User getById(@RequestParam("userId") Long userId){
        return userService.getById(userId);
    }

    /**
     * 根据 id 获取用户列表
     * @param idList
     * @return
     */
    @GetMapping("/get/ids")
    @Override
    public List<User>listByIds(@RequestParam("idList")Collection<Long> idList){
        return userService.listByIds(idList);
    }
}
