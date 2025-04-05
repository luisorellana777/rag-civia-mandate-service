package com.civia.mandate.service.coordinator;

import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.inout.MandateExistingHistoryRequest;
import com.civia.mandate.dto.inout.MandateNewHistoryRequest;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.mapper.HistoryMandateMapper;
import com.civia.mandate.repository.HistoryMandateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class HistoryMandateService {

    private HistoryMandateRepository historyMandateRepository;
    private HistoryMandateMapper mapper;

    public void saveNewMandates(List<MandateNewHistoryRequest> mandatesHistoryRequest) throws JsonProcessingException {
        List<HistoryMandateDto> historyMandateDtos = mapper.newRequestToDtoList(mandatesHistoryRequest);
        historyMandateRepository.saveNewMandatesHistory(historyMandateDtos);
    }

    public MandatePageResponse getMandates(int page, int size) {

        return historyMandateRepository.findMandatesHistoryByPagination(page, size);
    }

    public void saveExistingMandates(List<MandateExistingHistoryRequest> historyMandateRequest) {
        List<HistoryMandateDto> historyMandateDtos = mapper.existingRequestToDtoList(historyMandateRequest);
        historyMandateRepository.saveExistingMandatesHistory(historyMandateDtos);
    }
}
