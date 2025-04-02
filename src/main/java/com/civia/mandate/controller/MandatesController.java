package com.civia.mandate.controller;

import com.civia.mandate.dto.Status;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.dto.inout.MandateRequest;
import com.civia.mandate.dto.inout.MandateResponse;
import com.civia.mandate.service.coordinator.MandatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/mandates")
public class MandatesController {

    private MandatesService service;

    @PostMapping
    public List<MandateResponse> savePrioritize(@Valid @RequestBody List<MandateRequest> newMandateRequest) throws JsonProcessingException {
        return service.saveNewMandate(newMandateRequest);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<MandateResponse> updateMandateState(@PathVariable String id, @RequestParam Status status) throws JsonProcessingException {
        MandateResponse updatedMandate = service.updateMandateState(id, status);
        HttpStatus statusResult = Objects.nonNull(updatedMandate.getId()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        MandateResponse updatedMandateResult = Objects.nonNull(updatedMandate.getId()) ? updatedMandate : null;
        return new ResponseEntity<>(updatedMandateResult, statusResult);
    }

    @GetMapping
    public ResponseEntity<MandatePageResponse> getMandatesByStateAndDepartment(@RequestParam(name = "status", required = false) Status status, @RequestParam(name = "department", required = false) String department, @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        MandatePageResponse pagesMandates = service.getMandatesByStateAndDepartment(status, department, page, size);
        return new ResponseEntity<>(pagesMandates, HttpStatus.OK);
    }
}
