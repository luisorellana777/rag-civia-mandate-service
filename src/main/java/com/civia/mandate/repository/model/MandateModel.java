package com.civia.mandate.repository.model;

import com.civia.mandate.dto.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "mandates")
public class MandateModel {

    @Id
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

    private List<Double> embedding;

    private Float score;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Data
    @Builder(toBuilder=true)
    public static class Location {

        private String type;

        private List<Double> coordinates;
    }
}
