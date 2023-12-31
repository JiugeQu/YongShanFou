package com.mizore.mob.controller;

import com.mizore.mob.dto.Result;
import com.mizore.mob.service.ICommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    ICommentService commentService;


}
