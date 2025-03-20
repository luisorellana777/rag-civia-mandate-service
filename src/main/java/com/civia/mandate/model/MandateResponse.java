package com.civia.mandate.model;

import lombok.Data;

@Data
public class MandateResponse {

    private String description;

    private String inferredCost;

    private String inferredBenefit;

    private String priority;
}
