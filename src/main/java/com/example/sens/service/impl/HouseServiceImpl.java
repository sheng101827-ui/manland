package com.example.sens.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sens.common.base.BaseService;
import com.example.sens.entity.House;
import com.example.sens.mapper.HouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public BaseMapper<House> getRepository() {
        return houseMapper;
    }

    @Override
    public House insert(House house) {
        houseMapper.insert(house);
        return house;
    }

    @Override
    public House update(House house) {
        houseMapper.updateById(house);
        return house;
    }

    @Override
    public House save(House house) {
        if (house.getId() != null) {
            houseMapper.updateById(house);
        } else {
            houseMapper.insert(house);
        }
        return house;
    }
}