package com.example.sens.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sens.entity.Order;
import com.example.sens.entity.Post;
import com.example.sens.entity.User;
import com.example.sens.enums.OrderStatusEnum;
import com.example.sens.mapper.OrderMapper;
import com.example.sens.service.OrderService;
import com.example.sens.service.PostService;
import com.example.sens.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 言曌
 * @date 2020/4/6 2:01 下午
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Override
    public BaseMapper<Order> getRepository() {
        return orderMapper;
    }

    @Override
    public QueryWrapper<Order> getQueryWrapper(Order order) {
        //对指定字段查询
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (order != null) {
            if (order.getUserId() != null) {
                queryWrapper.eq("user_id", order.getUserId());
            }
            if (order.getOwnerUserId() != null) {
                queryWrapper.eq("owner_user_id", order.getOwnerUserId());
            }
            if (order.getPostId() != null) {
                queryWrapper.eq("post_id", order.getPostId());
            }
            if (order.getStatus() != null) {
                queryWrapper.eq("status", order.getStatus());
            }
            if (order.getStartDate() != null) {
                queryWrapper.eq("start_date", order.getStartDate());
            }
            if (order.getEndDate() != null) {
                queryWrapper.eq("end_date", order.getEndDate());
            }
        }
        return queryWrapper;
    }

    @Override
    public Integer getTotalPriceSum(Order condition) {
        return orderMapper.getTotalPriceSum(condition);
    }

    @Override
    public Page<Order> findAll(Order condition, Page<Order> page) {
        return page.setRecords(orderMapper.findAll(condition, page));
    }

    @Override
    public Order findByPostId(Long postId) {
        return orderMapper.findByPostId(postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String refund(Long orderId) {
        Order order = get(orderId);
        if (order == null) {
            return "订单不存在";
        }

        Post post = postService.get(order.getPostId());
        Long deposit = post.getDeposit();

        User ownerUser = userService.get(order.getOwnerUserId());
        User tenantUser = userService.get(order.getUserId());

        order.setStatus(OrderStatusEnum.FINISHED.getCode());
        update(order);

        ownerUser.setMoney(ownerUser.getMoney() - deposit);
        userService.update(ownerUser);

        try {
            tenantUser.setMoney(tenantUser.getMoney() + deposit);
            userService.update(tenantUser);
        } catch (Exception e) {
            log.error("给租客退还押金时发生异常，订单ID：{}，异常信息：{}", orderId, e.getMessage(), e);
            order.setStatus(OrderStatusEnum.DEPOSIT_RETURN_FAIL.getCode());
            update(order);
        }

        return "退租成功";
    }
}
