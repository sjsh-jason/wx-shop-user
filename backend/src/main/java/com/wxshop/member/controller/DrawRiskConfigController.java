package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.DrawRiskConfig;
import com.wxshop.member.service.DrawRiskConfigService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/draw/risk-config")
public class DrawRiskConfigController {

    @Resource
    private DrawRiskConfigService drawRiskConfigService;

    @GetMapping
    public Result<List<DrawRiskConfig>> getConfigs() {
        try {
            List<DrawRiskConfig> configs = drawRiskConfigService.getAllConfigs();
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping
    public Result<Void> updateConfigs(@RequestBody List<DrawRiskConfig> configs, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = JwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("未登录");
            }

            for (DrawRiskConfig config : configs) {
                drawRiskConfigService.updateConfig(config);
            }
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
