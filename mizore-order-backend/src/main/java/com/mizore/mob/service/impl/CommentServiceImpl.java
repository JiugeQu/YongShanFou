package com.mizore.mob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mizore.mob.dto.CommentDTO;
import com.mizore.mob.dto.Result;
import com.mizore.mob.entity.Comment;
import com.mizore.mob.entity.Order;
import com.mizore.mob.mapper.CommentMapper;
import com.mizore.mob.mapper.OrderMapper;
import com.mizore.mob.mapper.UserMapper;
import com.mizore.mob.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mizore.mob.util.BeanUtil;
import com.mizore.mob.util.UserHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mizore.mob.util.Constant.ORDER_COMMENTED;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
@Service
@AllArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    private OrderMapper orderMapper;

    private UserMapper userMapper;

    @Override
    public Result submit(Comment comment) {
        // 1. 插入评价
        comment.setUserId(UserHolder.get().getId());
        boolean res = save(comment);
        if (!res) {
            Result.error("评价提交失败！！");
        }

        // 2. 改订单状态
        Long orderCode = comment.getOrderCode();
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("state", ORDER_COMMENTED)
                .eq("code", orderCode);
        return orderMapper.update(null, updateWrapper) == 1 ?
                Result.ok() : Result.error("评价提交失败！！");
    }

    /**
     * 商家获取全部评价
     * 订单号， 评价内容， 评价创建时间， 总价， 顾客名
     */
    @Override
    public Result getAllComments() {
        List<Comment> comments = query()
                .select("order_code", "content", "create_time", "user_id")
                .list();
        // List<Comment> -> List<CommentDTO>
        List<CommentDTO> res = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtil.copyNotNullProperties(comment, commentDTO);

            // 填充总价和顾客名
            BigDecimal totalPrice = orderMapper.selectTotalPriceByCode(comment.getOrderCode());
            String customerName = userMapper.selectNameById(comment.getUserId());
            commentDTO.setTotalPrice(totalPrice)
                    .setCustomerName(customerName);

            res.add(commentDTO);

            // 不需要的属性
            commentDTO.setUserId(null);
        }

        return Result.ok(res);
    }
}
