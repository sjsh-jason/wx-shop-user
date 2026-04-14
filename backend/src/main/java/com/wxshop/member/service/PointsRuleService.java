package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.PointsRule;
import com.wxshop.member.mapper.PointsRuleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PointsRuleService {

    @Resource
    private PointsRuleMapper pointsRuleMapper;

    public List<PointsRule> getAllRules() {
        return pointsRuleMapper.selectList(null);
    }

    public PointsRule getRuleByType(String type) {
        LambdaQueryWrapper<PointsRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointsRule::getType, type);
        return pointsRuleMapper.selectOne(wrapper);
    }
}
