package com.wxshop.member.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("draw_risk_config")
public class DrawRiskConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String configKey;

    private String configName;

    private String configType;

    private String configValue;

    private String defaultValue;

    private String options;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
