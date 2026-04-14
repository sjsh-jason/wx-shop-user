package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.dto.LoginResponse;
import com.wxshop.member.dto.WechatLoginRequest;
import com.wxshop.member.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("/wechat/login")
    public Result<LoginResponse> wechatLogin(@Valid @RequestBody WechatLoginRequest request) {
        try {
            LoginResponse response = userService.wechatLogin(request.getCode());
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
