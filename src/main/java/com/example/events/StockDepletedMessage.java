package com.example.events;

public record StockDepletedMessage(Long variantId, String toneName, String timestamp) {}

