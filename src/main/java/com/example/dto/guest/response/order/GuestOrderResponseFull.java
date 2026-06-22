package com.example.dto.guest.response.order;

import com.example.dto.guest.response.auditable.GuestAuditableResponse;
import com.example.dto.guest.response.order_item.GuestOrderItemResponseFull;
import com.example.dto.guest.response.order_status.GuestOrderStatusResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Setter
@Getter
public class GuestOrderResponseFull extends GuestAuditableResponse {

    private Long id;

    private String customerName;

    private String customerAddress;

    private String description;

    private String orderCode;

    private String publicToken;

    private LocalDateTime expiresAt;

    private Long orderStatusId;

    private GuestOrderStatusResponse orderStatus;

    private Boolean packed;

    private Boolean separated;

    private Boolean paid;

    private BigDecimal costTotal;

    private BigDecimal total;

    private List<GuestOrderItemResponseFull> items;
}
