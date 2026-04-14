package com.wxshop.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("check_in")
public class CheckIn {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDate checkDate;

    private Integer points;

    private Integer continuousDays;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
