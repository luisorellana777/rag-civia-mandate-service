package com.civia.mandate.repository;

import com.civia.mandate.dto.Status;
import com.civia.mandate.repository.model.MandateModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;

import java.util.List;


public interface FieldWorkClusterRepository extends MandateModelRepository {
//db.mandates.createIndex({ location: "2dsphere" });
    @Aggregation(pipeline = {
            "{ $search: { "
                    + "index: 'vector_geo_index', "
                    + "knnBeta: {"
                        + "path: 'embedding', "
                        + "vector: ?0, "
                        + " k: 1000, "
                        + "filter: { "
                            + "geoWithin: { "
                                + "path: 'location', "
                                + "circle: { "
                                + "center: { type: 'Point', coordinates: [ ?1, ?2 ] }, "
                                + "radius: ?3 "
                                + "}}}}}}",
            "{ $project: { _id: 1, name: 1, cost: 1, address: 1, reason: 1, request: 1, observation: 1, signature: 1, rut: 1, phoneNumber: 1, requestSummarization: 1, inferredCost: 1, inferredBenefit: 1, priority: 1, explanation: 1, department: 1, status: 1, location: 1, embedding: 1, createdAt: 1, updatedAt: 1, score: { $meta: 'searchScore' } } }",
            "{ $match: { _id: { $ne: ?4 }, score: { $gt: ?5 } } }"
    })
    Slice<MandateModel> vectorSearchAll(List<Double> queryVector, Double latitude, Double longitude, int kilometers, String id, float similarity, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $search: { "
                    + "index: 'vector_geo_index', "
                    + "knnBeta: {"
                    + "path: 'embedding', "
                    + "vector: ?0, "
                    + " k: 1000, "
                    + "filter: { "
                    + "geoWithin: { "
                    + "path: 'location', "
                    + "circle: { "
                    + "center: { type: 'Point', coordinates: [ ?1, ?2 ] }, "
                    + "radius: ?3 "
                    + "}}}}}}",
            "{ $project: { _id: 1, name: 1, cost: 1, address: 1, reason: 1, request: 1, observation: 1, signature: 1, rut: 1, phoneNumber: 1, requestSummarization: 1, inferredCost: 1, inferredBenefit: 1, priority: 1, explanation: 1, department: 1, status: 1, location: 1, embedding: 1, createdAt: 1, updatedAt: 1, score: { $meta: 'searchScore' } } }",
            "{ $match: { _id: { $ne: ?4 }, score: { $gt: ?5 }, status: { $eq: ?6 } } }"
    })
    Slice<MandateModel> vectorSearchByStatus(List<Double> queryVector, Double latitude, Double longitude, int kilometers, String id, float similarity, Status status, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $search: { "
                    + "index: 'vector_geo_index', "
                    + "knnBeta: {"
                    + "path: 'embedding', "
                    + "vector: ?0, "
                    + " k: 1000, "
                    + "filter: { "
                    + "geoWithin: { "
                    + "path: 'location', "
                    + "circle: { "
                    + "center: { type: 'Point', coordinates: [ ?1, ?2 ] }, "
                    + "radius: ?3 "
                    + "}}}}}}",
            "{ $project: { _id: 1, name: 1, cost: 1, address: 1, reason: 1, request: 1, observation: 1, signature: 1, rut: 1, phoneNumber: 1, requestSummarization: 1, inferredCost: 1, inferredBenefit: 1, priority: 1, explanation: 1, department: 1, status: 1, location: 1, embedding: 1, createdAt: 1, updatedAt: 1, score: { $meta: 'searchScore' } } }",
            "{ $match: { _id: { $ne: ?4 }, score: { $gt: ?5 }, department: { $eq: ?6 } } }"
    })
    Slice<MandateModel> vectorSearchByDepartment(List<Double> queryVector, Double latitude, Double longitude, int kilometers, String id, float similarity, String department, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $search: { "
                    + "index: 'vector_geo_index', "
                    + "knnBeta: {"
                    + "path: 'embedding', "
                    + "vector: ?0, "
                    + " k: 1000, "
                    + "filter: { "
                    + "geoWithin: { "
                    + "path: 'location', "
                    + "circle: { "
                    + "center: { type: 'Point', coordinates: [ ?1, ?2 ] }, "
                    + "radius: ?3 "
                    + "}}}}}}",
            "{ $project: { _id: 1, name: 1, cost: 1, address: 1, reason: 1, request: 1, observation: 1, signature: 1, rut: 1, phoneNumber: 1, requestSummarization: 1, inferredCost: 1, inferredBenefit: 1, priority: 1, explanation: 1, department: 1, status: 1, location: 1, embedding: 1, createdAt: 1, updatedAt: 1, score: { $meta: 'searchScore' } } }",
            "{ $match: { _id: { $ne: ?4 }, score: { $gt: ?5 }, status: { $eq: ?6 }, department: { $eq: ?7 } } }"
    })
    Slice<MandateModel> vectorSearchByStatusAndDepartment(List<Double> queryVector, Double latitude, Double longitude, int kilometers, String id, float similarity, Status status, String department, Pageable pageable);
}