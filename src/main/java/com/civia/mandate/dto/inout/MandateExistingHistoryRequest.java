package com.civia.mandate.dto.inout;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateExistingHistoryRequest {

    @NotNull(message = "Id cannot be null")
    @Size(min = 4, message = "Id is required")
    @EqualsAndHashCode.Include
    private String id;

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
