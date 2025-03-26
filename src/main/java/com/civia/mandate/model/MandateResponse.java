package com.civia.mandate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MandateResponse {

    private String description;

    private String inferredCost;

    private String inferredBenefit;

    private String priority;

    private String reason;

    @JsonCreator
    public MandateResponse(@JsonProperty("description") String description, @JsonProperty("inferredCost") String inferredCost, @JsonProperty("inferredBenefit") String inferredBenefit, @JsonProperty("priority") String priority, @JsonProperty("reason") String reason) {
        this.description = description;
        this.inferredCost = inferredCost;
        this.inferredBenefit = inferredBenefit;
        this.priority = priority;
        this.reason = reason;
    }
}
