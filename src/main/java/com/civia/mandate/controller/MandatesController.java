package com.civia.mandate.controller;

import com.civia.mandate.dto.inout.MandateRequest;
import com.civia.mandate.dto.inout.MandateResponse;
import com.civia.mandate.service.coordinator.MandatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/mandates")
public class MandatesController {

    private MandatesService service;

    @PostMapping
    public List<MandateResponse> savePrioritize(@RequestBody List<MandateRequest> newMandateRequest) throws JsonProcessingException {
        return service.coordinatePrioritization(newMandateRequest);
    }
}
