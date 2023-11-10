package com.mizore.mob.controller.customer;

import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Comment;
import com.mizore.mob.service.ICommentService;
import com.mizore.mob.util.UserHolder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
@RestController("customerCommentController")
@RequestMapping("/customer/comment")
@AllArgsConstructor
public class CommentController {

    ICommentService commentService;

    /**
     * 提交评价
     * @param comment 包括评价内容和订单号
     */
    @PostMapping("/submit")
    public Result submitComment(@RequestBody Comment comment) {
        return commentService.submit(comment);
    }
}
