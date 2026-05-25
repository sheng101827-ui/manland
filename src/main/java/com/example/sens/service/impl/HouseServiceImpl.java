package com.example.sens.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sens.entity.Post;
import com.example.sens.mapper.PostMapper;
import com.example.sens.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 房屋特殊业务逻辑实现类
 */
@Service
public class HouseServiceImpl extends ServiceImpl<PostMapper, Post> implements HouseService {

    @Override
    public List<Post> getByArea(int minArea, int maxArea, int page, int size) {
        // 构建面积查询条件
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("area", minArea).le("area", maxArea);

        // 1. 先用 MyBatis-Plus 的 list() 方法查出所有满足面积条件的房屋（放入 List 中）
        List<Post> allHouses = this.list(queryWrapper);

        // 保证页码最少为1
        int actualPage = Math.max(1, page);

        // 2. 然后用 Java 8 的 Stream API 截取对应页码的数据返回给前端
        return allHouses.stream()
                .skip((long) (actualPage - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }
}
