package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.CheckIn;
import com.wxshop.member.service.CheckInService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkIn")
public class CheckInController {

    @Resource
    private CheckInService checkInService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/status")
    public Result<Map<String, Object>> getCheckInStatus(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            boolean hasCheckedIn = checkInService.hasCheckedInToday(userId);

            Map<String, Object> result = new HashMap<>();
            result.put("hasCheckedIn", hasCheckedIn);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping
    public Result<CheckIn> doCheckIn(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            CheckIn checkIn = checkInService.doCheckIn(userId);
            return Result.success(checkIn);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
