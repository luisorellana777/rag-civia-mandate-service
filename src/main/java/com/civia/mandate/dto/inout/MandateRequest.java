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

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name is required")
    private String name;

    @NotNull(message = "Address cannot be null")
    @Size(min = 5, message = "Address is required")
    private String address;

    private String reason;

    @NotNull(message = "Request cannot be null")
    @Size(min = 10, message = "Request is required")
    private String request;

    private String observation;

    @NotNull(message = "Signature cannot be null")
    private byte[] signature;

    @NotNull(message = "Rut cannot be null")
    @Size(min = 5, max=11, message = "Rut is required")
    private String rut;

    @NotNull(message = "Phone Number cannot be null")
    @Size(min = 8, message = "Phone Number is required")
    private String phoneNumber;
}
