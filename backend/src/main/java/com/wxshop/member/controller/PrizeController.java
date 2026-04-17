package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.Prize;
import com.wxshop.member.service.PrizeService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/prizes")
public class PrizeController {

    @Resource
    private PrizeService prizeService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping
    public Result<List<Prize>> getPrizes() {
        try {
            List<Prize> prizes = prizeService.getActivePrizes();
            return Result.success(prizes);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/all")
    public Result<List<Prize>> getAllPrizes(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("未登录");
            }

            List<Prize> prizes = prizeService.getAllPrizes();
            return Result.success(prizes);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Prize> getPrizeDetail(@PathVariable Long id) {
        try {
            Prize prize = prizeService.getPrizeById(id);
            if (prize == null) {
                return Result.error("奖品不存在");
            }
            return Result.success(prize);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/batch")
    public Result<Void> updatePrizes(@RequestBody List<Prize> prizes, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("未登录");
            }

            for (Prize prize : prizes) {
                prizeService.updatePrize(prize);
            }
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
