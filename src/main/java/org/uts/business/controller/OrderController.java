package org.uts.business.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uts.result.RestResult;
import org.uts.service.order.OrderService;
import org.uts.vo.order.OrderVo;

import java.util.List;

/**
 * @Description 订单接口类
 * @Author codBoy
 * @Date 2024/8/1 21:00
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    /*
     * 根据商品ID，查询用户订单信息
     */
    @GetMapping("/{userId}")
    public RestResult order(@PathVariable Long userId){
        List<OrderVo> orderVoList = orderService.selectByUserId(userId);
        return RestResult.createSuccessfulRest(orderVoList);
    }

}
