package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.Product;
import com.wxshop.member.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductService {

    @Resource
    private ProductMapper productMapper;

    public List<Product> getAllProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Product::getCreateTime);
        return productMapper.selectList(wrapper);
    }

    public List<Product> getProductsByType(String type) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getType, type)
                .eq(Product::getStatus, 1)
                .orderByDesc(Product::getCreateTime);
        return productMapper.selectList(wrapper);
    }

    public List<Product> getActiveProducts() {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1)
                .gt(Product::getStock, 0)
                .orderByDesc(Product::getCreateTime);
        return productMapper.selectList(wrapper);
    }

    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }

    public Product createProduct(Product product) {
        product.setStatus(1);
        productMapper.insert(product);
        return product;
    }

    public Product updateProduct(Product product) {
        productMapper.updateById(product);
        return product;
    }

    public boolean deleteProduct(Long id) {
        return productMapper.deleteById(id) > 0;
    }

    public boolean updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        return productMapper.updateById(product) > 0;
    }
}
