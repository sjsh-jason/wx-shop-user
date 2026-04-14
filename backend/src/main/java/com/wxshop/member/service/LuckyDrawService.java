package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.LuckyDraw;
import com.wxshop.member.entity.Prize;
import com.wxshop.member.entity.User;
import com.wxshop.member.mapper.LuckyDrawMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class LuckyDrawService {

    @Resource
    private LuckyDrawMapper luckyDrawMapper;

    @Resource
    private PrizeService prizeService;

    @Resource
    private UserService userService;

    @Resource
    private PointsLogService pointsLogService;

    private final Random random = new Random();

    public List<LuckyDraw> getUserDraws(Long userId) {
        LambdaQueryWrapper<LuckyDraw> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LuckyDraw::getUserId, userId)
                .orderByDesc(LuckyDraw::getCreateTime)
                .last("LIMIT 20");
        return luckyDrawMapper.selectList(wrapper);
    }

    public boolean hasDrawnToday(Long userId) {
        LocalDate today = LocalDate.now();
        LambdaQueryWrapper<LuckyDraw> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LuckyDraw::getUserId, userId)
                .apply("DATE(create_time) = {0}", today.toString());
        return luckyDrawMapper.selectCount(wrapper) > 0;
    }

    @Transactional
    public LuckyDraw doDraw(Long userId) {
        if (hasDrawnToday(userId)) {
            throw new RuntimeException("今日已抽奖");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        List<Prize> prizes = prizeService.getActivePrizes();
        if (prizes.isEmpty()) {
            throw new RuntimeException("暂无奖品");
        }

        Prize prize = drawPrize(prizes);

        LuckyDraw draw = new LuckyDraw();
        draw.setUserId(userId);
        draw.setPrizeId(prize.getId());
        draw.setPrizeName(prize.getName());
        draw.setPrizeType(prize.getType());
        draw.setPoints(prize.getPoints() != null ? prize.getPoints() : 0);
        draw.setStatus(0);
        luckyDrawMapper.insert(draw);

        if (prize.getType() == 1 && prize.getPoints() != null && prize.getPoints() > 0) {
            user.setPoints(user.getPoints() + prize.getPoints());
            user.setTotalPoints(user.getTotalPoints() + prize.getPoints());
            userService.updateUser(user.getId(), user.getNickname(), user.getAvatar());

            pointsLogService.addLog(userId, 4, prize.getPoints(), user.getPoints(),
                    "lucky_draw", draw.getId(), "幸运抽奖获得积分");

            draw.setStatus(1);
            luckyDrawMapper.updateById(draw);
        }

        return draw;
    }

    private Prize drawPrize(List<Prize> prizes) {
        BigDecimal totalProbability = prizes.stream()
                .map(Prize::getProbability)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double randomValue = random.nextDouble() * totalProbability.doubleValue();
        double current = 0;

        for (Prize prize : prizes) {
            current += prize.getProbability().doubleValue();
            if (randomValue <= current) {
                return prize;
            }
        }

        return prizes.get(prizes.size() - 1);
    }

    public List<User> getCustomerList() {
        return userService.getAllUsers();
    }
}
