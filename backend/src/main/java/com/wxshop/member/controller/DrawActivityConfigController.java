package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.DrawActivityConfig;
import com.wxshop.member.service.DrawActivityConfigService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/draw/activity")
public class DrawActivityConfigController {

    @Resource
    private DrawActivityConfigService drawActivityConfigService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/config")
    public Result<DrawActivityConfig> getConfig() {
        try {
            DrawActivityConfig config = drawActivityConfigService.getActiveConfig();
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/config")
    public Result<Void> updateConfig(@RequestBody DrawActivityConfig config, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("未登录");
            }

            drawActivityConfigService.updateConfig(config);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
