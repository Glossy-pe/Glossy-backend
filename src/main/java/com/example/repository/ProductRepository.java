package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByLabel(String label);

    @Query(value = """
        SELECT p.*
        FROM product p
        INNER JOIN product_label pl ON p.id = pl.product_id
        WHERE pl.label_id = :labelId
    """, nativeQuery = true)
    List<Product> findByLabelId(@Param("labelId") Long labelId);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByProductLabels_Label_Id(Long labelId, Pageable pageable);

    Optional<Product> findBySlug(String slug);
}
