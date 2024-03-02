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

@Document("folders")
@Getter
@Setter
public class Folder {

    @Id
    private Integer folderId;

    @NotNull
    @Size(max = 100)
    private String folderName;

    @NotNull
    @Size(max = 50)
    private String region;

    @NotNull
    @Size(max = 50)
    private String crop;

    @DocumentReference(lazy = true, lookup = "{ 'farmerFolderMappingFolders' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<User> farmerFolderMappingUsers;

    @DocumentReference(lazy = true, lookup = "{ 'folder' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Image> folderImages;

}
