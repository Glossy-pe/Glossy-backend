package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByLabel(String label);

    @Query(value = """
    SELECT p.*
    FROM product p
    INNER JOIN product_label pl ON p.id = pl.product_id
    WHERE pl.label_id = :labelId
""", nativeQuery = true)
    List<Product> findByLabelId(@Param("labelId") Long labelId);
}
