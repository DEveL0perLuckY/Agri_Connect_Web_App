package com.lucky.web_app.repos;

import com.lucky.web_app.domain.Folder;
import com.lucky.web_app.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class FolderListener extends AbstractMongoEventListener<Folder> {

    private final PrimarySequenceService primarySequenceService;

    public FolderListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Folder> event) {
        if (event.getSource().getFolderId() == null) {
            event.getSource().setFolderId((int)primarySequenceService.getNextValue());
        }
    }

}
