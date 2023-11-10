package com.mizore.mob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author mizore
 * @since 2023-10-22
 */
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String image;

    private String description;

    @NotEmpty
    private String name;

    /**
     * 1：上架  2：下架   3: 售罄
     * 默认上架状态
     */

    private Byte state = 1;

    /**
     * 菜品首次推出时间
     */
    private LocalDateTime createTime;

    /**
     * 销量
     */
    private Integer sale = 0;

    @NotEmpty
    private BigDecimal price;

    private String type;

    // 优先级 排序中越小越靠上，新菜默认为当前max(priority) + 1
    private int priority;

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", sale=" + sale +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", priority=" + priority +
                '}';
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
