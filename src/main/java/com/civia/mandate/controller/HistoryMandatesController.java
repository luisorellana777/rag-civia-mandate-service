package com.civia.mandate.controller;

import com.civia.mandate.dto.inout.MandateExistingHistoryRequest;
import com.civia.mandate.dto.inout.MandateNewHistoryRequest;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.service.coordinator.HistoryMandateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/mandates/history")
public class HistoryMandatesController {

    private HistoryMandateService service;

    @GetMapping
    public ResponseEntity<MandatePageResponse> getHistoryMandates(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {

        MandatePageResponse pagesMandates = service.getMandates(page, size);
        return new ResponseEntity<>(pagesMandates, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity saveNewHistoryMandates(@Valid @RequestBody List<MandateNewHistoryRequest> historyMandateRequest) throws JsonProcessingException {

        service.saveNewMandates(historyMandateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/from-existing")
    public ResponseEntity saveExistingHistoryMandates(@Valid @RequestBody List<MandateExistingHistoryRequest> historyMandateRequest) throws JsonProcessingException {

        service.saveExistingMandates(historyMandateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
