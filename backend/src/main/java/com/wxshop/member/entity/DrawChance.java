package com.wxshop.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("draw_chance")
public class DrawChance {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer remainingCount;

    private Integer totalCount;

    private Integer usedCount;

    private LocalDateTime lastUpdateTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
