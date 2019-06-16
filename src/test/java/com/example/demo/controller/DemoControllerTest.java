package com.example.demo.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.exception.DemoException;
import com.example.demo.model.OrderType;
import com.example.demo.model.TradeOrder;
import com.example.demo.service.OrderService;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Collections;
import org.hamcrest.CustomTypeSafeMatcher;
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

@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
public class DemoControllerTest {

  private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
  private static final String TIME_PATTERN = "\\d{2}:\\d{2}:\\d{2}\\.?\\d*";

  @Autowired private MockMvc mockMvc;

  @MockBean private OrderService orderService;

  private String loadJson(String fileName) throws IOException {
    return new String(Files.readAllBytes(ResourceUtils.getFile("classpath:" + fileName).toPath()));
  }

  @Test
  public void hello() throws Exception {
    this.mockMvc
        .perform(get("/hello"))
        .andExpect(status().isOk())
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
    this.mockMvc
        .perform(get("/order/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(loadJson("queryOrder.json"), true));
  }

  @Test
  public void addOrder() throws Exception {
    when(orderService.addOrder(ArgumentMatchers.any(TradeOrder.class)))
        .thenThrow((new DemoException(100, "add order error")));
    this.mockMvc
        .perform(
            put("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":0,\"summary\":\"hello\",\"type\":\"BUY\"}"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"code\":100,\"msg\":\"add order error\",\"data\":{}}"));
  }

  @Test
  public void modifyOrder() throws Exception {
    TradeOrder order = new TradeOrder();
    order.setId(1);
    order.setSummary("demo tradeOrder");
    when(orderService.modifyOrder(order)).thenReturn(order);
    this.mockMvc
        .perform(
            post("/order/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"summary\":\"demo tradeOrder\"}"))
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
    when(orderService.queryOrderByType(OrderType.SELL))
        .thenReturn(Collections.singletonList(order));
    this.mockMvc
        .perform(get("/order").param("type", "SELL"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(loadJson("orderList.json"), true));
  }

  @Test
  public void today() throws Exception {
    this.mockMvc
        .perform(get("/today"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(
            jsonPath("$.data")
                .value(
                    new CustomTypeSafeMatcher<String>("date of format yyyy-MM-dd") {
                      @Override
                      protected boolean matchesSafely(String item) {
                        return item.matches(DATE_PATTERN);
                      }
                    }));
  }

  @Test
  public void time() throws Exception {
    this.mockMvc
        .perform(get("/time"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(
            jsonPath("$.data")
                .value(
                    new CustomTypeSafeMatcher<String>("time of format HH:mm:ss.SSS") {
                      @Override
                      protected boolean matchesSafely(String item) {
                        return item.matches(TIME_PATTERN);
                      }
                    }));
  }

  @Test
  public void now() throws Exception {
    this.mockMvc
        .perform(get("/now"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(
            jsonPath("$.data")
                .value(
                    new CustomTypeSafeMatcher<String>(
                        "dateTime of format yyyy-MM-ddTHH:mm:ss.SSS") {
                      @Override
                      protected boolean matchesSafely(String item) {
                        return item.matches(DATE_PATTERN + "T" + TIME_PATTERN);
                      }
                    }));
  }

  @Test
  public void statusTest() throws Exception {
    this.mockMvc
        .perform(get("/status"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(0))
        .andExpect(jsonPath("$.msg").value("success"))
        .andExpect(jsonPath("$.data").isEmpty());
  }
}
