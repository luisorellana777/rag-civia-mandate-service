package com.civia.mandate.service.coordinator;

import com.civia.mandate.model.MandatePage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.civia.mandate.model.MandateRequest;
import com.civia.mandate.model.MandateResponse;
import com.civia.mandate.repository.VectorMandateRepository;
import com.civia.mandate.service.gemini.client.GeminiFlashLiteService;
import com.civia.mandate.service.prompt.PromptCreator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class StepCoordinatorService {

    private GeminiFlashLiteService geminiFlashLiteService;
    private PromptCreator promptCreator;

    private VectorMandateRepository vectorMandateRepository;

    public List<MandateResponse> coordinatePrioritization(List<MandateRequest> newMandatesRequest) throws JsonProcessingException {
        //TODO: Validate in DB what mandates are already saved, separate them, add cost and benefit to them, and mark them so they don't get processed by LLM.
        List<MandateRequest> historyMandatesRequest = vectorMandateRepository.getHistoryByNewMandates(newMandatesRequest);
        String prompt = promptCreator.createPrompt(historyMandatesRequest, newMandatesRequest);
        List<MandateResponse> modelResponse = geminiFlashLiteService.getModelRecommendation(prompt);
        //TODO: Save BD modelResponse. Validate by description if they have already been saved.
        return modelResponse;
    }

    public void saveMandates(List<MandateRequest> newMandatesRequest) throws JsonProcessingException {
        vectorMandateRepository.saveMandatesHistory(newMandatesRequest);
    }

    public MandatePage getMandates(int page, int size) {

        return vectorMandateRepository.findMandatesHistoryByPagination(page, size);
    }
}
