package org.uts.business.service.order.impl;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.uts.business.mapper.OrderMapper;
import org.uts.dto.order.OrderDto;
import org.uts.service.order.OrderService;
import org.uts.vo.order.OrderVo;

import java.util.List;

/**
 * @Description 订单 服务实现类
 * @Author codBoy
 * @Date 2024/8/8 23:38
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;


    /*
     * 根据用户ID，查询用户订单信息
     */
    @Override
    public List<OrderVo> selectByUserId(Long userId) {
        List<OrderDto> orderDtoList = orderMapper.selectByUserId(userId);
        return OrderVo.convertDtoToVo(orderDtoList);
    }

    /*
     * 新增订单信息
     */
    public Integer addOrder(OrderVo orderVo) {
        OrderDto orderDto = OrderVo.convertVoToDto(orderVo);
        return orderMapper.addOrder(orderDto);
    }
}
