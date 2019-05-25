package com.example.demo.service;

import com.example.demo.exception.DemoException;
import com.example.demo.model.TradeOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * order service.
 *
 * @author yuan.cheng
 */
@Service
public class OrderService {

    public TradeOrder queryOrder(int id) throws DemoException {
        if (id == 7) {
            throw new DemoException(101, "wrong order");
        }
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setId(id);
        tradeOrder.setSummary("demo tradeOrder");
        tradeOrder.setDate(LocalDateTime.now());
        return tradeOrder;
    }

    public List<TradeOrder> queryAll() {
        TradeOrder tradeOrder1 = new TradeOrder();
        tradeOrder1.setId(1);
        tradeOrder1.setSummary("demo order 1");
        tradeOrder1.setDate(LocalDateTime.now());
        TradeOrder tradeOrder2 = new TradeOrder();
        tradeOrder2.setId(2);
        tradeOrder2.setSummary("demo order 2");
        tradeOrder2.setDate(LocalDateTime.now());
        return Arrays.asList(tradeOrder1, tradeOrder2);
    }

    public TradeOrder addOrder(TradeOrder tradeOrder) throws DemoException {
        if (tradeOrder.getId() == 0) {
            throw new DemoException(1001, "add order error");
        }
        return tradeOrder;
    }

    public TradeOrder modifyOrder(TradeOrder tradeOrder) throws DemoException {
        if (tradeOrder.getId()== 0 ) {
            throw new DemoException(1002, "modify order error");
        }
        return tradeOrder;
    }
}
