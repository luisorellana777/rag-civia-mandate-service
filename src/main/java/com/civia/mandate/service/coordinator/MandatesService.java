package com.civia.mandate.service.coordinator;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.Status;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.dto.inout.MandateRequest;
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

    public List<MandateResponse> saveNewMandate(List<MandateRequest> newMandatesRequest) throws JsonProcessingException {

        List<MandateDto> mandatesDto = mapper.requestToDtoList(newMandatesRequest);
        mandatesDto = mandateRepository.getUnsavedMandates(mandatesDto);
        if(mandatesDto.isEmpty()) return List.of();
        PromptDto promptSummarizationDto = promptCreator.createSummarizationPrompt(mandatesDto);
        mandatesDto = geminiFlashLiteService.getRequestSummarization(promptSummarizationDto, mandatesDto);

        List<HistoryMandateDto> historyMandatesDto = historyMandateRepository.getHistoryByNewMandates(mandatesDto);

        PromptDto promptInferencePrioritizationDto = promptCreator.createInferencePrioritizationPrompt(historyMandatesDto, mandatesDto);
        mandatesDto = geminiFlashLiteService.getModelRecommendation(promptInferencePrioritizationDto, mandatesDto);

        List<MandateDto> savedMandatesDto = mandateRepository.saveNewMandates(mandatesDto);
        return mapper.dtoToResponses(savedMandatesDto);
    }

    public MandatePageResponse getMandates(int page, int size) {
        return mandateRepository.findMandatesByPagination(page, size);
    }

    public MandateResponse updateMandateState(String id, Status status) {
        MandateDto mandateDto = mandateRepository.updateStatus(id, status);
        return mapper.dtoToResponse(mandateDto);
    }

    public MandatePageResponse getMandatesByState(Status status, int page, int size) {
        return mandateRepository.getMandates(status, page, size);
    }
}
