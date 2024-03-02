package com.lucky.web_app.repos;

import com.lucky.web_app.domain.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RolesRepository extends MongoRepository<Roles, Long> {

    boolean existsByNameIgnoreCase(String name);

}
