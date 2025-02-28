package com.nextone.web.controller.system;

import com.nextone.pojo.AdminUser;
import com.nextone.service.AdminUserService;
import com.nextone.utils.ImageCodeUtils;
import com.nextone.utils.JsonResult;
import com.nextone.web.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 徐塬峰
 * @email 986771570@qq.com
 * @date 2019-06-08
 * @description
 **/
@RequestMapping("/adminUser")
@Controller
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @SysLog
    @RequestMapping("/list")
    @ApiOperation(value = "跳转用户管理页面", notes = "跳转用户管理页面")
    public ModelAndView list() {
        return new ModelAndView("/system/admin/list");
    }

    @SysLog
    @ResponseBody
    @ApiOperation(value = "查询所有用户的信息", notes = "查询所有用户的信息")
    @RequestMapping("/queryAll")
    public JsonResult queryAll(Integer page, Integer pageSize, String keyword) {
        return JsonResult.ok(adminUserService.queryAll(page, pageSize, null));
    }

    @SysLog
    @ResponseBody
    @ApiOperation(value = "查询单个用户的详细信息", notes = "根据id来查询用户信息")
    @GetMapping("/selectOne/{id}")
    public JsonResult selectOne(@PathVariable("id") Long id) {
        return JsonResult.ok(adminUserService.selectOne(id));
    }


    @SysLog
    @ResponseBody
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping("/changePassword")
    public ModelAndView changePassword() {
        return new ModelAndView("/system/admin/changePassword");
    }

    @SysLog
    @ApiOperation(value = "个人资料展示")
    @RequestMapping("/profile")
    public ModelAndView profile() {
        return new ModelAndView("/system/admin/profile");
    }

    @SysLog
    @ResponseBody
    @PostMapping("/loginSubmit")
    public JsonResult loginSubmit(HttpServletRequest request, String username, String password, String verifyCode) {

        if (!ImageCodeUtils.checkImageCode(request.getSession(), verifyCode)) {
            return JsonResult.errorMsg("验证码错误");
        }
        AdminUser adminUser = adminUserService.login(username, password);
        if (adminUser == null) {
            return JsonResult.errorMsg("账号密码错误");
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(adminUser.getUsername(), adminUser.getPassword());
        try {
            subject.login(token);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.errorMsg("账号密码错误!");
        }
        Session session = subject.getSession(true);
        session.setAttribute("adminUser", adminUser);
        return JsonResult.ok();
    }
}