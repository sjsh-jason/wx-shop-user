package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.CheckIn;
import com.wxshop.member.entity.PointsRule;
import com.wxshop.member.entity.User;
import com.wxshop.member.mapper.CheckInMapper;
import com.wxshop.member.mapper.PointsRuleMapper;
import com.wxshop.member.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CheckInService {

    @Resource
    private CheckInMapper checkInMapper;

    @Resource
    private PointsRuleMapper pointsRuleMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PointsLogService pointsLogService;

    public CheckIn getTodayCheckIn(Long userId) {
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getUserId, userId)
                .eq(CheckIn::getCheckDate, LocalDate.now());
        return checkInMapper.selectOne(wrapper);
    }

    public boolean hasCheckedInToday(Long userId) {
        return getTodayCheckIn(userId) != null;
    }

    public List<CheckIn> getRecentCheckIns(Long userId, int limit) {
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getUserId, userId)
                .orderByDesc(CheckIn::getCheckDate)
                .last("LIMIT " + limit);
        return checkInMapper.selectList(wrapper);
    }

    @Transactional
    public CheckIn doCheckIn(Long userId) {
        if (hasCheckedInToday(userId)) {
            throw new RuntimeException("今日已签到");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 计算连续签到天数
        int continuousDays = calculateContinuousDays(userId);

        // 获取签到规则
        int basePoints = 10;
        LambdaQueryWrapper<PointsRule> ruleWrapper = new LambdaQueryWrapper<>();
        ruleWrapper.eq(PointsRule::getType, "check_in");
        PointsRule rule = pointsRuleMapper.selectOne(ruleWrapper);
        if (rule != null && rule.getPoints() != null && rule.getPoints() > 0) {
            basePoints = rule.getPoints();
        }

        // 连续签到奖励（简单处理：每连续7天额外+5分）
        int bonus = 0;
        if (continuousDays > 0 && continuousDays % 7 == 0) {
            bonus = 5;
        }
        int totalPoints = basePoints + bonus;

        // 创建签到记录
        CheckIn checkIn = new CheckIn();
        checkIn.setUserId(userId);
        checkIn.setCheckDate(LocalDate.now());
        checkIn.setPoints(totalPoints);
        checkIn.setContinuousDays(continuousDays);
        checkInMapper.insert(checkIn);

        // 增加用户积分
        user.setPoints(user.getPoints() + totalPoints);
        user.setTotalPoints(user.getTotalPoints() + totalPoints);
        userMapper.updateById(user);

        // 记录积分明细
        String remark = "每日签到";
        if (bonus > 0) {
            remark = "每日签到（连续" + continuousDays + "天奖励" + bonus + "积分）";
        }
        pointsLogService.addLog(userId, 1, totalPoints, user.getPoints(),
                "check_in", checkIn.getId(), remark);

        return checkIn;
    }

    private int calculateContinuousDays(Long userId) {
        List<CheckIn> recentList = getRecentCheckIns(userId, 30);
        if (recentList.isEmpty()) {
            return 1;
        }

        CheckIn lastCheckIn = recentList.get(0);
        LocalDate yesterday = LocalDate.now().minusDays(1);

        if (lastCheckIn.getCheckDate().equals(yesterday)) {
            return lastCheckIn.getContinuousDays() + 1;
        }

        // 如果上次签到不是昨天，则从今天开始重新计算
        return 1;
    }
}
