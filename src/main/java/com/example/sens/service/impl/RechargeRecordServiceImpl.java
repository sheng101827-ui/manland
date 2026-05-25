package com.example.sens.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sens.entity.RechargeRecord;
import com.example.sens.entity.User;
import com.example.sens.mapper.RechargeRecordMapper;
import com.example.sens.mapper.UserMapper;
import com.example.sens.service.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 言曌
 * @date 2020/3/22 3:19 下午
 */
@Service
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseMapper<RechargeRecord> getRepository() {
        return rechargeRecordMapper;
    }

    @Override
    public QueryWrapper<RechargeRecord> getQueryWrapper(RechargeRecord rechargeRecord) {
        QueryWrapper<RechargeRecord> queryWrapper = new QueryWrapper<>();
        if (rechargeRecord != null) {
            if (rechargeRecord.getUserId() != null) {
                queryWrapper.eq("user_id", rechargeRecord.getUserId());
            }
        }
        return queryWrapper;
    }

    @Override
    public Page<RechargeRecord> findAll(String startDate, String endDate, Page<RechargeRecord> page) {
        rechargeRecordMapper.selectPage(page, buildQueryWrapper(startDate, endDate, null));
        fillUsers(page.getRecords());
        return page;
    }

    @Override
    public Page<RechargeRecord> findByUserId(String startDate, String endDate, Long userId, Page<RechargeRecord> page) {
        rechargeRecordMapper.selectPage(page, buildQueryWrapper(startDate, endDate, userId));
        fillUsers(page.getRecords());
        return page;
    }

    @Override
    public Long getTotalMoneySum(String startDate, String endDate) {
        return getTotalMoneySum(startDate, endDate, null);
    }

    @Override
    public Long getTotalMoneySum(String startDate, String endDate, Long userId) {
        return rechargeRecordMapper.selectList(buildQueryWrapper(startDate, endDate, userId)).stream()
                .map(RechargeRecord::getMoney)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();
    }

    @Override
    public long countByCondition(String startDate, String endDate, Long userId) {
        return count(buildQueryWrapper(startDate, endDate, userId));
    }

    private QueryWrapper<RechargeRecord> buildQueryWrapper(String startDate, String endDate, Long userId) {
        QueryWrapper<RechargeRecord> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(startDate)) {
            queryWrapper.ge("create_time", cn.hutool.core.date.DateUtil.parseDate(startDate));
        }
        if (StrUtil.isNotBlank(endDate)) {
            queryWrapper.lt("create_time", cn.hutool.core.date.DateUtil.offsetDay(cn.hutool.core.date.DateUtil.parseDate(endDate), 1));
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        return queryWrapper;
    }

    private void fillUsers(List<RechargeRecord> rechargeRecords) {
        if (rechargeRecords == null || rechargeRecords.isEmpty()) {
            return;
        }
        List<Long> userIds = rechargeRecords.stream()
                .map(RechargeRecord::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (userIds.isEmpty()) {
            return;
        }
        Map<Long, User> userMap = userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));
        for (RechargeRecord rechargeRecord : rechargeRecords) {
            rechargeRecord.setUser(userMap.get(rechargeRecord.getUserId()));
        }
    }
}
