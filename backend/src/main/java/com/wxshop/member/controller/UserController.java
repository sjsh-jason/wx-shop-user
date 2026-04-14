package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.dto.LoginResponse;
import com.wxshop.member.dto.UpdateUserRequest;
import com.wxshop.member.entity.User;
import com.wxshop.member.service.MerchantWhitelistService;
import com.wxshop.member.service.UserService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private MerchantWhitelistService merchantWhitelistService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/info")
    public Result<LoginResponse.UserInfo> getUserInfo(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getUserById(userId);

            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                    user.getId(),
                    user.getNickname(),
                    user.getAvatar(),
                    user.getPoints(),
                    user.getLevel()
            );
            return Result.success(userInfo);
        } catch (Exception e) {
            return Result.error("获取用户信息失败");
        }
    }

    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody UpdateUserRequest updateRequest, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            userService.updateUser(userId, updateRequest.getNickname(), updateRequest.getAvatar());
            return Result.success();
        } catch (Exception e) {
            return Result.error("更新用户信息失败");
        }
    }

    @GetMapping("/isMerchant")
    public Result<Map<String, Object>> isMerchant(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getUserById(userId);
            boolean isMerchant = merchantWhitelistService.isMerchantUser(user);

            Map<String, Object> result = new HashMap<>();
            result.put("isMerchant", isMerchant);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("检查商家身份失败");
        }
    }
}
