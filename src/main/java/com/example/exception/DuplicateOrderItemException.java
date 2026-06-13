package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateOrderItemException extends RuntimeException {
  public DuplicateOrderItemException(Long variantId) {
    super("La variante " + variantId + " ya existe en la orden");
  }
}