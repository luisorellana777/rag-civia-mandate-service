package com.civia.mandate.repository;

import com.civia.mandate.dto.Status;
import com.civia.mandate.repository.model.MandateModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;


public interface MandateModelRepository extends MongoRepository<MandateModel, String> {

    boolean existsByRequest(String request);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'status': ?1 } }")
    void updateMandatesById(String id, Status status);

    Page<MandateModel> findByStatus(Status status, Pageable pageable);

}