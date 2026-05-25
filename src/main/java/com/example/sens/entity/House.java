package com.example.sens.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.sens.common.base.BaseEntity;
import lombok.Data;

@Data
@TableName("t_house")
public class House extends BaseEntity {

    private Long ownerUserId;

    private Long tenantUserId;

    private Integer status;

}