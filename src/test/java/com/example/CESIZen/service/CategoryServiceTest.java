package com.example.CESIZen.service;

import com.example.CESIZen.dto.category.CategoryDtoRequest;
import com.example.CESIZen.dto.category.CategoryDtoResponse;
import com.example.CESIZen.model.page.Category;
import com.example.CESIZen.projection.CategoryProjection;
import com.example.CESIZen.repository.CategoryRepository;
import com.example.CESIZen.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - CategoryService")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDtoRequest categoryRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1);
        category.setName("Bien-être");

        categoryRequest = new CategoryDtoRequest();
        categoryRequest.setName("Bien-être");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getAll()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("CAT-01 | getAll() - Retourne toutes les catégories")
    void getAll_shouldReturnAllCategories() {
        Category cat2 = new Category();
        cat2.setId(2);
        cat2.setName("Stress");

        when(categoryRepository.findAll()).thenReturn(List.of(category, cat2));

        List<Category> result = categoryService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Category::getName)
                .containsExactlyInAnyOrder("Bien-être", "Stress");
    }

    @Test
    @DisplayName("CAT-02 | getAll() - Retourne liste vide si aucune catégorie")
    void getAll_shouldReturnEmptyList_whenNone() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<Category> result = categoryService.getAll();

        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getCategoryById()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("CAT-03 | getCategoryById() - Succès : catégorie trouvée")
    void getCategoryById_shouldReturnCategory_whenFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Bien-être");
    }

    @Test
    @DisplayName("CAT-04 | getCategoryById() - Échec : catégorie introuvable")
    void getCategoryById_shouldThrow_whenNotFound() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("CAT-05 | getCategoryById() - Échec : id null")
    void getCategoryById_shouldThrow_whenIdIsNull() {
        assertThatThrownBy(() -> categoryService.getCategoryById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  saveCategory()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("CAT-06 | saveCategory() - Succès : catégorie créée")
    void saveCategory_shouldReturnDto_whenValid() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDtoResponse result = categoryService.saveCategory(categoryRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Bien-être");
        verify(categoryRepository).save(any(Category.class));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  genererRapportCategeory()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("CAT-07 | genererRapportCategeory() - Retourne bytes Excel non vides")
    void genererRapportCategeory_shouldReturnNonEmptyBytes() throws IOException {
        CategoryProjection proj = new CategoryProjection() {
            @Override
            public Integer getIndexCategory() { return 1; }
            @Override
            public String getCategoryName() { return "Bien-être"; }
        };
        when(categoryRepository.getCategoryList()).thenReturn(List.of(proj));

        byte[] result = categoryService.genererRapportCategeory();

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("CAT-08 | genererRapportCategeory() - Retourne fichier Excel même si liste vide")
    void genererRapportCategeory_shouldReturnBytes_whenEmpty() throws IOException {
        when(categoryRepository.getCategoryList()).thenReturn(List.of());

        byte[] result = categoryService.genererRapportCategeory();

        assertThat(result).isNotEmpty();
    }
}
