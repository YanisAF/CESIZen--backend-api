package com.example.CESIZen.service;

import com.example.CESIZen.dto.page.PageDtoRequest;
import com.example.CESIZen.dto.page.PageDtoResponse;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.mapper.PageMapper;
import com.example.CESIZen.model.page.Category;
import com.example.CESIZen.model.page.ContentPage;
import com.example.CESIZen.model.page.Page;
import com.example.CESIZen.repository.CategoryRepository;
import com.example.CESIZen.repository.PageRepository;
import com.example.CESIZen.service.page.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - PageService")
class PageServiceTest {

    @Mock
    private PageRepository pageRepository;
    @Mock
    private PageMapper pageMapper;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PageService pageService;

    private Page page;
    private Category category;
    private PageDtoRequest pageRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1);
        category.setName("Bien-être");

        page = new Page();
        page.setId(1);
        page.setTitle("Gestion du stress");
        page.setCategory(category);
        page.setContent(new ArrayList<>());

        pageRequest = new PageDtoRequest();
        pageRequest.setTitle("Gestion du stress");
        pageRequest.setCategory(1);
        pageRequest.setContent(new ArrayList<>());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getPageById()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("PG-01 | getPageById() - Succès : page trouvée")
    void getPageById_shouldReturnDto_whenFound() {
        when(pageRepository.findById(1)).thenReturn(Optional.of(page));

        PageDtoResponse result = pageService.getPageById(1);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Gestion du stress");
    }

    @Test
    @DisplayName("PG-02 | getPageById() - Retourne null si page inexistante")
    void getPageById_shouldReturnNull_whenNotFound() {
        when(pageRepository.findById(99)).thenReturn(Optional.empty());

        PageDtoResponse result = pageService.getPageById(99);

        assertThat(result).isNull();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getAllPages()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("PG-03 | getAllPages() - Retourne toutes les pages")
    void getAllPages_shouldReturnAll() {
        Page page2 = new Page();
        page2.setId(2);
        page2.setTitle("Méditation");
        page2.setContent(new ArrayList<>());

        when(pageRepository.findAll()).thenReturn(List.of(page, page2));

        List<PageDtoResponse> result = pageService.getAllPages();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(PageDtoResponse::getTitle)
                .containsExactlyInAnyOrder("Gestion du stress", "Méditation");
    }

    @Test
    @DisplayName("PG-04 | getAllPages() - Liste vide si aucune page")
    void getAllPages_shouldReturnEmpty_whenNone() {
        when(pageRepository.findAll()).thenReturn(List.of());

        List<PageDtoResponse> result = pageService.getAllPages();

        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  createPage()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("PG-05 | createPage() - Succès : page créée sans image")
    void createPage_shouldSavePage_withoutImage() throws IOException {
        when(pageMapper.toPage(any(PageDtoRequest.class), isNull())).thenReturn(page);
        when(pageRepository.save(any(Page.class))).thenReturn(page);

        assertThatNoException().isThrownBy(() -> pageService.createPage(pageRequest, null));

        verify(pageRepository).save(any(Page.class));
    }

    @Test
    @DisplayName("PG-06 | createPage() - Succès : page créée avec image")
    void createPage_shouldSavePage_withImage() throws IOException {
        MockMultipartFile image = new MockMultipartFile(
                "image", "banner.jpg", "image/jpeg", "fake-image-bytes".getBytes()
        );
        when(pageMapper.toPage(any(PageDtoRequest.class), any())).thenReturn(page);
        when(pageRepository.save(any(Page.class))).thenReturn(page);

        assertThatNoException().isThrownBy(() -> pageService.createPage(pageRequest, image));

        verify(pageRepository).save(any(Page.class));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  updatePage()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("PG-07 | updatePage() - Succès : titre mis à jour")
    void updatePage_shouldUpdateTitle() throws Exception {
        pageRequest.setTitle("Nouveau titre");

        when(pageRepository.findById(1)).thenReturn(Optional.of(page));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(pageRepository.save(any(Page.class))).thenReturn(page);

        PageDtoResponse result = pageService.updatePage(1, pageRequest, null);

        assertThat(result).isNotNull();
        verify(pageRepository).save(any(Page.class));
    }

    @Test
    @DisplayName("PG-08 | updatePage() - Échec : page introuvable")
    void updatePage_shouldThrow_whenPageNotFound() {
        when(pageRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pageService.updatePage(99, pageRequest, null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("PG-09 | updatePage() - Échec : catégorie introuvable")
    void updatePage_shouldThrow_whenCategoryNotFound() {
        pageRequest.setCategory(999);

        when(pageRepository.findById(1)).thenReturn(Optional.of(page));
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pageService.updatePage(1, pageRequest, null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("PG-10 | updatePage() - Succès : contenu de page mis à jour")
    void updatePage_shouldReplaceContent() throws Exception {
        com.example.CESIZen.dto.page.ContentPageDto contentDto = new com.example.CESIZen.dto.page.ContentPageDto();
        contentDto.setName("Section 1");
        contentDto.setDescription("Description de la section 1");
        pageRequest.setContent(List.of(contentDto));

        when(pageRepository.findById(1)).thenReturn(Optional.of(page));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(pageRepository.save(any(Page.class))).thenReturn(page);

        assertThatNoException().isThrownBy(() -> pageService.updatePage(1, pageRequest, null));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  deleteById()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("PG-11 | deleteById() - Succès : page supprimée")
    void deleteById_shouldCallRepository() {
        doNothing().when(pageRepository).deleteById(1);

        assertThatNoException().isThrownBy(() -> pageService.deleteById(1));

        verify(pageRepository).deleteById(1);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getPageWithTitle()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("PG-12 | getPageWithTitle() - Succès : page trouvée par titre")
    void getPageWithTitle_shouldReturnDto() {
        when(pageRepository.findByTitle("Gestion du stress")).thenReturn(page);

        PageDtoResponse result = pageService.getPageWithTitle("Gestion du stress");

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Gestion du stress");
    }
}
