package org.uts.business.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.uts.dto.order.OrderDto;


import java.util.List;

/**
 * @Description 订单 Mapper类
 * @Author codBoy
 * @Date 2024/7/14 20:03
 */
@Mapper
public interface OrderMapper {

    /*
     * 查询用户订单信息
     */
    public List<OrderDto> selectOrder(OrderDto orderDto);

    /*
     * 新增订单信息
     */
    public Integer addOrder(OrderDto orderDto);

    /*
     * 更新订单信息
     */
    public Integer updateOrder(OrderDto orderDto);
}
