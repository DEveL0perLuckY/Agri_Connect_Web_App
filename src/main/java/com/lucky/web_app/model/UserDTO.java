package com.lucky.web_app.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Integer userId;

    @NotNull
    @Size(max = 50)
    @NotEmpty(message = "Please enter valid name.")
    private String username;

    @Size(max = 255)
    @Email
    @NotEmpty(message = "Please enter valid email.")
    private String email;

    @NotNull
    @Size(max = 100)
    @NotEmpty(message = "Please enter valid password.")
    private String password;

    private List<Integer> farmerFolderMappingFolders;

    private List<Long> roleId;

}
