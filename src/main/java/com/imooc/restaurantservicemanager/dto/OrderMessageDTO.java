package com.imooc.restaurantservicemanager.dto;

import com.imooc.restaurantservicemanager.enummeration.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/21 15:30
 **/
@Getter
@Setter
@ToString
public class OrderMessageDTO {
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 订单状态
     */
    private OrderStatus orderStatus;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 骑手ID
     */
    private Integer deliverymanId;
    /**
     * 产品ID
     */
    private Integer productId;
    /**
     * 用户ID
     */
    private Integer accountId;
    /**
     * 结算ID
     */
    private Integer settlementId;
    /**
     * 积分结算ID
     */
    private Integer rewardId;
    /**
     * 积分奖励数量
     */
    private Integer rewardAmount;
    /**
     * 确认
     */
    private Boolean confirmed;
}
