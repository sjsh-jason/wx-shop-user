package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.entity.ExchangeRecord;
import com.wxshop.member.service.ExchangeRecordService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeRecordController {

    @Resource
    private ExchangeRecordService exchangeRecordService;

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping
    public Result<ExchangeRecord> createExchange(@RequestBody Map<String, Long> params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            Long productId = params.get("productId");
            if (productId == null) {
                return Result.error("商品ID不能为空");
            }
            ExchangeRecord record = exchangeRecordService.createExchange(userId, productId);
            return Result.success(record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<List<ExchangeRecord>> getMyExchanges(@RequestParam(required = false) Integer status, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<ExchangeRecord> list;
            if (status != null) {
                list = exchangeRecordService.getUserExchangesByStatus(userId, status);
            } else {
                list = exchangeRecordService.getUserExchanges(userId);
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<ExchangeRecord> getExchangeDetail(@PathVariable Long id) {
        try {
            ExchangeRecord record = exchangeRecordService.getExchangeById(id);
            if (record == null) {
                return Result.error("兑换记录不存在");
            }
            return Result.success(record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/verifyByQrCode")
    public Result<Void> verifyByQrCode(@RequestBody Map<String, String> params) {
        try {
            String qrCode = params.get("qrCode");
            if (qrCode == null || qrCode.isEmpty()) {
                return Result.error("核销码不能为空");
            }
            exchangeRecordService.verifyExchange(qrCode);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
