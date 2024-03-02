package com.lucky.web_app.repos;

import com.lucky.web_app.domain.Folder;
import com.lucky.web_app.domain.Image;
import com.lucky.web_app.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ImageRepository extends MongoRepository<Image, Integer> {

    Image findFirstByFarmer(User user);

    Image findFirstByFolder(Folder folder);

}
