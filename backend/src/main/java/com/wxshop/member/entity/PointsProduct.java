package com.wxshop.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("points_product")
public class PointsProduct {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String image;

    private Integer points;

    private BigDecimal price;

    private String category;

    private Integer stock;

    private Integer exchangedCount;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
