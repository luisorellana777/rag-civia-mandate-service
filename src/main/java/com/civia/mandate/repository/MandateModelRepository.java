package com.civia.mandate.repository;

import com.civia.mandate.repository.model.MandateModel;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MandateModelRepository extends MongoRepository<MandateModel, String> {

    boolean existsByRequest(String request);
}