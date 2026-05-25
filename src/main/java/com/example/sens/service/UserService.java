package com.example.sens.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sens.common.base.BaseService;
import com.example.sens.entity.User;

 */
public interface UserService extends BaseService<User, Long> {

    /**
     * 根据账号获得用户
     *
     * @param userName 账号
     * @return 用户
     */
    User findByUserName(String userName);


    /**
     * 根据身份证号码查找用户
     *
     * @param idCard 身份证号码
     * @return User
     */
    User findByIdCard(String idCard);

    /**
     * 更新密码
     *
     * @param userId   用户Id
     * @param password 密码
     */
    void updatePassword(Long userId, String password);

    /**
     * 分页获取所有用户
     *
     * @param roleName  角色名称
     * @param condition 查询条件
     * @param page      分页信息
     * @return 用户列表
     */
    Page<User> findByRoleAndCondition(String roleName, User condition, Page<User> page);

    User getById(Long id);

    void updateById(User user);

    /**
     * 充值并记录流水
     *
     * @param userId 用户Id
     * @param amount 充值金额
     */
    void recharge(Long userId, java.math.BigDecimal amount);

}
