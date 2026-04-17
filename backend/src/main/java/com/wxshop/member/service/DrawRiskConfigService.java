package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.DrawPrizeLimit;
import com.wxshop.member.entity.DrawRiskConfig;
import com.wxshop.member.mapper.DrawPrizeLimitMapper;
import com.wxshop.member.mapper.DrawRiskConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DrawRiskConfigService {

    @Resource
    private DrawRiskConfigMapper drawRiskConfigMapper;

    @Resource
    private DrawPrizeLimitMapper drawPrizeLimitMapper;

    public List<DrawRiskConfig> getAllConfigs() {
        return drawRiskConfigMapper.selectList(null);
    }

    public DrawRiskConfig getConfigByKey(String configKey) {
        LambdaQueryWrapper<DrawRiskConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DrawRiskConfig::getConfigKey, configKey);
        return drawRiskConfigMapper.selectOne(wrapper);
    }

    public boolean isTopPrizeLimitEnabled() {
        DrawRiskConfig config = getConfigByKey("top_prize_limit");
        return config != null && "true".equals(config.getConfigValue());
    }

    public boolean isAbnormalInterceptEnabled() {
        DrawRiskConfig config = getConfigByKey("abnormal_intercept");
        return config != null && "true".equals(config.getConfigValue());
    }

    public String getCheatPunishment() {
        DrawRiskConfig config = getConfigByKey("cheat_punishment");
        return config != null ? config.getConfigValue() : "cancel";
    }

    public void updateConfig(DrawRiskConfig config) {
        drawRiskConfigMapper.updateById(config);
    }

    public boolean hasWonTopPrize(Long userId, Long prizeId, int prizeLevel) {
        if (!isTopPrizeLimitEnabled()) {
            return false;
        }
        if (prizeLevel > 3) {
            return false;
        }
        LambdaQueryWrapper<DrawPrizeLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DrawPrizeLimit::getUserId, userId);
        wrapper.eq(DrawPrizeLimit::getPrizeLevel, prizeLevel);
        return drawPrizeLimitMapper.selectCount(wrapper) > 0;
    }

    public void recordTopPrizeWin(Long userId, Long prizeId, int prizeLevel) {
        LambdaQueryWrapper<DrawPrizeLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DrawPrizeLimit::getUserId, userId);
        wrapper.eq(DrawPrizeLimit::getPrizeId, prizeId);
        DrawPrizeLimit limit = drawPrizeLimitMapper.selectOne(wrapper);
        if (limit == null) {
            limit = new DrawPrizeLimit();
            limit.setUserId(userId);
            limit.setPrizeId(prizeId);
            limit.setPrizeLevel(prizeLevel);
            limit.setWinCount(1);
            drawPrizeLimitMapper.insert(limit);
        } else {
            limit.setWinCount(limit.getWinCount() + 1);
            drawPrizeLimitMapper.updateById(limit);
        }
    }

    public boolean isAbnormalUser(Long userId) {
        if (!isAbnormalInterceptEnabled()) {
            return false;
        }
        return false;
    }
}
