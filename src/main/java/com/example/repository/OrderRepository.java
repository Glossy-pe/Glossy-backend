package com.example.repository;

import com.example.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
    SELECT o.* FROM orders o
    WHERE (
        :q = ''
        OR LOWER(REPLACE(o.customer_name, ' ', ''))    LIKE LOWER(CONCAT('%', REPLACE(:q, ' ', ''), '%'))
        OR LOWER(REPLACE(o.order_code, ' ', ''))        LIKE LOWER(CONCAT('%', REPLACE(:q, ' ', ''), '%'))
        OR LOWER(REPLACE(o.customer_address, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:q, ' ', ''), '%'))
        OR CAST(o.id AS CHAR)                           LIKE CONCAT('%', :q, '%')
        OR CAST(o.total AS CHAR)                        LIKE CONCAT('%', :q, '%')
    )
    AND (:emptyStatus = true OR o.status IN :statusList)
""", nativeQuery = true)
Page<Order> findAllByQuery(
        @Param("q") String q,
        @Param("statusList") List<String> statusList,
        @Param("emptyStatus") boolean emptyStatus,
        Pageable pageable
);

@Query(value = """
    SELECT DISTINCT o.* FROM orders o
    JOIN order_item oi ON oi.order_id = o.id
    WHERE oi.product_variant_id = :variantId
    AND (:emptyStatus = true OR o.status IN :statusList)
""", nativeQuery = true)
Page<Order> findAllByVariantId(
        @Param("variantId") Long variantId,
        @Param("statusList") List<String> statusList,
        @Param("emptyStatus") boolean emptyStatus,
        Pageable pageable
);
}