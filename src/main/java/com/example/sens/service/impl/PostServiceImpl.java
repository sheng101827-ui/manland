package com.example.sens.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sens.entity.Post;
import com.example.sens.enums.PostStatusEnum;
import com.example.sens.mapper.PostMapper;
import com.example.sens.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     房屋业务逻辑实现类
 * </pre>
 */
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    private final Map<Long, Object> bookingLocks = new ConcurrentHashMap<>();

    private final Set<Long> bookingPostIds = ConcurrentHashMap.newKeySet();

    @Override
    public Page<Post> findPostByCondition(Post condition, Page<Post> page) {
        List<Post> postList = postMapper.findPostByCondition(condition, page);
        return page.setRecords(postList);
    }

    @Override
    public Page<Post> findByRentUserId(Long userId, Page<Post> page) {
        List<Post> postList = postMapper.findByRentUserId(userId, page);
        return page.setRecords(postList);
    }


    @Override
    public BaseMapper<Post> getRepository() {
        return postMapper;
    }

    @Override
    public Post insert(Post post) {
        postMapper.insert(post);
        return post;
    }

    @Override
    public Post update(Post post) {
        postMapper.updateById(post);
        return post;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long postId) {
        postMapper.deleteById(postId);
    }

    @Override
    public QueryWrapper<Post> getQueryWrapper(Post post) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (post != null) {
            if (StrUtil.isNotBlank(post.getPostTitle())) {
                queryWrapper.like("post_title", post.getPostTitle());
            }
            if (StrUtil.isNotBlank(post.getPostContent())) {
                queryWrapper.like("post_content", post.getPostContent());
            }
            if (post.getPostStatus() != null && post.getPostStatus() != -1) {
                queryWrapper.eq("post_status", post.getPostStatus());
            }
        }
        return queryWrapper;
    }

    @Override
    public Post insertOrUpdate(Post post) {
        if (post.getId() == null) {
            insert(post);
        } else {
            update(post);
        }
        return post;
    }


    @Override
    public List<Post> getLatestPost(Long cityId, int limit) {
        return postMapper.getLatestPost(cityId, limit);
    }

    @Override
    public Integer countByStatus(Integer postStatus) {
        return postMapper.countByStatus(postStatus);
    }

    @Override
    public List<Post> getUnionRentPost(Post post) {
        Post temp = new Post();
        temp.setNumber(post.getNumber());
        temp.setUserId(post.getUserId());
        temp.setPostTitle(post.getPostTitle());
        temp.setCityId(post.getCityId());
        if (temp.getNumber() != null && temp.getNumber().length() > 2) {
            if (temp.getNumber().indexOf("室") != -1) {
                temp.setNumber(temp.getNumber().substring(0, temp.getNumber().indexOf("室") + 1));
            }
        }
        return postMapper.getUnionRentPost(temp);
    }

    @Override
    public boolean bookHouse(Long houseId, Long userId) {
        Object lock = bookingLocks.computeIfAbsent(houseId, key -> new Object());
        synchronized (lock) {
            boolean locked = bookingPostIds.add(houseId);
            if (!locked) {
                return false;
            }
            boolean releaseAfterTransaction = false;
            try {
                Post currentPost = postMapper.selectById(houseId);
                if (currentPost == null) {
                    return false;
                }
                if (!Objects.equals(currentPost.getPostStatus(), PostStatusEnum.ON_SALE.getCode())) {
                    return false;
                }
                if (Objects.equals(currentPost.getUserId(), userId)) {
                    return false;
                }
                Post updatePost = new Post();
                updatePost.setId(houseId);
                updatePost.setPostStatus(PostStatusEnum.OFF_SALE.getCode());
                if (postMapper.updateById(updatePost) <= 0) {
                    return false;
                }
                if (TransactionSynchronizationManager.isSynchronizationActive()) {
                    releaseAfterTransaction = true;
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            bookingPostIds.remove(houseId);
                        }
                    });
                }
                return true;
            } finally {
                if (!releaseAfterTransaction) {
                    bookingPostIds.remove(houseId);
                }
            }
        }
    }
}
