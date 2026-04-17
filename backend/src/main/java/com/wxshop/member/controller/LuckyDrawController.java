package com.wxshop.member.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wxshop.member.common.Result;
import com.wxshop.member.entity.LuckyDraw;
import com.wxshop.member.entity.Prize;
import com.wxshop.member.service.LuckyDrawService;
import com.wxshop.member.service.PrizeService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lucky-draw")
public class LuckyDrawController {

    @Resource
    private LuckyDrawService luckyDrawService;

    @Resource
    private PrizeService prizeService;

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/status")
    public Result<Map<String, Object>> getDrawStatus(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);

            List<Prize> prizes = prizeService.getActivePrizes();

            Map<String, Object> result = new HashMap<>();
            result.put("prizes", prizes);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping
    public Result<LuckyDraw> doDraw(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            LuckyDraw draw = luckyDrawService.doDraw(userId);
            return Result.success(draw);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<IPage<LuckyDraw>> getMyDraws(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            IPage<LuckyDraw> draws = luckyDrawService.getUserDraws(userId, page, pageSize);
            return Result.success(draws);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/verify/{id}")
    public Result<Void> verifyDraw(@PathVariable Long id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("未登录");
            }

            luckyDrawService.verifyDraw(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
