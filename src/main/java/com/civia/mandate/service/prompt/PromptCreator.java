package com.civia.mandate.service.prompt;


import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.PromptDto;
import com.civia.mandate.dto.inout.MandateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptCreator {

    @Value("${integration.gemini.prompt.grounding-prioritization}")
    private String systemInstructionPrioritization;

    @Value("${integration.gemini.prompt.grounding-summarization}")
    private String systemInstructionSummarization;
    private static final String HISTORY_MANDATES_TOKEN = "<HISTORY_MANDATES_LIST>";

    public PromptDto createInferencePrioritizationPrompt(List<HistoryMandateDto> historyMandatesRequest, List<MandateDto> mandatesDto){

        var historyMandatesPromptStyle = historyMandatesRequest.stream().map(mandate -> "*".concat(mandate.getDescription()).concat(" Costo: ").concat(mandate.getCost()).concat("Beneficio: ").concat(mandate.getBenefit()).concat("\n")).toList();
        String systemInstructionPrompt = systemInstructionPrioritization.replaceFirst(HISTORY_MANDATES_TOKEN, String.join(" \n ", historyMandatesPromptStyle));

        var newMandatesPromptStyle = mandatesDto.stream().map(mandate -> "*".concat(mandate.getRequestSummarization())).toList();
        String contentPrompt = String.join("\n", newMandatesPromptStyle);

        return PromptDto.builder().systemInstruction(systemInstructionPrompt).content(contentPrompt).build();
    }

    public PromptDto createSummarizationPrompt(List<MandateDto> mandatesDto) {


        var newMandatesPromptStyle = mandatesDto.stream().map(mandate -> "*".concat(mandate.getRequest())).toList();
        String contentPrompt = String.join("\n", newMandatesPromptStyle);

        return PromptDto.builder().systemInstruction(systemInstructionSummarization).content(contentPrompt).build();
    }
}
