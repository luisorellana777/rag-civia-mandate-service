package com.civia.mandate.repository.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "history_mandates")
public class HistoryMandate {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String description;
    private String cost;
    private String benefit;
    private String priority;
    private List<Double> embedding;
}
