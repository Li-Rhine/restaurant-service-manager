package com.imooc.restaurantservicemanager.dao;

import com.imooc.restaurantservicemanager.po.ProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 16:40
 **/
@Mapper
@Repository
public interface ProductDao {

    @Select("SELECT id, name, price, restaurant_id restaurantId, status, date FROM product WHERE id = #{id}")
    ProductPO selectProduct(Integer id);
}
