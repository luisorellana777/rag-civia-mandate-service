package com.civia.mandate.dto.inout;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LlmInferringResponse {

    @EqualsAndHashCode.Include
    private String request;

    private String inferredCost;

    private String inferredBenefit;

    private String priority;

    private String explanation;

    @JsonCreator
    public LlmInferringResponse(@JsonProperty("request") String request, @JsonProperty("inferredCost") String inferredCost, @JsonProperty("inferredBenefit") String inferredBenefit, @JsonProperty("priority") String priority, @JsonProperty("explanation") String explanation) {
        this.request = request;
        this.inferredCost = inferredCost;
        this.inferredBenefit = inferredBenefit;
        this.priority = priority;
        this.explanation = explanation;
    }
}
