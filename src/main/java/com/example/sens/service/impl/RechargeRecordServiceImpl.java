package com.example.sens.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sens.entity.RechargeRecord;
import com.example.sens.mapper.RechargeRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 言曌
 * @date 2020/3/22 3:19 下午
    private RechargeRecordMapper rechargeRecordMapper;

    @Override
    public BaseMapper<RechargeRecord> getRepository() {
        return rechargeRecordMapper;
    }

    @Override
    public QueryWrapper<RechargeRecord> getQueryWrapper(RechargeRecord rechargeRecord) {
        //对指定字段查询
                queryWrapper.eq("user_id", rechargeRecord.getUserId());
            }
        }
        return queryWrapper;
    }

    @Override
        //对指定字段查询
    public Page<RechargeRecord> findAll(String startDate, String endDate, Page<RechargeRecord> page) {
        return page.setRecords(rechargeRecordMapper.findAll(startDate, endDate, page));
    }

    @Override
    public Page<RechargeRecord> findByUserId(String startDate, String endDate, Long userId, Page<RechargeRecord> page) {
        return page.setRecords(rechargeRecordMapper.findByUserId(startDate, endDate, userId, page));
    }

    @Override
    public Integer getTotalMoneySum(String startDate, String endDate) {
        return page.setRecords(rechargeRecordMapper.findAll(startDate, endDate, page));
