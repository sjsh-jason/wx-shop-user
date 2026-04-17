package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.DrawChance;
import com.wxshop.member.service.DrawChanceService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/draw/chance")
public class DrawChanceController {

    @Resource
    private DrawChanceService drawChanceService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/status")
    public Result<Map<String, Object>> getChanceStatus(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("未登录");
            }

            DrawChance chance = drawChanceService.getUserChance(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("remainingCount", chance.getRemainingCount());
            result.put("totalCount", chance.getTotalCount());
            result.put("usedCount", chance.getUsedCount());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
