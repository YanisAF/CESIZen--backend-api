package com.example.CESIZen.dto.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentPageDto {
    private String name;
    private String description;
    private String itemUrl;

    @Override
    public String toString() {
        return "ContentPageDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", itemUrl='" + itemUrl + '\'' +
                '}';
    }
}
