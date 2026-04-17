package com.wxshop.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("draw_chance_rule")
public class DrawChanceRule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String chanceType;

    private String triggerCondition;

    private BigDecimal minAmount;

    private Integer giveCount;

    private Integer dailyLimit;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
