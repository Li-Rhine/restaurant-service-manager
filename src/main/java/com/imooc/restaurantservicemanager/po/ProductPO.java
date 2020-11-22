package com.imooc.restaurantservicemanager.po;

import com.imooc.restaurantservicemanager.enummeration.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 16:36
 **/
@Getter
@Setter
@ToString
public class ProductPO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer restaurantId;
    private ProductStatus status;
    private Date date;
}
