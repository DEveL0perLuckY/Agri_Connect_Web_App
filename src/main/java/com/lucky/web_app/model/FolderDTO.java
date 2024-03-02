package com.lucky.web_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderDTO {

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

}
