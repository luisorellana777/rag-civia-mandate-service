package com.civia.mandate.dto.inout;

import com.civia.mandate.dto.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MandateResponse {

    protected String id;

    protected String name;

    protected String address;

    protected String reason;

    protected String request;

    protected String observation;

    protected byte[] signature;

    protected String rut;

    protected String phoneNumber;

    protected String requestSummarization;

    protected String inferredCost;

    protected String inferredBenefit;

    protected String priority;

    protected String explanation;

    protected String department;

    private Boolean fieldWork;

    private Location location;

    protected Status status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    protected LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    protected LocalDateTime updatedAt;

    @Data
    @Builder(toBuilder=true)
    public static class Location {

        private List<Double> coordinates;
    }
}
