package com.imooc.restaurantservicemanager.enummeration;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 15:06
 **/
public enum OrderStatus {
    /**
     * 创建中
     */
    ORDER_CREATING,
    /**
     *餐厅已确认
     */
    RESTAURANT_CONFIRMED,
    /**
     * 骑手已确认
     */
    DELIVERMAN_CONFIRMED,
    /**
     * 已结算
     */
    SETTLEMENT_CONFIRMED,
    /**
     * 订单已创建
     */
    ORDER_CREATED,
    /**
     * 订单创建失败
     */
    FAILED;
}
