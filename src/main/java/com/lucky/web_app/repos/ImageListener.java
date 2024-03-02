package com.lucky.web_app.repos;

import com.lucky.web_app.domain.Image;
import com.lucky.web_app.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class ImageListener extends AbstractMongoEventListener<Image> {

    private final PrimarySequenceService primarySequenceService;

    public ImageListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Image> event) {
        if (event.getSource().getImageId() == null) {
            event.getSource().setImageId((int)primarySequenceService.getNextValue());
        }
    }

}
