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
    private PromotionProductService promotionProductService;

    @GetMapping("/products")
    public Result<List<PromotionProduct>> getProducts() {
        try {
            List<PromotionProduct> products = promotionProductService.getActiveProducts();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/products/all")
    public Result<List<PromotionProduct>> getAllProducts() {
        try {
            List<PromotionProduct> products = promotionProductService.getAllProducts();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/products/{id}")
    public Result<PromotionProduct> getProductDetail(@PathVariable Long id) {
        try {
            PromotionProduct product = promotionProductService.getProductById(id);
            if (product == null) {
                return Result.error("商品不存在");
            }
            return Result.success(product);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/products")
    public Result<PromotionProduct> createProduct(@RequestBody PromotionProduct product) {
        try {
            PromotionProduct created = promotionProductService.createProduct(product);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/products/{id}")
    public Result<PromotionProduct> updateProduct(@PathVariable Long id, @RequestBody PromotionProduct product) {
        try {
            product.setId(id);
            PromotionProduct updated = promotionProductService.updateProduct(product);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/products/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        try {
            boolean deleted = promotionProductService.deleteProduct(id);
            if (deleted) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/products/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody PromotionProduct product) {
        try {
            boolean updated = promotionProductService.updateStatus(id, product.getStatus());
            if (updated) {
                return Result.success();
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
