package com.example.CESIZen.service.category;

import com.example.CESIZen.dto.category.CategoryDtoRequest;
import com.example.CESIZen.dto.category.CategoryDtoResponse;
import com.example.CESIZen.model.page.Category;
import com.example.CESIZen.projection.CategoryProjection;
import com.example.CESIZen.projection.CategoryProjectionImpl;
import com.example.CESIZen.repository.CategoryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public static byte[] categoryToExcel(List<CategoryProjection> category) throws IOException {
        String SHEET_NAME = "Category file";

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < CategoryProjectionImpl.getHeaderCategory().length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(CategoryProjectionImpl.getHeaderCategory()[i]);
            }

            int rowIdx = 1;
            for (CategoryProjection categoryProjection : category) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(categoryProjection.getIndexCategory());
                row.createCell(1).setCellValue(categoryProjection.getCategoryName());
            }

            for(int i = 0; i < CategoryProjectionImpl.getHeaderCategory().length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] genererRapportCategeory() throws IOException {
        List<CategoryProjection> categoryProjectionList = categoryRepository.getCategoryList();
        return categoryToExcel(categoryProjectionList);
    }

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer id){
        if (id == null) {
            throw new IllegalArgumentException("L'id de la catégorie ne peut pas être null");
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucune catégorie trouvée avec l'id " + id));
    }

    public CategoryDtoResponse saveCategory(CategoryDtoRequest categoryDtoRequest){
        Category category = new Category();
        category.setName(categoryDtoRequest.getName());
        Category categorySaved = categoryRepository.save(category);
        CategoryDtoResponse categoryDtoResponse = new CategoryDtoResponse();
        categoryDtoResponse.setId(categorySaved.getId());
        categoryDtoResponse.setName(categorySaved.getName());
        return categoryDtoResponse;
    }
}
