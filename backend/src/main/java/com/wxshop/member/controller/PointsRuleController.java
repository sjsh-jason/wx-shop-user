package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.PointsRule;
import com.wxshop.member.service.PointsRuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointsRuleController {

    @Resource
    private PointsRuleService pointsRuleService;

    @GetMapping("/rules")
    public Result<List<PointsRule>> getRules() {
        try {
            List<PointsRule> rules = pointsRuleService.getAllRules();
            return Result.success(rules);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
