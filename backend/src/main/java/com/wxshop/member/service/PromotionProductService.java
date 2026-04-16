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
    private PromotionProductMapper promotionProductMapper;

    public List<PromotionProduct> getActiveProducts() {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<PromotionProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PromotionProduct::getStatus, 1)
                .le(PromotionProduct::getStartTime, now)
                .ge(PromotionProduct::getEndTime, now)
                .gt(PromotionProduct::getStock, 0)
                .orderByDesc(PromotionProduct::getCreateTime);
        return promotionProductMapper.selectList(wrapper);
    }

    public List<PromotionProduct> getAllProducts() {
        LambdaQueryWrapper<PromotionProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PromotionProduct::getCreateTime);
        return promotionProductMapper.selectList(wrapper);
    }

    public PromotionProduct getProductById(Long id) {
        return promotionProductMapper.selectById(id);
    }

    public PromotionProduct createProduct(PromotionProduct product) {
        product.setStatus(1);
        product.setSoldCount(0);
        promotionProductMapper.insert(product);
        return product;
    }

    public PromotionProduct updateProduct(PromotionProduct product) {
        promotionProductMapper.updateById(product);
        return product;
    }

    public boolean deleteProduct(Long id) {
        return promotionProductMapper.deleteById(id) > 0;
    }

    public boolean updateStatus(Long id, Integer status) {
        PromotionProduct product = new PromotionProduct();
        product.setId(id);
        product.setStatus(status);
        return promotionProductMapper.updateById(product) > 0;
    }

    public boolean decreaseStock(Long productId) {
        PromotionProduct product = promotionProductMapper.selectById(productId);
        if (product == null || product.getStock() <= 0) {
            return false;
        }
        product.setStock(product.getStock() - 1);
        product.setSoldCount(product.getSoldCount() + 1);
        promotionProductMapper.updateById(product);
        return true;
    }
}
