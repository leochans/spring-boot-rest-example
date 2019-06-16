package com.example.demo.exception;

import lombok.Getter;

/**
 * demo exception.
 *
 * @author yuan.cheng
 */
public class DemoException extends Exception {
  @Getter private final int errorCode;
  @Getter private final String errorMsg;

  /**
   * Constructs a new runtime exception with {@code null} as its detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   */
  public DemoException(int errorCode, String errorMsg) {
    super(errorMsg);
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }
}
