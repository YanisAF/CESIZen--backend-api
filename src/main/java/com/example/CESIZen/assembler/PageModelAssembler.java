package com.example.CESIZen.assembler;

import com.example.CESIZen.controller.page.PageController;
import com.example.CESIZen.dto.page.PageDtoResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PageModelAssembler implements RepresentationModelAssembler<PageDtoResponse, EntityModel<PageDtoResponse>> {

    @Override
    public EntityModel<PageDtoResponse> toModel(PageDtoResponse page){
        String id = page.getId() != null ? page.getId().toString() : "0";
        try {
            return EntityModel.of(page,
                    linkTo(methodOn(PageController.class).getAllPages()).withRel("/get-all-pages")
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des liens HATEOAS : " + e.getMessage());
            return EntityModel.of(page);
        }
    }
}
