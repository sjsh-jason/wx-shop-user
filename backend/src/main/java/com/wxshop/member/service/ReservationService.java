package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.dto.CreateReservationRequest;
import com.wxshop.member.entity.PromotionProduct;
import com.wxshop.member.entity.Reservation;
import com.wxshop.member.mapper.ReservationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    @Resource
    private ReservationMapper reservationMapper;

    @Resource
    private PromotionProductService productService;

    @Transactional
    public Reservation createReservation(Long userId, CreateReservationRequest request) {
        PromotionProduct product = productService.getProductById(request.getProductId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        if (product.getStock() <= 0) {
            throw new RuntimeException("库存不足");
        }

        boolean success = productService.decreaseStock(request.getProductId());
        if (!success) {
            throw new RuntimeException("库存不足");
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setProductId(product.getId());
        reservation.setProductName(product.getName());
        reservation.setProductImage(product.getImage());
        reservation.setOriginalPrice(product.getOriginalPrice());
        reservation.setPromotionPrice(product.getPromotionPrice());
        reservation.setQrCode(generateQrCode());
        reservation.setStatus(0);
        reservationMapper.insert(reservation);

        return reservation;
    }

    private String generateQrCode() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public List<Reservation> getUserReservations(Long userId) {
        LambdaQueryWrapper<Reservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reservation::getUserId, userId)
                .orderByDesc(Reservation::getCreateTime);
        return reservationMapper.selectList(wrapper);
    }

    public Reservation getReservationById(Long id) {
        return reservationMapper.selectById(id);
    }

    @Transactional
    public void verifyReservation(Long id) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation == null) {
            throw new RuntimeException("预约记录不存在");
        }
        if (reservation.getStatus() != 0) {
            throw new RuntimeException("该预约已核销或已取消");
        }

        reservation.setStatus(1);
        reservation.setVerifyTime(LocalDateTime.now());
        reservationMapper.updateById(reservation);
    }

    public Reservation getReservationByQrCode(String qrCode) {
        LambdaQueryWrapper<Reservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reservation::getQrCode, qrCode);
        return reservationMapper.selectOne(wrapper);
    }

    @Transactional
    public void verifyReservationByQrCode(String qrCode) {
        Reservation reservation = getReservationByQrCode(qrCode);
        if (reservation == null) {
            throw new RuntimeException("预约记录不存在");
        }
        if (reservation.getStatus() != 0) {
            throw new RuntimeException("该预约已核销或已取消");
        }

        reservation.setStatus(1);
        reservation.setVerifyTime(LocalDateTime.now());
        reservationMapper.updateById(reservation);
    }
}
