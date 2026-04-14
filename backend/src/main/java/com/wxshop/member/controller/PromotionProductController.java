package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.PromotionProduct;
import com.wxshop.member.service.PromotionProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/promotion")
public class PromotionProductController {

    @Resource
    private PromotionProductService productService;

    @GetMapping("/products")
    public Result<List<PromotionProduct>> getProducts() {
        try {
            List<PromotionProduct> products = productService.getActiveProducts();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/products/{id}")
    public Result<PromotionProduct> getProductDetail(@PathVariable Long id) {
        try {
            PromotionProduct product = productService.getProductById(id);
            if (product == null) {
                return Result.error("商品不存在");
            }
            return Result.success(product);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
