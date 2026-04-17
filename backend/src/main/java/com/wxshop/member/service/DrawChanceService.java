package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.DrawChance;
import com.wxshop.member.entity.DrawChanceLog;
import com.wxshop.member.entity.DrawChanceRule;
import com.wxshop.member.mapper.DrawChanceLogMapper;
import com.wxshop.member.mapper.DrawChanceMapper;
import com.wxshop.member.mapper.DrawChanceRuleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DrawChanceService {

    @Resource
    private DrawChanceMapper drawChanceMapper;

    @Resource
    private DrawChanceLogMapper drawChanceLogMapper;

    @Resource
    private DrawChanceRuleMapper drawChanceRuleMapper;

    public DrawChance getOrCreateChance(Long userId) {
        LambdaQueryWrapper<DrawChance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DrawChance::getUserId, userId);
        DrawChance chance = drawChanceMapper.selectOne(wrapper);
        if (chance == null) {
            chance = new DrawChance();
            chance.setUserId(userId);
            chance.setRemainingCount(0);
            chance.setTotalCount(0);
            chance.setUsedCount(0);
            chance.setLastUpdateTime(LocalDateTime.now());
            drawChanceMapper.insert(chance);
        }
        return chance;
    }

    public DrawChance getUserChance(Long userId) {
        return getOrCreateChance(userId);
    }

    public int getRemainingCount(Long userId) {
        DrawChance chance = getOrCreateChance(userId);
        return chance.getRemainingCount();
    }

    @Transactional
    public void giveChance(Long userId, String sourceType, Long relatedId, int count, String remark) {
        if (count <= 0) {
            return;
        }

        DrawChance chance = getOrCreateChance(userId);
        int beforeCount = chance.getRemainingCount();
        int afterCount = beforeCount + count;

        chance.setRemainingCount(afterCount);
        chance.setTotalCount(chance.getTotalCount() + count);
        chance.setLastUpdateTime(LocalDateTime.now());
        drawChanceMapper.updateById(chance);

        DrawChanceLog log = new DrawChanceLog();
        log.setUserId(userId);
        log.setType(1);
        log.setSourceType(sourceType);
        log.setRelatedId(relatedId);
        log.setCount(count);
        log.setBeforeCount(beforeCount);
        log.setAfterCount(afterCount);
        log.setRemark(remark);
        drawChanceLogMapper.insert(log);
    }

    @Transactional
    public boolean consumeChance(Long userId) {
        DrawChance chance = getOrCreateChance(userId);
        if (chance.getRemainingCount() <= 0) {
            return false;
        }

        int beforeCount = chance.getRemainingCount();
        int afterCount = beforeCount - 1;

        chance.setRemainingCount(afterCount);
        chance.setUsedCount(chance.getUsedCount() + 1);
        chance.setLastUpdateTime(LocalDateTime.now());
        drawChanceMapper.updateById(chance);

        DrawChanceLog log = new DrawChanceLog();
        log.setUserId(userId);
        log.setType(2);
        log.setSourceType("draw");
        log.setCount(1);
        log.setBeforeCount(beforeCount);
        log.setAfterCount(afterCount);
        log.setRemark("抽奖消耗");
        drawChanceLogMapper.insert(log);

        return true;
    }

    public DrawChanceRule getRuleByType(String chanceType) {
        LambdaQueryWrapper<DrawChanceRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DrawChanceRule::getChanceType, chanceType);
        wrapper.eq(DrawChanceRule::getStatus, 1);
        return drawChanceRuleMapper.selectOne(wrapper);
    }

    public List<DrawChanceRule> getAllRules() {
        return drawChanceRuleMapper.selectList(null);
    }

    @Transactional
    public void updateRule(DrawChanceRule rule) {
        drawChanceRuleMapper.updateById(rule);
    }

    public int getTodayGivenCount(Long userId, String sourceType) {
        LocalDate today = LocalDate.now();
        LambdaQueryWrapper<DrawChanceLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DrawChanceLog::getUserId, userId);
        wrapper.eq(DrawChanceLog::getSourceType, sourceType);
        wrapper.eq(DrawChanceLog::getType, 1);
        wrapper.apply("DATE(create_time) = {0}", today.toString());
        List<DrawChanceLog> logs = drawChanceLogMapper.selectList(wrapper);
        return logs.stream().mapToInt(DrawChanceLog::getCount).sum();
    }

    @Transactional
    public void handleConsumeComplete(Long userId, BigDecimal orderAmount) {
        DrawChanceRule rule = getRuleByType("consume");
        if (rule == null || rule.getStatus() != 1) {
            return;
        }

        if (rule.getMinAmount() != null && orderAmount.compareTo(rule.getMinAmount()) < 0) {
            return;
        }

        if (rule.getDailyLimit() != null) {
            int todayGiven = getTodayGivenCount(userId, "consume");
            if (todayGiven >= rule.getDailyLimit()) {
                return;
            }
        }

        giveChance(userId, "consume", null, rule.getGiveCount(),
                "消费满" + rule.getMinAmount() + "元赠送");
    }

    @Transactional
    public void handleCheckIn(Long userId) {
        DrawChanceRule rule = getRuleByType("check_in");
        if (rule == null || rule.getStatus() != 1) {
            return;
        }

        giveChance(userId, "check_in", null, rule.getGiveCount(), "每日签到赠送");
    }

    @Transactional
    public void handleNewUser(Long userId) {
        DrawChanceRule rule = getRuleByType("new_user");
        if (rule == null || rule.getStatus() != 1) {
            return;
        }

        giveChance(userId, "new_user", null, rule.getGiveCount(), "新用户注册赠送");
    }
}
