package com.wxshop.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("lucky_draw")
public class LuckyDraw {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long prizeId;

    private String prizeName;

    private Integer prizeType;

    private Integer points;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
