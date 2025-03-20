package com.civia.mandate.repository;

import com.civia.mandate.repository.model.HistoryMandate;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryMandateRepository extends MongoRepository<HistoryMandate, String> {

    @Aggregation(pipeline = {
            "{ $vectorSearch: { "
                    + "index: vector_index, "
                    + "queryVector: ?0, "
                    + "path: 'embedding', "
                    + "numCandidates: 100, "
                    + "limit: 3, "
                    + "index: 'vector_index' }}",
            "{ $project: { _id: 1, description: 1, cost: 1, benefit: 1, score: { $meta: 'vectorSearchScore' } }}"
    })
    List<HistoryMandate> vectorSearch(List<Double> queryVector);

    boolean existsByDescription(String description);
}