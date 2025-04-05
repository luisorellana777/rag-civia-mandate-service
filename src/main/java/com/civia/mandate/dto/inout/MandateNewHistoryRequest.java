package com.civia.mandate.dto.inout;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateNewHistoryRequest {

    @NotNull(message = "Description cannot be null")
    @Size(min = 4, message = "Description is required")
    @EqualsAndHashCode.Include
    private String description;

    @NotNull(message = "Cost cannot be null")
    @Size(min = 1, message = "Cost is required")
    private String cost;

    @NotNull(message = "Benefit cannot be null")
    @Size(min = 1, message = "Benefit is required")
    private String benefit;

    @NotNull(message = "Department cannot be null")
    @Size(min = 4, message = "Department is required")
    private String department;
}
