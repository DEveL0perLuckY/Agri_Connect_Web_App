package com.lucky.web_app.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDTO {

    private Integer imageId;
    private String description;
    private LocalDateTime uploadDate;
    private Integer farmer;
    private Integer folder;
    private boolean hasImage;

}
