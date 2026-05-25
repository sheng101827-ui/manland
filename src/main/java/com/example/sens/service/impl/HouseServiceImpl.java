package com.example.sens.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sens.entity.House;
import com.example.sens.mapper.HouseMapper;
import com.example.sens.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HouseServiceImpl implements HouseService {

    private static final Integer STATUS_AVAILABLE = 0;
    private static final Integer STATUS_BOOKED = 1;

    private final ConcurrentHashMap<Long, Object> lockMap = new ConcurrentHashMap<>();

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public boolean bookHouse(Long houseId, Long userId) {
        Object lock = lockMap.computeIfAbsent(houseId, k -> new Object());
        synchronized (lock) {
            House house = houseMapper.selectById(houseId);
            if (house == null) {
                return false;
            }
            if (!Objects.equals(STATUS_AVAILABLE, house.getStatus())) {
                return false;
            }

            House update = new House();
            update.setId(houseId);
            update.setStatus(STATUS_BOOKED);
            update.setTenantUserId(userId);
            houseMapper.updateById(update);
            return true;
        }
    }

    @Override
    public BaseMapper<House> getRepository() {
        return houseMapper;
    }

    @Override
    public QueryWrapper<House> getQueryWrapper(House house) {
        return new QueryWrapper<>();
    }
}