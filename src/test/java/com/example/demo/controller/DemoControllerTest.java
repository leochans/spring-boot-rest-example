package com.example.demo.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.OrderType;
import com.example.demo.model.TradeOrder;
import com.example.demo.service.OrderService;
import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
public class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;


    @Test
    public void hello() throws Exception {
        this.mockMvc.perform(get("/hello")).andDo(print()).andExpect(status().isOk())
            .andExpect(content().string(containsString("hello world")));
    }

    @Test
    public void queryOrder() throws Exception {
        TradeOrder expect = new TradeOrder();
        expect.setId(1);
        expect.setSummary("demo tradeOrder");
        expect.setDate(LocalDateTime.now());
        expect.setType(OrderType.SELL);
        when(orderService.queryOrder(1)).thenReturn(expect);
        this.mockMvc.perform(get("/order/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().json("{\"code\":0,\"msg\":\"success\",\"data\":{\"id\":1,\"summary\":\"demo tradeOrder\", \"type\":\"SELL\",\"price\":null, \"amount\":null}}", false))
            .andExpect(jsonPath("$.data.date", new CustomTypeSafeMatcher<String>("date of format yyyy-MM-ddTHH:mm:ss.SSS") {
                @Override
                protected boolean matchesSafely(String item) {
                    return item.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.?\\d?\\d?\\d?");
                }
            }));

    }

    @Test
    public void addOrder() {
    }

    @Test
    public void modifyOrder() {
    }

    @Test
    public void queryAllOrder() {
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