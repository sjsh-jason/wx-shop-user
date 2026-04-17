package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.ExchangeRecord;
import com.wxshop.member.entity.PointsProduct;
import com.wxshop.member.entity.User;
import com.wxshop.member.mapper.ExchangeRecordMapper;
import com.wxshop.member.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ExchangeRecordService {

    @Resource
    private ExchangeRecordMapper exchangeRecordMapper;

    @Resource
    private PointsProductService pointsProductService;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PointsLogService pointsLogService;

    @Transactional
    public ExchangeRecord createExchange(Long userId, Long productId) {
        PointsProduct product = pointsProductService.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        if (product.getStock() <= 0) {
            throw new RuntimeException("库存不足");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getPoints() < product.getPoints()) {
            throw new RuntimeException("积分不足");
        }

        // 扣减库存
        boolean success = pointsProductService.decreaseStock(productId);
        if (!success) {
            throw new RuntimeException("库存不足");
        }

        // 扣减用户积分
        user.setPoints(user.getPoints() - product.getPoints());
        userMapper.updateById(user);

        // 创建兑换记录
        ExchangeRecord record = new ExchangeRecord();
        record.setUserId(userId);
        record.setProductId(productId);
        record.setProductName(product.getName());
        record.setProductImage(product.getImage());
        record.setPoints(product.getPoints());
        record.setQrCode(generateQrCode());
        record.setStatus(0);
        exchangeRecordMapper.insert(record);

        // 记录积分明细
        pointsLogService.addLog(userId, 5, -product.getPoints(), user.getPoints(),
                "exchange", record.getId(), "积分兑换商品: " + product.getName());

        return record;
    }

    private String generateQrCode() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public List<ExchangeRecord> getUserExchanges(Long userId) {
        LambdaQueryWrapper<ExchangeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExchangeRecord::getUserId, userId)
                .orderByDesc(ExchangeRecord::getCreateTime);
        return exchangeRecordMapper.selectList(wrapper);
    }

    public List<ExchangeRecord> getUserExchangesByStatus(Long userId, Integer status) {
        LambdaQueryWrapper<ExchangeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExchangeRecord::getUserId, userId)
                .eq(ExchangeRecord::getStatus, status)
                .orderByDesc(ExchangeRecord::getCreateTime);
        return exchangeRecordMapper.selectList(wrapper);
    }

    public ExchangeRecord getExchangeById(Long id) {
        return exchangeRecordMapper.selectById(id);
    }

    public ExchangeRecord getExchangeByQrCode(String qrCode) {
        LambdaQueryWrapper<ExchangeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExchangeRecord::getQrCode, qrCode);
        return exchangeRecordMapper.selectOne(wrapper);
    }

    @Transactional
    public void verifyExchange(String qrCode) {
        ExchangeRecord record = getExchangeByQrCode(qrCode);
        if (record == null) {
            throw new RuntimeException("兑换记录不存在");
        }
        if (record.getStatus() != 0) {
            throw new RuntimeException("该兑换已领取或已取消");
        }

        record.setStatus(1);
        record.setVerifyTime(LocalDateTime.now());
        exchangeRecordMapper.updateById(record);
    }
}
