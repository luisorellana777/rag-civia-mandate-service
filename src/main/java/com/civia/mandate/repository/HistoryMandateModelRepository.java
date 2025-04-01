package com.civia.mandate.repository;

import com.civia.mandate.repository.model.HistoryMandateModel;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryMandateModelRepository extends MongoRepository<HistoryMandateModel, String> {

    @Aggregation(pipeline = {
            "{ $vectorSearch: { "
                    + "index: vector_index, "
                    + "queryVector: ?0, "
                    + "path: 'embedding', "
                    + "numCandidates: 100, "
                    + "limit: 3, "
                    + "index: 'vector_index' }}",
            "{ $project: { _id: 1, description: 1, cost: 1, benefit: 1, department: 1, score: { $meta: 'vectorSearchScore' } }}"
    })
    List<HistoryMandateModel> vectorSearch(List<Double> queryVector);

    boolean existsByDescription(String description);
}