package com.civia.mandate.service.coordinator;

import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.inout.MandateHistoryRequest;
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

    public void saveMandates(List<MandateHistoryRequest> mandatesHistoryRequest) throws JsonProcessingException {
        List<HistoryMandateDto> historyMandateDtos = mapper.requestToDtoList(mandatesHistoryRequest);
        historyMandateRepository.saveMandatesHistory(historyMandateDtos);
    }

    public MandatePageResponse getMandates(int page, int size) {

        return historyMandateRepository.findMandatesHistoryByPagination(page, size);
    }
}
