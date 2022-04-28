package com.wh.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wh.demo.constant.SystemConstant;
import com.wh.demo.entity.Admin;
import com.wh.demo.service.AdminService;
import com.wh.demo.utils.JwtUtils;
import com.wh.demo.utils.R;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/login")
    //@RequestBody:接受前端传递的数据，转换成实体
    public R login(@RequestBody  Admin admin){
        System.out.println(admin);
        try {
            //验证用户名和密码
            QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("userName",admin.getUsername());
            queryWrapper.eq("password",admin.getPassword());
            Admin one = adminService.getOne(queryWrapper);
            if (one == null){
                return R.error("用户登录失败！");
            }
            //return R.ok("登录成功！");
            //步骤：1、生成token 2、把token保存到localStorage中 3、下次请求携带token
            String token = JwtUtils.createJWT("1001", admin.getUsername(), SystemConstant.JWT_TTL);
            return R.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("服务端异常！！");
        }
    }

    @RequestMapping("/refreshToken")
    public R refreshToken(HttpServletRequest request){
        try {
            String token = request.getHeader("token");
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            String jwt = JwtUtils.createJWT(claims.getId(), claims.getSubject(), SystemConstant.JWT_TTL);
            return R.ok(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("token刷新异常");
        }
    }
}















