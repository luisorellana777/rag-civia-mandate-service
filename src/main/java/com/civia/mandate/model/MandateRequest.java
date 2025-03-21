package com.civia.mandate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateRequest {

    @EqualsAndHashCode.Include
    private String description;
    private String cost;
    private String benefit;
}
