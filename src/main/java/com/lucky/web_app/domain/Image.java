package com.lucky.web_app.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("images")
@Getter
@Setter
public class Image {

    @Id
    private Integer imageId;

    private String description;

    private LocalDateTime uploadDate;

    private byte[] postImage;

    @DocumentReference(lazy = true)
    private User farmer;

    @DocumentReference(lazy = true)
    private Folder folder;

}
