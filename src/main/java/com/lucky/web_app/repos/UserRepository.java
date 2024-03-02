package com.lucky.web_app.repos;

import com.lucky.web_app.domain.Folder;
import com.lucky.web_app.domain.Roles;
import com.lucky.web_app.domain.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {

    User findFirstByFarmerFolderMappingFolders(Folder folder);

    User findFirstByRoleId(Roles roles);

    List<User> findAllByFarmerFolderMappingFolders(Folder folder);

    List<User> findAllByRoleId(Roles roles);

    Optional<User> findByEmail(String email);
}
