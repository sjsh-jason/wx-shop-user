package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.PointsProduct;
import com.wxshop.member.service.PointsProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointsProductController {

    @Resource
    private PointsProductService pointsProductService;

    @GetMapping("/products")
    public Result<List<PointsProduct>> getProducts(@RequestParam(required = false) String category) {
        try {
            List<PointsProduct> products;
            if (category != null && !category.isEmpty()) {
                products = pointsProductService.getActiveProductsByCategory(category);
            } else {
                products = pointsProductService.getActiveProducts();
            }
            return Result.success(products);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/products/all")
    public Result<List<PointsProduct>> getAllProducts() {
        try {
            List<PointsProduct> products = pointsProductService.getAllProducts();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/products/{id}")
    public Result<PointsProduct> getProductDetail(@PathVariable Long id) {
        try {
            PointsProduct product = pointsProductService.getProductById(id);
            if (product == null) {
                return Result.error("商品不存在");
            }
            return Result.success(product);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/products")
    public Result<PointsProduct> createProduct(@RequestBody PointsProduct product) {
        try {
            PointsProduct created = pointsProductService.createProduct(product);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/products/{id}")
    public Result<PointsProduct> updateProduct(@PathVariable Long id, @RequestBody PointsProduct product) {
        try {
            product.setId(id);
            PointsProduct updated = pointsProductService.updateProduct(product);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/products/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        try {
            boolean deleted = pointsProductService.deleteProduct(id);
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
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody PointsProduct product) {
        try {
            boolean updated = pointsProductService.updateStatus(id, product.getStatus());
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
