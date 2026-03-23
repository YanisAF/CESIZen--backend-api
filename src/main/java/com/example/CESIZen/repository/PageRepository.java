package com.example.CESIZen.repository;

import com.example.CESIZen.model.page.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Integer> {
    boolean existsByTitle(String title);
    Page findByTitle(String title);
}
