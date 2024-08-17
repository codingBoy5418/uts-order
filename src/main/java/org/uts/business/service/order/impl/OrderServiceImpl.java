package org.uts.business.service.order.impl;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.uts.business.mapper.OrderMapper;
import org.uts.dto.order.OrderDto;
import org.uts.exception.BusinessException;
import org.uts.global.errorCode.BusinessErrorCode;
import org.uts.service.order.OrderService;
import org.uts.vo.order.OrderVo;

import java.util.List;
import java.util.Objects;

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
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(userId);
        List<OrderDto> orderDtoList = orderMapper.selectOrder(orderDto);
        return OrderVo.convertDtoToVo(orderDtoList);
    }

    /*
     * 新增订单信息
     */
    public Integer addOrder(OrderVo orderVo) {
        OrderDto orderDto = OrderVo.convertVoToDto(orderVo);
        return orderMapper.addOrder(orderDto);
    }

    /*
     * 查询订单详情信息接口
     */
    @Override
    public List<OrderVo> selectOrder(OrderVo orderVo) throws BusinessException {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(orderVo.getOrderId());
        List<OrderDto> orderDtoList = orderMapper.selectOrder(orderDto);

        if(CollectionUtils.isEmpty(orderDtoList)) {
            throw new BusinessException(BusinessErrorCode.ORDER_IS_NOT_EXIST);
        }

        OrderDto orderFromDb = orderDtoList.get(0);
        if(!Objects.equals(orderFromDb.getUserId(), orderVo.getUserId())) {
            throw new BusinessException(BusinessErrorCode.ORDER_QUERY_ILLEGAL);
        }

        return OrderVo.convertDtoToVo(orderDtoList);
    }
}
