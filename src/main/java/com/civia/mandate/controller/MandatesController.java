package com.civia.mandate.controller;

import com.civia.mandate.dto.Status;
import com.civia.mandate.dto.inout.ClusterPageResponse;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.dto.inout.MandateRequest;
import com.civia.mandate.dto.inout.MandateResponse;
import com.civia.mandate.service.coordinator.MandatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/mandates")
public class MandatesController {

    private MandatesService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'OFFICER', 'CLERK')")
    public ResponseEntity<List<MandateResponse>> savePrioritize(@Valid @RequestBody List<MandateRequest> newMandateRequest) throws JsonProcessingException {
        List<MandateResponse> mandateResponses = service.saveNewMandate(newMandateRequest);
        HttpStatus statusResult = !mandateResponses.isEmpty() ? HttpStatus.OK : HttpStatus.ALREADY_REPORTED;
        return new ResponseEntity<>(mandateResponses, statusResult);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'OFFICER', 'INSPECTOR')")
    public ResponseEntity<MandateResponse> updateMandateState(@PathVariable String id, @RequestParam Status status) throws JsonProcessingException {
        MandateResponse updatedMandate = service.updateMandateState(id, status);
        HttpStatus statusResult = Objects.nonNull(updatedMandate.getId()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        MandateResponse updatedMandateResult = Objects.nonNull(updatedMandate.getId()) ? updatedMandate : null;
        return new ResponseEntity<>(updatedMandateResult, statusResult);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'OFFICER', 'INSPECTOR','CLERK')")
    public ResponseEntity<MandatePageResponse> getMandatesByStateAndDepartmentAndRut(@RequestParam(name = "status", required = false) Status status, @RequestParam(name = "department", required = false) String department, @RequestParam(name = "rut", required = false) String rut, @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        MandatePageResponse pagesMandates = service.getMandatesByStateAndDepartment(status, department,rut, page, size);
        return new ResponseEntity<>(pagesMandates, HttpStatus.OK);
    }

    @GetMapping("/{id}/cluster")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'OFFICER')")
    public ResponseEntity<ClusterPageResponse> getMandatesCluster(@PathVariable String id, @RequestParam(name = "status", required = false) Status status, @RequestParam(name = "department", required = false) String department, @RequestParam(name = "kilometers", required = false, defaultValue = "0") int kilometers, @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        ClusterPageResponse pagesMandates = service.getMandatesCluster(id, status, department, kilometers, page, size);
        return new ResponseEntity<>(pagesMandates, Objects.nonNull(pagesMandates) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
