package com.example.demo.controller;

import com.example.demo.exception.DemoException;
import com.example.demo.model.OrderType;
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

import java.time.LocalDate;
import java.time.LocalTime;
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

    /**
     * will return plain text with Content-Type text/plain.
     *
     * @param name who you will say hello to
     * @return greeting text
     */
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "world") String name) {
        return "hello " + name;
    }

    /**
     * query order by id.
     *
     * @param id id for order
     * @return {@link TradeOrder}
     */
    @GetMapping("order/{id}")
    public TradeOrder queryOrder(@PathVariable("id") int id) throws DemoException {
        return orderService.queryOrder(id);
    }

    /**
     * add an order.
     *
     * @param tradeOrder {@link TradeOrder}
     * @return added {@link TradeOrder}
     * @throws DemoException throw when error occur
     */
    @PutMapping("order")
    public TradeOrder addOrder(@RequestBody TradeOrder tradeOrder) throws DemoException {
        return orderService.addOrder(tradeOrder);
    }

    /**
     * modify an order.
     *
     * @param id order's id
     * @param tradeOrder {@link TradeOrder}
     * @return modified {@link TradeOrder}
     * @throws DemoException throw when error occur
     */
    @PostMapping("order/{id}")
    public TradeOrder modifyOrder(@PathVariable("id") int id, @RequestBody TradeOrder tradeOrder) throws DemoException {
        tradeOrder.setId(id);
        return orderService.modifyOrder(tradeOrder);
    }

    /**
     * query all order.
     *
     * @param type order type of value ["BUY", "SELL"]
     * @return list of {@link TradeOrder}
     */
    @GetMapping("order")
    public List<TradeOrder> queryAllOrder(@RequestParam(value = "type", required = false) OrderType type) {
        if (null == type) {
            return orderService.queryAll();
        } else {
            return orderService.queryOrderByType(type);
        }
    }

    @GetMapping("today")
    public LocalDate today() {
        return LocalDate.now();
    }

    @GetMapping("time")
    public LocalTime time() {
        return LocalTime.now();
    }

    /**
     * return empty response body.
     */
    @GetMapping("status")
    public void status() {
        // nothing to return
    }

}
