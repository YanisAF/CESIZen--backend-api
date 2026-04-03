package com.example.CESIZen.assembler;

import com.example.CESIZen.controller.user.UserController;
import com.example.CESIZen.dto.user.UserDtoRequest;
import com.example.CESIZen.dto.user.UserDtoResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDtoResponse, EntityModel<UserDtoResponse>> {

    @Override
    public EntityModel<UserDtoResponse> toModel(UserDtoResponse user){
        String id = user.getId() != null ? user.getId().toString() : "0";
        try {
            return EntityModel.of(user,
                    linkTo(methodOn(UserController.class).getAllUsers()).withRel("/users-list"),
                    linkTo(methodOn(UserController.class).getAllUsersWithFilter()).withRel("/filter-users-list"),
                    linkTo(methodOn(UserController.class).createUserUrl(new UserDtoRequest(
                            id,
                            user.getUsername() != null ? user.getUsername() : "",
                            user.getEmail() != null ? user.getEmail() : "",
                            user.getRole() != null ? user.getRole() : null
                    ))).withRel("create-user")
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des liens HATEOAS : " + e.getMessage());
            return EntityModel.of(user);
        }
    }
}