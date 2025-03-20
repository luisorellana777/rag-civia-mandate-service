package com.civia.mandate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.civia.mandate.model.MandateRequest;
import com.civia.mandate.model.MandateResponse;
import com.civia.mandate.service.coordinator.StepCoordinatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MandateProcessorController {

    private StepCoordinatorService service;

    @PostMapping(value = "/prioritize/mandates")
    public List<MandateResponse> prioritize(@RequestBody List<MandateRequest> newMandateRequest) throws JsonProcessingException {
        return service.coordinatePrioritization(newMandateRequest);
    }

    @PutMapping(value = "/history/mandates")
    public ResponseEntity saveMandates(@RequestBody List<MandateRequest> newMandateRequest) throws JsonProcessingException {

        service.saveMandates(newMandateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
