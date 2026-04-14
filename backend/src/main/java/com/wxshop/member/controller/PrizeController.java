package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.Prize;
import com.wxshop.member.service.PrizeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/prizes")
public class PrizeController {

    @Resource
    private PrizeService prizeService;

    @GetMapping
    public Result<List<Prize>> getPrizes() {
        try {
            List<Prize> prizes = prizeService.getActivePrizes();
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
}
