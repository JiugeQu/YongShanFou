package com.mizore.mob.service;

import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
public interface ICommentService extends IService<Comment> {

    Result submit(Comment comment);

    Result getAllComments();
}
