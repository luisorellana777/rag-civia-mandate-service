package com.civia.mandate.service.coordinator;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.inout.MandateRequest;
import com.civia.mandate.dto.inout.LlmMandateResponse;
import com.civia.mandate.dto.PromptDto;
import com.civia.mandate.dto.inout.MandateResponse;
import com.civia.mandate.mapper.MandateMapper;
import com.civia.mandate.repository.MandateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.civia.mandate.repository.HistoryMandateRepository;
import com.civia.mandate.service.gemini.client.GeminiFlashLiteService;
import com.civia.mandate.service.prompt.PromptCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class MandatesService {

    private GeminiFlashLiteService geminiFlashLiteService;
    private PromptCreator promptCreator;
    private HistoryMandateRepository historyMandateRepository;
    private MandateRepository mandateRepository;
    private MandateMapper mapper;

    public List<MandateResponse> coordinatePrioritization(List<MandateRequest> newMandatesRequest) throws JsonProcessingException {

        List<MandateDto> mandatesDto = mapper.requestToDtoList(newMandatesRequest);
        mandatesDto = mandateRepository.getUnsavedMandates(mandatesDto);
        if(mandatesDto.isEmpty()) return List.of();
        List<HistoryMandateDto> historyMandatesDto = historyMandateRepository.getHistoryByNewMandates(mandatesDto);
        PromptDto promptDto = promptCreator.createPrompt(historyMandatesDto, newMandatesRequest);
        mandatesDto = geminiFlashLiteService.getModelRecommendation(promptDto, mandatesDto);
        List<MandateDto> savedMandatesDto = mandateRepository.saveMandates(mandatesDto);
        return mapper.dtoToResponse(savedMandatesDto);
    }

}
