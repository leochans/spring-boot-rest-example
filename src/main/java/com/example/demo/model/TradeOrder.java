package com.example.demo.model;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * order entity.
 *
 * @author yuan.cheng
 */
@Data
public class TradeOrder {
  private int id;
  @NotNull private String summary;
  @PositiveOrZero private Float price;
  @Positive private Integer amount;
  private LocalDateTime date;
  @NotNull private OrderType type;
}
