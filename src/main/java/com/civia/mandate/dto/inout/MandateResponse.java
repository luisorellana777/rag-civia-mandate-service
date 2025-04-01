package com.civia.mandate.dto.inout;

import com.civia.mandate.dto.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateResponse {

    private String id;

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

    private String department;

    private Status status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
