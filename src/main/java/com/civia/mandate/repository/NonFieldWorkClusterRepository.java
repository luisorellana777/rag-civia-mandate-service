package com.civia.mandate.repository;

import com.civia.mandate.dto.Status;
import com.civia.mandate.repository.model.MandateModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.time.LocalDateTime;
import java.util.List;


public interface NonFieldWorkClusterRepository extends MandateModelRepository {

    @Aggregation(pipeline = {
            "{ $vectorSearch: { "
                    + "index: 'vector_index', "
                    + "queryVector: ?0, "
                    + "path: 'embedding', "
                    + "numCandidates: 10000, "
                    + "limit: 1000 "
                    + "} }",
            "{ $project: { _id: 1, name: 1, cost: 1, address: 1, reason: 1, request: 1, observation: 1, signature: 1, rut: 1, phoneNumber: 1, requestSummarization: 1, inferredCost: 1, inferredBenefit: 1, priority: 1, explanation: 1, department: 1, status: 1, location: 1, embedding: 1, createdAt: 1, updatedAt: 1, score: { $meta: 'vectorSearchScore' } } }",
            "{ $match: { _id: { $ne: ?1 }, score: { $gt: ?2 } } }"
    })
    Slice<MandateModel> vectorSearchAll(List<Double> queryVector, String id, float similarity, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $vectorSearch: { "
                    + "index: 'vector_index', "
                    + "queryVector: ?0, "
                    + "path: 'embedding', "
                    + "numCandidates: 10000, "
                    + "limit: 1000 "
                    + "} }",
            "{ $project: { _id: 1, name: 1, cost: 1, address: 1, reason: 1, request: 1, observation: 1, signature: 1, rut: 1, phoneNumber: 1, requestSummarization: 1, inferredCost: 1, inferredBenefit: 1, priority: 1, explanation: 1, department: 1, status: 1, location: 1, embedding: 1, createdAt: 1, updatedAt: 1, score: { $meta: 'vectorSearchScore' } } }",
            "{ $match: { _id: { $ne: ?1 }, score: { $gt: ?2 }, status: { $eq: ?3 } } }"
    })
    Slice<MandateModel> vectorSearchByStatus(List<Double> queryVector, String id, float similarity, Status status, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $vectorSearch: { "
                    + "index: 'vector_index', "
                    + "queryVector: ?0, "
                    + "path: 'embedding', "
                    + "numCandidates: 10000, "
                    + "limit: 1000 "
                    + "} }",
            "{ $project: { _id: 1, name: 1, cost: 1, address: 1, reason: 1, request: 1, observation: 1, signature: 1, rut: 1, phoneNumber: 1, requestSummarization: 1, inferredCost: 1, inferredBenefit: 1, priority: 1, explanation: 1, department: 1, status: 1, location: 1, embedding: 1, createdAt: 1, updatedAt: 1, score: { $meta: 'vectorSearchScore' } } }",
            "{ $match: { _id: { $ne: ?1 }, score: { $gt: ?2 }, department: { $eq: ?3 } } }"
    })
    Slice<MandateModel> vectorSearchByDepartment(List<Double> queryVector, String id, float similarity, String department, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $vectorSearch: { "
                    + "index: 'vector_index', "
                    + "queryVector: ?0, "
                    + "path: 'embedding', "
                    + "numCandidates: 10000, "
                    + "limit: 1000 "
                    + "} }",
            "{ $project: { _id: 1, name: 1, cost: 1, address: 1, reason: 1, request: 1, observation: 1, signature: 1, rut: 1, phoneNumber: 1, requestSummarization: 1, inferredCost: 1, inferredBenefit: 1, priority: 1, explanation: 1, department: 1, status: 1, location: 1, embedding: 1, createdAt: 1, updatedAt: 1, score: { $meta: 'vectorSearchScore' } } }",
            "{ $match: { _id: { $ne: ?1 }, score: { $gt: ?2 }, status: { $eq: ?3 }, department: { $eq: ?4 } } }"
    })
    Slice<MandateModel> vectorSearchByStatusAndDepartment(List<Double> queryVector, String id, float similarity, Status status, String department, Pageable pageable);
}