package com.example.sens.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sens.common.base.BaseService;
import com.example.sens.entity.RechargeRecord;

/**
 * @author 言曌
 * @date 2020/3/22 3:18 下午
 */
public interface RechargeRecordService extends BaseService<RechargeRecord, Long> {

    Page<RechargeRecord> findAll(String startDate, String endDate, Page<RechargeRecord> page);

    /**
     * 根据用户ID获得充值记录
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param userId 用户ID
     * @param page 分页信息
     * @return 充值记录分页列表
     */
    Page<RechargeRecord> findByUserId(String startDate, String endDate, Long userId, Page<RechargeRecord> page);

    /**
     * 根据时间范围查询总金额
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 总金额
     */
    Long getTotalMoneySum(String startDate, String endDate);

    /**
     * 根据时间范围和用户查询总金额
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param userId 用户ID
     * @return 总金额
     */
    Long getTotalMoneySum(String startDate, String endDate, Long userId);

    /**
     * 根据时间范围和用户统计充值记录数
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param userId 用户ID
     * @return 充值记录数
     */
    long countByCondition(String startDate, String endDate, Long userId);
}
