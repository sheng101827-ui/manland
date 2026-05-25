package com.example.sens.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sens.entity.House;
import com.example.sens.mapper.HouseMapper;
import com.example.sens.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void bookHouse(Long houseId, Long userId) {
        // 使用 houseId 的字符串调用 intern() 方法作为锁对象
        // 这样可以保证针对同一套房子的并发请求会被同步，而不同房子的请求可以并发执行，提升性能
        synchronized (String.valueOf(houseId).intern()) {
            // 使用 transactionTemplate 编程式事务
            // 确保在锁释放之前，事务已经完全提交，避免事务未提交导致下一个拿到锁的线程读到旧数据
            transactionTemplate.execute(status -> {
                // 1. 查询房屋最新状态
                House house = this.getById(houseId);
                
                if (house == null) {
                    throw new RuntimeException("房屋不存在");
                }
                
                // 2. 判断房屋是否已经被预定（假设 status=1 表示已预定，0表示未预定）
                if (house.getStatus() != null && house.getStatus() == 1) {
                    throw new RuntimeException("手慢了，该房屋已被预定");
                }
                
                // 3. 更新房屋状态和租客信息
                house.setStatus(1);
                house.setUserId(userId);
                
                // 4. 使用 MyBatis-Plus 的 updateById 来更新房屋状态
                this.updateById(house);
                
                return null;
            });
        }
    }
}