package com.civia.mandate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateRequest {

    @NotNull(message = "Description cannot be null")
    @Size(min = 5, message = "Description is required")
    @EqualsAndHashCode.Include
    private String description;
    @NotNull(message = "Cost cannot be null")
    @Size(min = 1, message = "Cost is required")
    private String cost;
    @NotNull(message = "Benefit cannot be null")
    @Size(min = 1, message = "Benefit is required")
    private String benefit;
}
