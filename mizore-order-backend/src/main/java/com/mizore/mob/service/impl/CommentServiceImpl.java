package com.mizore.mob.service.impl;

import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Comment;
import com.mizore.mob.mapper.CommentMapper;
import com.mizore.mob.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Override
    public Result getByDishId(Integer dishId) {
        List<Comment> comments = query().eq("dish_id", dishId).list();
        return comments == null ? Result.error("获取评论失败！！") : Result.ok(comments);
    }
}
