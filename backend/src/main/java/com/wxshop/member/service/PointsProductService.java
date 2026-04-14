package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.PointsProduct;
import com.wxshop.member.mapper.PointsProductMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PointsProductService {

    @Resource
    private PointsProductMapper pointsProductMapper;

    public List<PointsProduct> getActiveProducts() {
        LambdaQueryWrapper<PointsProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointsProduct::getStatus, 1)
                .gt(PointsProduct::getStock, 0)
                .orderByDesc(PointsProduct::getCreateTime);
        return pointsProductMapper.selectList(wrapper);
    }

    public List<PointsProduct> getActiveProductsByCategory(String category) {
        LambdaQueryWrapper<PointsProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PointsProduct::getStatus, 1)
                .eq(PointsProduct::getCategory, category)
                .gt(PointsProduct::getStock, 0)
                .orderByDesc(PointsProduct::getCreateTime);
        return pointsProductMapper.selectList(wrapper);
    }

    public PointsProduct getProductById(Long id) {
        return pointsProductMapper.selectById(id);
    }

    public boolean decreaseStock(Long productId) {
        PointsProduct product = pointsProductMapper.selectById(productId);
        if (product == null || product.getStock() <= 0) {
            return false;
        }
        product.setStock(product.getStock() - 1);
        product.setExchangedCount(product.getExchangedCount() + 1);
        pointsProductMapper.updateById(product);
        return true;
    }
}
