package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.PromotionProduct;
import com.wxshop.member.mapper.PromotionProductMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionProductService {

    @Resource
    private PromotionProductMapper productMapper;

    public List<PromotionProduct> getActiveProducts() {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<PromotionProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PromotionProduct::getStatus, 1)
                .le(PromotionProduct::getStartTime, now)
                .ge(PromotionProduct::getEndTime, now)
                .gt(PromotionProduct::getStock, 0)
                .orderByDesc(PromotionProduct::getCreateTime);
        return productMapper.selectList(wrapper);
    }

    public PromotionProduct getProductById(Long id) {
        return productMapper.selectById(id);
    }

    public boolean decreaseStock(Long productId) {
        PromotionProduct product = productMapper.selectById(productId);
        if (product == null || product.getStock() <= 0) {
            return false;
        }
        product.setStock(product.getStock() - 1);
        product.setSoldCount(product.getSoldCount() + 1);
        productMapper.updateById(product);
        return true;
    }
}
