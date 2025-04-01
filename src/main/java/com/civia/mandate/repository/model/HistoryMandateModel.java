package com.civia.mandate.repository.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "history_mandates")
public class HistoryMandateModel {

    @Id
    private String id;
    @EqualsAndHashCode.Include
    private String description;
    private String cost;
    private String benefit;
    private List<Double> embedding;
    private float score;
}
