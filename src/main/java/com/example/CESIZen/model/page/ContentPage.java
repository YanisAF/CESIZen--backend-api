package com.example.CESIZen.model.page;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "content")
@NoArgsConstructor
@AllArgsConstructor
public class ContentPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Ce champ est obligatoire")
    @Column(length = 1000)
    private String name;

    @NotBlank(message = "Ce champ est obligatoire")
    @Column(length = 2000)
    private String description;

    @NotBlank(message = "Ce champ est obligatoire")
    @Column(length = 2000)
    private String item;

    @ManyToOne(optional = false)
    @JoinColumn(name = "page_id", nullable = false)
    @JsonBackReference
    private Page page;
}

