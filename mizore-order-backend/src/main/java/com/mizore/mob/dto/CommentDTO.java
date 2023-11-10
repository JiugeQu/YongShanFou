package com.mizore.mob.dto;

import com.mizore.mob.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CommentDTO extends Comment {

    private String customerName;

    private BigDecimal totalPrice;

}
