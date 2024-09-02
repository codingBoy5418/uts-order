package org.uts.mq.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.uts.exception.BusinessException;
import org.uts.global.constant.*;
import org.uts.mq.message.AddOrderMessage;
import org.uts.mq.message.AddOrderResultMessage;
import org.uts.mq.message.MQMessage;
import org.uts.mq.message.OrderDelayMessage;
import org.uts.service.order.OrderService;
import org.uts.utils.SnowflakeUtils;
import org.uts.vo.order.OrderVo;

import java.util.Objects;
import java.util.UUID;

/**
 * @Description 订单MQ消息处理类
 * @Author codBoy
 * @Date 2024/8/24 21:09
 */
@Component
@Slf4j
public class OrderHandler {
    @Autowired
    public OrderService orderService;

    private final SnowflakeUtils snowflakeUtils = new SnowflakeUtils(1, 1);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
     处理新增订单消息
     */
    @Transactional(rollbackFor = Exception.class)
    public void dealWithAddOrder(AddOrderMessage addOrderMsg) {
        //创建订单并入库
        OrderVo orderVo = new OrderVo(null,
                String.valueOf(snowflakeUtils.nextId()),
                null,
                addOrderMsg.getUserId(),
                addOrderMsg.getSeckillId(),
                addOrderMsg.getProductName(),
                OrderStatusEnum.WAIT_TO_PAY_STATUS.getStatus(),
                null,
                PlatformTypeEnum.WEB_CLIENT_TYPE.getId(),
                addOrderMsg.getPrice(),
                null,
                null,
                null);

        try {
            orderService.addOrder(orderVo);
            //TODO：这里牵扯到分布式事务，后面再解决
            //int n = 1/0;

            //WS适用于服务端和客户端之间的场景。
            AddOrderResultMessage addOrderResultMessage = new AddOrderResultMessage(orderVo.getUserId(), orderVo.getProductId(), orderVo.getOrderId(), BusinessConstant.SUCCESS);
            MQMessage mqMessage = new MQMessage(UUID.randomUUID().toString(), MessageCategory.BUSINESS_MESSAGE_TYPE, MessageType.ADD_ORDER_RESULT_MESSAGE_TYPE, JSON.toJSONString(addOrderResultMessage));
            //先发送创建订单结果消息到事件中心，由事件中心发送WebSocket消息到客户端
            log.info("Send ADD_ORDER_RESULT_MESSAGE Message To uts-event...");
            rabbitTemplate.convertAndSend(MqEnum.UTS_EVENT_BUSINESS_QUEUE.getExchange(), MqEnum.UTS_EVENT_BUSINESS_QUEUE.getRoutingKey(), JSON.toJSONString(mqMessage));
            //然后发送创建订单结果消息到秒杀服务，当下单失败时，由秒杀服务进行回滚
            log.info("Send ADD_ORDER_RESULT_MESSAGE Message To uts-seckill...");
            rabbitTemplate.convertAndSend(MqEnum.UTS_SECKILL_QUEUE.getExchange(), MqEnum.UTS_SECKILL_QUEUE.getRoutingKey(), JSON.toJSONString(mqMessage));
        } catch (Exception e) {
            e.printStackTrace();
            //WS适用于服务端和客户端之间的场景。
            AddOrderResultMessage addOrderResultMessage = new AddOrderResultMessage(orderVo.getUserId(), orderVo.getProductId(), orderVo.getOrderId(), BusinessConstant.FAILED);
            MQMessage mqMessage = new MQMessage(UUID.randomUUID().toString(), MessageCategory.BUSINESS_MESSAGE_TYPE, MessageType.ADD_ORDER_RESULT_MESSAGE_TYPE, JSON.toJSONString(addOrderResultMessage));

            //先发送创建订单结果消息到事件中心，由事件中心发送WebSocket消息到客户端
            log.info("Send ADD_ORDER_RESULT_MESSAGE Message To uts-event...");
            rabbitTemplate.convertAndSend(MqEnum.UTS_EVENT_BUSINESS_QUEUE.getExchange(), MqEnum.UTS_EVENT_BUSINESS_QUEUE.getRoutingKey(), JSON.toJSONString(mqMessage));
            //然后发送创建订单结果消息到秒杀服务，当下单失败时，由秒杀服务进行回滚
            log.info("Send ADD_ORDER_RESULT_MESSAGE Message To uts-seckill...");
            rabbitTemplate.convertAndSend(MqEnum.UTS_SECKILL_QUEUE.getExchange(), MqEnum.UTS_SECKILL_QUEUE.getRoutingKey(), JSON.toJSONString(mqMessage));

        }

    }

    /*
     处理订单超时消息
     */
    public void dealWithOrderDelayResult(OrderDelayMessage orderTimeoutMessage) {

        try {
            OrderVo orderVo = new OrderVo();
            orderVo.setOrderId(orderTimeoutMessage.getOrderId());
            orderVo.setUserId(orderTimeoutMessage.getUserId());
            OrderVo orderVoFromDB = orderService.selectOrder(orderVo).get(0);
            if(Objects.equals(OrderStatusEnum.WAIT_TO_PAY_STATUS.getStatus(), orderVoFromDB.getStatus())
                    || Objects.equals(OrderStatusEnum.SYSTEM_CANCEL_STATUS.getStatus(), orderVoFromDB.getStatus())
                    || Objects.equals(OrderStatusEnum.HAND_CANCEL_STATUS.getStatus(), orderVoFromDB.getStatus())
            ){
                orderVo.setStatus(OrderStatusEnum.SYSTEM_CANCEL_STATUS.getStatus());
                orderService.updateOrder(orderVo);
            }
        } catch (BusinessException businessException) {
            log.error("Update Order Stock failed, orderId: {}", orderTimeoutMessage.getOrderId());
            businessException.printStackTrace();
        }

    }
}
