package com.civia.mandate.service.prompt;


import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.PromptDto;
import com.civia.mandate.dto.inout.MandateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptCreator {

    @Value("${integration.gemini.system-instruction}")
    private String systemInstruction;
    private static final String HISTORY_MANDATES_TOKEN = "<HISTORY_MANDATES_LIST>";

    public PromptDto createPrompt(List<HistoryMandateDto> historyMandatesRequest, List<MandateRequest> newMandatesRequest){

        var historyMandatesPromptStyle = historyMandatesRequest.stream().map(mandate -> "*".concat(mandate.getDescription()).concat(" Costo: ").concat(mandate.getCost()).concat("Beneficio: ").concat(mandate.getBenefit()).concat("\n")).toList();
        String systemInstructionPrompt = systemInstruction.replaceFirst(HISTORY_MANDATES_TOKEN, String.join(" \n ", historyMandatesPromptStyle));

        var newMandatesPromptStyle = newMandatesRequest.stream().map(mandate -> "*".concat(mandate.getRequest())).toList();
        String contentPrompt = String.join("\n", newMandatesPromptStyle);

        return PromptDto.builder().systemInstruction(systemInstructionPrompt).content(contentPrompt).build();
    }
}
