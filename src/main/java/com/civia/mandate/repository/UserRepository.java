package com.civia.mandate.repository;

import com.civia.mandate.repository.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserModel, String> {

    boolean existsByEmail(String email);

    UserModel findByEmail(String email);
}