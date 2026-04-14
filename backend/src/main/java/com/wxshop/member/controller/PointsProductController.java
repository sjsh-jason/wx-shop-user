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
}
