package com.wxshop.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("draw_prize_limit")
public class DrawPrizeLimit {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long prizeId;

    private Integer prizeLevel;

    private Integer winCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
