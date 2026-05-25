package com.example.sens.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.sens.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("house")
public class House extends BaseEntity {

    private Long landlordId;

    private String title;

    private String description;

    private String address;

    private Integer price;

    private Integer area;

    private String thumbnail;

    private Integer roomCount;

    private Integer toiletCount;

    private Integer status;

    private String city;

    private String district;

    @TableField(exist = false)
    private User landlord;

    @TableField(exist = false)
    private Category category;
}