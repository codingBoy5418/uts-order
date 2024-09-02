package org.uts.business.service.order.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.uts.business.mapper.RefundMapper;
import org.uts.dto.order.RefundDto;
import org.uts.service.order.RefundService;
import org.uts.vo.order.RefundVo;

/**
 * @Description 退款流水 Service实现类
 * @Author codBoy
 * @Date 2024/9/1 20:31
 */
@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    private RefundMapper refundMapper;

    /*
     * 根据订单ID，查询退款流水信息
     */
    @Override
    public RefundVo selectRefund(String orderId) {
        RefundVo refundVo = null;
        RefundDto refundDto = refundMapper.selectRefund(orderId);
        if(refundDto != null){
            refundVo = refundDto.convertPoToVo();
        }
        return refundVo;
    }

    /*
     * 新增退款流水信息
     */
    @Override
    public int addRefund(RefundVo refundVo) {
        return refundMapper.addRefund(refundVo.convertVoToPo());
    }
}
