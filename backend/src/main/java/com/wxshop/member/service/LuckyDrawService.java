package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxshop.member.entity.*;
import com.wxshop.member.mapper.LuckyDrawMapper;
import com.wxshop.member.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private UserMapper userMapper;

    @Resource
    private PointsLogService pointsLogService;

    @Resource
    private DrawChanceService drawChanceService;

    @Resource
    private DrawRiskConfigService drawRiskConfigService;

    private final Random random = new Random();

    public IPage<LuckyDraw> getUserDraws(Long userId, int page, int pageSize) {
        LambdaQueryWrapper<LuckyDraw> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LuckyDraw::getUserId, userId)
                .orderByDesc(LuckyDraw::getCreateTime);
        return luckyDrawMapper.selectPage(new Page<>(page, pageSize), wrapper);
    }

    @Transactional
    public LuckyDraw doDraw(Long userId) {
        if (drawRiskConfigService.isAbnormalUser(userId)) {
        throw new RuntimeException("检测到异常操作，暂时无法参与抽奖");
    }

        int remainingCount = drawChanceService.getRemainingCount(userId);
        if (remainingCount <= 0) {
            throw new RuntimeException("抽奖次数不足");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        List<Prize> prizes = prizeService.getActivePrizes();
        if (prizes.isEmpty()) {
            throw new RuntimeException("暂无奖品");
        }

        Prize prize = drawPrize(prizes, userId);
        int prizeIndex = prizes.indexOf(prize);

        if (!drawChanceService.consumeChance(userId)) {
            throw new RuntimeException("抽奖次数消耗失败");
        }

        LuckyDraw draw = new LuckyDraw();
        draw.setUserId(userId);
        draw.setPrizeId(prize.getId());
        draw.setPrizeName(prize.getName());
        draw.setPrizeType(prize.getType());
        draw.setPoints(prize.getPoints() != null ? prize.getPoints() : 0);
        draw.setStatus(0);

        if (prize.getValidDays() != null && prize.getValidDays() > 0) {
            draw.setExpireTime(LocalDateTime.now().plusDays(prize.getValidDays()));
        }
        draw.setVerifyStatus(0);

        luckyDrawMapper.insert(draw);

        if (prize.getType() == 1 && prize.getPoints() != null && prize.getPoints() > 0) {
            user.setPoints(user.getPoints() + prize.getPoints());
            user.setTotalPoints(user.getTotalPoints() + prize.getPoints());
            userMapper.updateById(user);

            pointsLogService.addLog(userId, 4, prize.getPoints(), user.getPoints(),
                    "lucky_draw", draw.getId(), "幸运抽奖获得积分");

            draw.setStatus(1);
            luckyDrawMapper.updateById(draw);
        }

        if (prize.getType() == 2 || prize.getType() == 3) {
            draw.setStatus(1);
            draw.setVerifyStatus(0);
            luckyDrawMapper.updateById(draw);
        }

        if (prize.getType() == 0) {
            draw.setStatus(1);
            luckyDrawMapper.updateById(draw);
        }

        if (prizeIndex < 3) {
            drawRiskConfigService.recordTopPrizeWin(userId, prize.getId(), prizeIndex + 1);
        }

        return draw;
    }

    private Prize drawPrize(List<Prize> prizes, Long userId) {
        List<Prize> availablePrizes = new java.util.ArrayList<>();
        for (int i = 0; i < prizes.size(); i++) {
            Prize prize = prizes.get(i);
            if (i < 3 && drawRiskConfigService.hasWonTopPrize(userId, prize.getId(), i + 1)) {
                continue;
            }
            availablePrizes.add(prize);
        }

        if (availablePrizes.isEmpty()) {
            availablePrizes = prizes;
        }

        BigDecimal totalProbability = availablePrizes.stream()
                .map(Prize::getProbability)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double randomValue = random.nextDouble() * totalProbability.doubleValue();
        double current = 0;

        for (Prize prize : availablePrizes) {
            current += prize.getProbability().doubleValue();
            if (randomValue <= current) {
                return prize;
            }
        }

        return availablePrizes.get(availablePrizes.size() - 1);
    }

    public List<User> getCustomerList() {
        return userService.getAllUsers();
    }

    public LuckyDraw getDrawById(Long id) {
        return luckyDrawMapper.selectById(id);
    }

    @Transactional
    public void verifyDraw(Long id) {
        LuckyDraw draw = luckyDrawMapper.selectById(id);
        if (draw == null) {
            throw new RuntimeException("抽奖记录不存在");
        }
        if (draw.getVerifyStatus() != 0) {
            throw new RuntimeException("该奖品已核销或已过期");
        }
        draw.setVerifyStatus(1);
        draw.setVerifyTime(LocalDateTime.now());
        luckyDrawMapper.updateById(draw);
    }
}
