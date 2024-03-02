package com.lucky.web_app.repos;

import com.lucky.web_app.domain.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface FolderRepository extends MongoRepository<Folder, Integer> {
}
