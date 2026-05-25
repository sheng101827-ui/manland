package com.example.sens.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sens.entity.House;

public interface HouseService extends IService<House> {
    
    /**
     * 预定房屋
     * @param houseId 房屋ID
     * @param userId 租客ID
     */
    void bookHouse(Long houseId, Long userId);
}