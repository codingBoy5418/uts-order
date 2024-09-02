package org.uts.business.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.uts.dto.order.RefundDto;


/**
 * @Description 退款流水 Mapper类
 * @Author codBoy
 * @Date 2024/7/14 20:03
 */
@Mapper
public interface RefundMapper {

    /*
     * 根据订单ID，查询退款流水信息
     */
    public RefundDto selectRefund(String orderId);

    /*
     * 新增退款流水信息
     */
    public int addRefund(RefundDto refundDto);
}
