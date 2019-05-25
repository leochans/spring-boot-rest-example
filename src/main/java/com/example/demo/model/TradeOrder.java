package com.example.demo.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * order entity.
 *
 * @author yuan.cheng
 */
@Data
public class TradeOrder {
    private int id;
    private String summary;
    private Float price;
    private Integer amount;
    private LocalDateTime date;
}
