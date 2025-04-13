package com.civia.mandate.dto.inout;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateRequest {

    private String name;

    @NotNull(message = "Address cannot be null")
    @Size(min = 5, message = "Address is required")
    private String address;

    private String reason;

    @NotNull(message = "Request cannot be null")
    @Size(min = 10, message = "Request is required")
    private String request;

    private String observation;

    private byte[] signature;

    @NotNull(message = "Rut cannot be null")
    @Size(min = 5, max=14, message = "Rut is required")
    private String rut;

    private String phoneNumber;
}
