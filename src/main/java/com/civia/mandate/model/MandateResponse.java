package com.civia.mandate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
public class MandateResponse {

    private String description;

    private String inferredCost;

    private String inferredBenefit;

    private String priority;

    @JsonCreator
    public MandateResponse(@JsonProperty("description") String description, @JsonProperty("inferredCost") String inferredCost, @JsonProperty("inferredBenefit") String inferredBenefit, @JsonProperty("priority") String priority) {
        this.description = description;
        this.inferredCost = inferredCost;
        this.inferredBenefit = inferredBenefit;
        this.priority = priority;
    }
}
