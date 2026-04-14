package com.wxshop.member.controller;

import com.wxshop.member.common.Result;
import com.wxshop.member.dto.CreateReservationRequest;
import com.wxshop.member.entity.Reservation;
import com.wxshop.member.service.ReservationService;
import com.wxshop.member.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Resource
    private ReservationService reservationService;

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping
    public Result<Reservation> createReservation(@Valid @RequestBody CreateReservationRequest request, HttpServletRequest httpRequest) {
        try {
            String token = httpRequest.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            Reservation reservation = reservationService.createReservation(userId, request);
            return Result.success(reservation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<List<Reservation>> getMyReservations(HttpServletRequest httpRequest) {
        try {
            String token = httpRequest.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<Reservation> reservations = reservationService.getUserReservations(userId);
            return Result.success(reservations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Reservation> getReservationDetail(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.getReservationById(id);
            if (reservation == null) {
                return Result.error("预约记录不存在");
            }
            return Result.success(reservation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/verify")
    public Result<Void> verifyReservation(@PathVariable Long id) {
        try {
            reservationService.verifyReservation(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/verifyByQrCode")
    public Result<Void> verifyByQrCode(@RequestBody java.util.Map<String, String> params) {
        try {
            String qrCode = params.get("qrCode");
            if (qrCode == null || qrCode.isEmpty()) {
                return Result.error("核销码不能为空");
            }
            reservationService.verifyReservationByQrCode(qrCode);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
