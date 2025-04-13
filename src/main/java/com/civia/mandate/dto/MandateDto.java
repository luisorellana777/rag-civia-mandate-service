package com.civia.mandate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

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

    private String department;

    private Boolean fieldWork;

    private Location location;

    private Status status;

    private Float centroidProximity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @Data
    @Builder(toBuilder=true)
    public static class Location {

        private String type;

        private List<Double> coordinates;
    }
}
