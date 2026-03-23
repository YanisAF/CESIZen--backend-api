package com.example.CESIZen.repository;

import com.example.CESIZen.model.page.Category;
import com.example.CESIZen.projection.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category getCategoryByName(String name);

    @Query(value = """
        SELECT id AS id, name AS categoryName, COALESCE(id, 0) AS indexCategory
        FROM Category
        """, nativeQuery = true)
    List<CategoryProjection> getCategoryList();
}
