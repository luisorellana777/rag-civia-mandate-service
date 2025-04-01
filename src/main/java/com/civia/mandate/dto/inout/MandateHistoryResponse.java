package com.civia.mandate.dto.inout;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MandateHistoryResponse {

    private String id;
    private String description;

    private String inferredCost;

    private String inferredBenefit;

    private String inferredDepartment;
}
