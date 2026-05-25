package com.example.sens.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_house")
public class House {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 房屋状态：0-未预定，1-已预定
     */
    private Integer status;
    
    /**
     * 预定用户的ID
     */
    private Long userId;
}