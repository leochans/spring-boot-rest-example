package com.example.demo.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.exception.DemoException;
import com.example.demo.model.OrderType;
import com.example.demo.model.TradeOrder;
import com.example.demo.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Collections;

@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
public class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private String loadJson(String fileName) throws IOException {
        return new String(Files.readAllBytes(ResourceUtils.getFile("classpath:" + fileName).toPath()));
    }

    @Test
    public void hello() throws Exception {
        this.mockMvc.perform(get("/hello")).andDo(print()).andExpect(status().isOk())
            .andExpect(content().string(containsString("hello world")));
    }

    @Test
    public void queryOrder() throws Exception {
        TradeOrder order = new TradeOrder();
        order.setId(1);
        order.setSummary("demo tradeOrder");
        order.setDate(LocalDateTime.of(2019, 3, 14, 7, 58, 19, 111 * 1000000));
        order.setType(OrderType.SELL);
        when(orderService.queryOrder(1)).thenReturn(order);
        this.mockMvc.perform(get("/order/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(loadJson("queryOrder.json"), true));
    }

    @Test
    public void addOrder() throws Exception {
        when(orderService.addOrder(ArgumentMatchers.any(TradeOrder.class)))
            .thenThrow((new DemoException(100, "add order error")));
        this.mockMvc.perform(put("/order").contentType(MediaType.APPLICATION_JSON).content("{\"id\":0}"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json("{\"code\":0,\"msg\":\"success\",\"data\":{}}"));
    }

    @Test
    public void modifyOrder() throws Exception {
        TradeOrder order = new TradeOrder();
        order.setId(1);
        order.setSummary("demo tradeOrder");
        when(orderService.modifyOrder(order)).thenReturn(order);
        this.mockMvc.perform(post("/order/1").contentType(MediaType.APPLICATION_JSON).content("{\"summary\":\"demo tradeOrder\"}"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.data.summary").value("demo tradeOrder"));
    }

    @Test
    public void queryAllOrder() throws Exception {
        TradeOrder order = new TradeOrder();
        order.setId(1);
        order.setSummary("demo tradeOrder");
        order.setDate(LocalDateTime.of(2019, 3, 14, 7, 58, 19, 111 * 1000000));
        order.setType(OrderType.SELL);
        when(orderService.queryOrderByType(OrderType.SELL)).thenReturn(Collections.singletonList(order));
        this.mockMvc.perform(get("/order").param("type","SELL"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json(loadJson("orderList.json"), true));
    }

    @Test
    public void today() {
    }

    @Test
    public void time() {
    }

    @Test
    public void statusTest() {
    }
}