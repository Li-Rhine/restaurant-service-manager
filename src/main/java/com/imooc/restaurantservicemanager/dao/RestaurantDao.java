package com.imooc.restaurantservicemanager.dao;

import com.imooc.restaurantservicemanager.po.RestaurantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 16:47
 **/
@Mapper
@Repository
public interface RestaurantDao {

    @Select("SELECT id, name, address, status, date FROM restaurant WHERE id = #{id}")
    RestaurantPO selectRestaurant(Integer id);
}
