package com.civia.mandate.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistoryMandateDto {

    private String id;
    @EqualsAndHashCode.Include
    private String description;
    private String cost;
    private String benefit;
    private float score;
    private String department;
}
