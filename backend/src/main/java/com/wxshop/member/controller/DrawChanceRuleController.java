package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.DrawChanceRule;
import com.wxshop.member.service.DrawChanceService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/draw/chance-rules")
public class DrawChanceRuleController {

    @Resource
    private DrawChanceService drawChanceService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping
    public Result<List<DrawChanceRule>> getRules() {
        try {
            List<DrawChanceRule> rules = drawChanceService.getAllRules();
            return Result.success(rules);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping
    public Result<Void> updateRules(@RequestBody List<DrawChanceRule> rules, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("未登录");
            }

            for (DrawChanceRule rule : rules) {
                drawChanceService.updateRule(rule);
            }
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
