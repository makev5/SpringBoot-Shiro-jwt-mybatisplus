package com.xiaoke.demo.controller;

import com.xiaoke.demo.beans.User;
import com.xiaoke.demo.exceptions.MyException;
import com.xiaoke.demo.jwt.JWTUtil;
import com.xiaoke.demo.result.Result;
import com.xiaoke.demo.result.ResultUtils;
import com.xiaoke.demo.service.impl.UserServiceImpl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Ma Ke
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserServiceImpl userServiceImpl;

    @Autowired
    public void setService(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                              @RequestParam("password") String password) throws Exception {
        User user = userServiceImpl.getUser(username);
                            
        if(!userServiceImpl.isExistUsername(username)) {
            throw new MyException("用户名不存在");
        } else if(!user.getPassword().equals(password)) {
            throw new MyException("密码不正确");
        } else{
            return ResultUtils.success(200,"登录成功",JWTUtil.sign(username, password));
        }

    }

    /**
     * 描述: 游客和登录用户访问不同资源
     *
     * @author Ma ke 2020/4/21 13:15
     * @param
     * @return com.xiaoke.demo.result.ResultUtils
     */
    @GetMapping("/article")
    public Result article() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return ResultUtils.success(200, "你已经登录", null);
        } else {
            return ResultUtils.success(200, "你是游客", null);
        }
    }

    /**
     * 描述: @RequiresAuthentication 需要认证登陆后才能访问
     *
     * @author Ma ke 2020/4/21 12:58
     * @param
     * @return com.xiaoke.demo.result.ResultUtils
     */
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public Result requireAuth() {
        return ResultUtils.success(200, "你被认证了", null);
    }

    /**
     * 描述: @RequiresRoles("admin") 需要admin的角色信息才能访问
     *
     * @author Ma ke 2020/4/21 12:59
     * @param
     * @return com.xiaoke.demo.result.ResultUtils
     */
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public Result requireRole() {
        return ResultUtils.success(200, "你正在访问需要admin角色", null);
    }

    /**
     * 描述: @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"}) 需要同时有view和edit权限才能访问
     *
     * @author Ma ke 2020/4/21 12:59
     * @param
     * @return com.xiaoke.demo.result.ResultUtils
     */
    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public Result requirePermission() {
        return ResultUtils.success(200, "你正在访问需要edit和view的权限", null);
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result unauthorized() {
        return ResultUtils.success(401, "未授权", null);
    }
}

