package com.civia.mandate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MandateRequest {

    private String description;
    private String cost;
    private String benefit;
}
