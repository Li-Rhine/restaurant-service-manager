package com.imooc.restaurantservicemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.restaurantservicemanager.dao.ProductDao;
import com.imooc.restaurantservicemanager.dao.RestaurantDao;
import com.imooc.restaurantservicemanager.dto.OrderMessageDTO;
import com.imooc.restaurantservicemanager.enummeration.ProductStatus;
import com.imooc.restaurantservicemanager.enummeration.RestaurantStatus;
import com.imooc.restaurantservicemanager.po.ProductPO;
import com.imooc.restaurantservicemanager.po.RestaurantPO;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 16:49
 **/
@Slf4j
@Service
public class OrderMessageService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductDao productDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Async
    public void handleMessage() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.104.74");
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();) {

            channel.exchangeDeclare(
                    "exchange.order.restaurant",
                    BuiltinExchangeType.DIRECT,
                    true,
                    false,
                    null
            );
            channel.queueDeclare(
                    "queue.restaurant",
                    true,
                    false,
                    false,
                    null
            );
            channel.queueBind(
                    "queue.restaurant",
                    "exchange.order.restaurant",
                    "key.restaurant"
            );

            channel.basicConsume("queue.restaurant", true, deliverCallback, consumerTag -> {});
            while (true) {
                Thread.sleep(100000);
            }
        }
    }


    //具体消费逻辑
    DeliverCallback deliverCallback = ((consumerTag, message) -> {
        String messageBody = new String(message.getBody());

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.104.74");

        try{
            //将消息反序列化
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(messageBody, OrderMessageDTO.class);
            ProductPO productPO = productDao.selectProduct(orderMessageDTO.getProductId());
            RestaurantPO restaurantPO = restaurantDao.selectRestaurant(productPO.getRestaurantId());
            if (productPO.getStatus() == ProductStatus.AVALIABLE
                    && restaurantPO.getStatus() == RestaurantStatus.OPEN) {
                orderMessageDTO.setConfirmed(true);
                orderMessageDTO.setPrice(productPO.getPrice());
            } else {
                orderMessageDTO.setConfirmed(false);
            }

            try(Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();) {

                String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
                channel.basicPublish(
                        "exchange.order.restaurant",
                        "key.order",
                        null,
                        messageToSend.getBytes());
            }


        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    });
            
            
            
}
