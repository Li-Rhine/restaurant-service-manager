package com.imooc.restaurantservicemanager.po;

import com.imooc.restaurantservicemanager.enummeration.RestaurantStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 16:38
 **/
@Getter
@Setter
@ToString
public class RestaurantPO {

    private Integer id;
    private String name;
    private String address;
    private RestaurantStatus status;
    private Date date;
}
