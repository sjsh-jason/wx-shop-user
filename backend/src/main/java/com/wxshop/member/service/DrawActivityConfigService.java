package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.DrawActivityConfig;
import com.wxshop.member.mapper.DrawActivityConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DrawActivityConfigService {

    @Resource
    private DrawActivityConfigMapper drawActivityConfigMapper;

    public DrawActivityConfig getActiveConfig() {
        LambdaQueryWrapper<DrawActivityConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DrawActivityConfig::getStatus, 1);
        wrapper.last("LIMIT 1");
        DrawActivityConfig config = drawActivityConfigMapper.selectOne(wrapper);
        if (config == null) {
            config = createDefaultConfig();
        }
        return config;
    }

    private DrawActivityConfig createDefaultConfig() {
        DrawActivityConfig config = new DrawActivityConfig();
        config.setTitle("幸运大抽奖");
        config.setStatus(1);
        drawActivityConfigMapper.insert(config);
        return config;
    }

    public DrawActivityConfig getConfigById(Long id) {
        return drawActivityConfigMapper.selectById(id);
    }

    public void updateConfig(DrawActivityConfig config) {
        drawActivityConfigMapper.updateById(config);
    }
}
