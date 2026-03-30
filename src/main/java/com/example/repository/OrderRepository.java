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
    WHERE (:q = '' OR LOWER(o.customer_name) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(o.order_code) LIKE LOWER(CONCAT('%', :q, '%')))
    AND (:emptyStatus = true OR o.status IN :statusList)
""", nativeQuery = true)
    Page<Order> findAllByQuery(
            @Param("q") String q,
            @Param("statusList") List<String> statusList,
            @Param("emptyStatus") boolean emptyStatus,
            Pageable pageable
    );


}