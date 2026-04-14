package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.PointsLog;
import com.wxshop.member.service.PointsLogService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointsLogController {

    @Resource
    private PointsLogService pointsLogService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/logs")
    public Result<List<PointsLog>> getPointsLogs(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<PointsLog> logs = pointsLogService.getUserPointsLogs(userId);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
