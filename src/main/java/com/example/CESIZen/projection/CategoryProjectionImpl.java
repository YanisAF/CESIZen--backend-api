package com.example.CESIZen.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryProjectionImpl implements CategoryProjection{
    private Integer indexCategory;
    private String categoryName;

    public static String[] getHeaderCategory(){
        return new String[] {
                "CODE_CATEGORY",
                "CATEGORY_NAME"
        };
    }
}
