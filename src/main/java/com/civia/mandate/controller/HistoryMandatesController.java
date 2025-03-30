package com.civia.mandate.controller;

import com.civia.mandate.dto.inout.MandateHistoryRequest;
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

    @PostMapping
    public ResponseEntity saveMandates(@Valid @RequestBody List<MandateHistoryRequest> historyMandateRequest) throws JsonProcessingException {

        service.saveMandates(historyMandateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<MandatePageResponse> getMandates(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {

        MandatePageResponse pagesMandates = service.getMandates(page, size);
        return new ResponseEntity<>(pagesMandates, HttpStatus.OK);
    }
}
