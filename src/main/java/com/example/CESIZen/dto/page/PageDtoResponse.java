package com.example.CESIZen.dto.page;

import com.example.CESIZen.dto.category.CategoryDtoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDtoResponse {

    private Integer id;
    private String title;
    private List<ContentPageDto> content;
    private String imageUrl;
    private CategoryDtoResponse category;

    @Override
    public String toString() {
        return "PageDtoResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content=" + content +
                ", category=" + category +
                '}';
    }
}
