package com.lucky.web_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RolesDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @RolesNameUnique
    private String name;

}
