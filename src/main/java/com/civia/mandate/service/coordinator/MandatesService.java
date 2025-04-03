package com.civia.mandate.service.coordinator;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.Status;
import com.civia.mandate.dto.inout.ClusterPageResponse;
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
import java.util.Objects;

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

        List<HistoryMandateDto> historyMandatesDto = historyMandateRepository.getSimilarHistoryByNewMandates(mandatesDto);

        PromptDto promptInferencePrioritizationDto = promptCreator.createInferencePrioritizationPrompt(historyMandatesDto, mandatesDto);
        mandatesDto = geminiFlashLiteService.getModelRecommendation(promptInferencePrioritizationDto, mandatesDto);

        List<MandateDto> savedMandatesDto = mandateRepository.saveNewMandates(mandatesDto);
        return mapper.dtoToResponses(savedMandatesDto);
    }

    public MandateResponse updateMandateState(String id, Status status) {
        MandateDto mandateDto = mandateRepository.updateStatus(id, status);
        return mapper.dtoToResponse(mandateDto);
    }

    public MandatePageResponse getMandatesByStateAndDepartment(Status status, String department, int page, int size) {
        MandatePageResponse mandatesPage = mandateRepository.getMandates(status, department, page, size);
        List mandatesResponse = mapper.dtoToResponses(mandatesPage.getContent());
        mandatesPage.setContent(mandatesResponse);
        return mandatesPage;
    }

    public ClusterPageResponse getMandatesCluster(String id, Status status, String department, int similarity, int page, int size) {
        ClusterPageResponse clusterPage = mandateRepository.getCluster(id, status, department, similarity, page, size);
        if(Objects.isNull(clusterPage)) return null;
        List mandatesResponse = mapper.dtoToClusterResponses(clusterPage.getContent());
        clusterPage.setContent(mandatesResponse);
        return clusterPage;
    }
}
