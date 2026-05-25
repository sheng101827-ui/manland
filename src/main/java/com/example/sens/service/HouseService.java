package com.example.sens.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sens.entity.Post;

import java.util.List;

/**
 * 房屋特殊业务逻辑接口
 */
public interface HouseService extends IService<Post> {

    /**
     * 按面积检索房屋，先用 list() 查询满足条件的房屋，再用 Stream 截取对应页码数据
     *
     * @param minArea 最小面积
     * @param maxArea 最大面积
     * @param page    页码
     * @param size    每页数量
     * @return 分页后的房屋列表
     */
    List<Post> getByArea(int minArea, int maxArea, int page, int size);
}
