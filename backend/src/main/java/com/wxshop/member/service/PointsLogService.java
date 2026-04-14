package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.PointsLog;
import com.wxshop.member.mapper.PointsLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PointsLogService {

    @Resource
    private PointsLogMapper pointsLogMapper;

    public void addLog(Long userId, Integer type, Integer points, Integer balance,
                       String relatedType, Long relatedId, String remark) {
        PointsLog log = new PointsLog();
        log.setUserId(userId);
        log.setType(type);
        log.setPoints(points);
        log.setBalance(balance);
        log.setRelatedType(relatedType);
        log.setRelatedId(relatedId);
        log.setRemark(remark);
        pointsLogMapper.insert(log);
    }

    public List<PointsLog> getUserPointsLogs(Long userId) {
        LambdaQueryWrapper<PointsLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointsLog::getUserId, userId)
                .orderByDesc(PointsLog::getCreateTime);
        return pointsLogMapper.selectList(wrapper);
    }
}
