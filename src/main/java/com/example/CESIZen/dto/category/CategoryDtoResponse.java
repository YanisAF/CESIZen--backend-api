package com.example.CESIZen.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDtoResponse {

    private Integer id;
    private String name;

    @Override
    public String toString() {
        return "CategoryDtoResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
