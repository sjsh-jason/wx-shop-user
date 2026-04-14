package com.wxshop.member.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateReservationRequest {

    @NotNull(message = "商品ID不能为空")
    private Long productId;
}
