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
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    @Autowired
    Channel channel;

    @Async
    public void handleMessage() throws IOException, TimeoutException, InterruptedException {

        channel.exchangeDeclare(
                "exchange.dlx",
                BuiltinExchangeType.TOPIC,
                true,
                false,
                null
        );

        channel.queueDeclare(
                "queue.dlx",
                true,
                false,
                false,
                null
        );

        channel.queueBind(
                "queue.dlx",
                "exchange.dlx",
                "#"
        );




        channel.exchangeDeclare(
                "exchange.order.restaurant",
                BuiltinExchangeType.DIRECT,
                true,
                false,
                null
        );

        Map<String, Object> args = new HashMap<>(16);
        //队列参数
        args.put("x-message-ttl", 15000);
        //出现死信转发到exchange.dlx交换机上
        args.put("x-dead-letter-exchange", "exchange.dlx");
        //设置长度为5 出现死信
        args.put("x-max-length", 5);


        channel.queueDeclare(
                "queue.restaurant",
                true,
                false,
                false,
                args
        );
        channel.queueBind(
                "queue.restaurant",
                "exchange.order.restaurant",
                "key.restaurant"
        );
        channel.basicQos(2);
        channel.basicConsume("queue.restaurant", false, deliverCallback, consumerTag -> {});
        while (true) {
            Thread.sleep(100000);
        }

    }


    //具体消费逻辑
    DeliverCallback deliverCallback = ((consumerTag, message) -> {
        String messageBody = new String(message.getBody());

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

            //这种走出try块后channel会是AutoClosable，自动关闭

//                channel.addReturnListener(new ReturnListener() {
//                    @Override
//                    public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                        log.info("Message Return: replyCode:{}, replyText:{}, exchange:{}, routingKey:{}, properties:{}, body:{}", replyCode, replyText, exchange, routingKey, properties, new String(body));
//                        // 除了打印log,可以添加别的业务操作
//
//                    }
//                });

            //和上面的写法是一样的
            channel.addReturnListener(new ReturnCallback() {
                @Override
                public void handle(Return returnMessage) {
                    log.info("Message Return:returnMessage:{}", returnMessage);

                }
            });
            //批量手动签收
            Thread.sleep(3000);
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

            String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);

            channel.basicPublish("exchange.order.restaurant", "key.order", true, null, messageToSend.getBytes());
            Thread.sleep(1000);


        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    });
            
            
            
}
