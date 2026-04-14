package com.wxshop.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WechatLoginRequest {

    @NotBlank(message = "code不能为空")
    private String code;
}
