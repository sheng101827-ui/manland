package com.example.sens.entity;

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

    private Integer price;

    private String address;

    private Double area;

    private Integer roomCount;

    private Integer toiletCount;

    private String imgUrl;

    private Integer status;

    private Date createTime;
}
