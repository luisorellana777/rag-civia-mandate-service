package com.civia.mandate.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder(toBuilder=true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateDto {

    private String id;

    private String name;

    private String address;

    private String reason;

    @EqualsAndHashCode.Include
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

    private Status status;
}
