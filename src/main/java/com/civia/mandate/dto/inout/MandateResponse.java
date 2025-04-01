package com.civia.mandate.dto.inout;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateResponse {

    private String name;

    private String address;

    private String reason;

    private String request;

    private String observation;

    private byte[] signature;

    private String rut;

    private String phoneNumber;

    private String requestSummarization;

    private String inferredCost;

    private String inferredBenefit;

    private String priority;

    private String explanation;
}
