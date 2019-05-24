package com.example.demo.controller;

import com.example.demo.exception.DemoException;
import com.example.demo.model.TradeOrder;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * demo controller.
 *
 * @author yuan.cheng
 */
@RestController
@RequiredArgsConstructor
public class DemoController {
    private final OrderService orderService;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "world") String name) {
        return "hello " + name;
    }

    /**
     * order query.
     *
     * @param id id for order
     * @return {@link TradeOrder}
     */
    @GetMapping("order/{id}")
    public TradeOrder queryOrder(@PathVariable("id") int id) throws DemoException {
        return orderService.queryOrder(id);
    }

    @PutMapping("order")
    public TradeOrder addOrder(@RequestBody TradeOrder tradeOrder) throws DemoException {
        return orderService.addOrder(tradeOrder);
    }

    @PostMapping("order/{id}")
    public TradeOrder modifyOrder(@PathVariable("id") int id, @RequestBody TradeOrder tradeOrder) throws DemoException {
        tradeOrder.setId(id);
        return orderService.modifyOrder(tradeOrder);
    }

    /**
     * query all order.
     *
     * @return list of {@link TradeOrder}
     */
    @GetMapping("order")
    public List<TradeOrder> queryAllOrder() {
        return orderService.queryAll();
    }

    @GetMapping("status")
    public void status() {
        // nothing to return
    }

}
