package com.civia.mandate.controller;

import com.civia.mandate.model.MandatePage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.civia.mandate.model.MandateRequest;
import com.civia.mandate.model.MandateResponse;
import com.civia.mandate.service.coordinator.StepCoordinatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity saveMandates(@Valid @RequestBody List<MandateRequest> newMandateRequest) throws JsonProcessingException {

        service.saveMandates(newMandateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/history/mandates")
    public ResponseEntity<MandatePage> getMandates(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {

        MandatePage pagesMandates = service.getMandates(page, size);
        return new ResponseEntity<>(pagesMandates, HttpStatus.OK);
    }
}
