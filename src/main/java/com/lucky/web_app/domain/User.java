package com.lucky.web_app.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document("users")
@Getter
@Setter
public class User {

    @Id
    private Integer userId;

    @NotNull
    @Size(max = 50)
    private String username;

    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 100)
    private String password;

    @DocumentReference(lazy = true)
    private Set<Folder> farmerFolderMappingFolders;

    @DocumentReference(lazy = true, lookup = "{ 'farmer' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Image> farmerImages;

    @DocumentReference(lazy = true)
    private Set<Roles> roleId;

}
