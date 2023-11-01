package com.mizore.mob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.mizore.mob.util.Constant.ORDER_WAIT_CONFIRM;

/**
 * <p>
 * 
 * </p>
 *
 * @author mizore
 * @since 2023-10-30
 */
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 使用雪花算法生成的订单编号
     */
    private Long code;

    /**
     * 1：待接单-用户完成支付但待商家确认，
     * 2：备餐中-商家确认后处于备餐中，
     * 3：待配送-备餐完毕等待骑手配送，
     * 4：配送中-骑手抢单后到送达前，
     * 5：已送达-骑手送达
     */
    private Byte state = ORDER_WAIT_CONFIRM;

    private Integer deliverId;

    private String address;

    private BigDecimal totalPrice;

    private Integer userId;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 顾客指定的送达时间
     */
    private LocalDateTime expectArriveTime;

    /**
     * 订单实际送达时间
     */
    private LocalDateTime realArriveTime;

    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Integer getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(Integer deliverId) {
        this.deliverId = deliverId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getExpectArriveTime() {
        return expectArriveTime;
    }

    public void setExpectArriveTime(LocalDateTime expectArriveTime) {
        this.expectArriveTime = expectArriveTime;
    }

    public LocalDateTime getRealArriveTime() {
        return realArriveTime;
    }

    public void setRealArriveTime(LocalDateTime realArriveTime) {
        this.realArriveTime = realArriveTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id = " + id +
            ", code = " + code +
            ", state = " + state +
            ", deliverId = " + deliverId +
            ", address = " + address +
            ", totalPrice = " + totalPrice +
            ", userId = " + userId +
            ", createTime = " + createTime +
            ", expectArriveTime = " + expectArriveTime +
            ", realArriveTime = " + realArriveTime +
            ", note = " + note +
        "}";
    }
}
