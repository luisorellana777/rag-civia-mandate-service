package com.civia.mandate.service.prompt;


import com.civia.mandate.dto.PromptDto;
import com.civia.mandate.model.MandateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptCreator {

    @Value("${integration.gemini.system-instruction}")
    private String systemInstruction;
    private static final String HISTORY_MANDATES_TOKEN = "<HISTORY_MANDATES_LIST>";

    public PromptDto createPrompt(List<MandateRequest> historyMandatesRequest, List<MandateRequest> newMandatesRequest){

        var historyMandates = historyMandatesRequest.stream().map(mandate -> "*".concat(mandate.getDescription()).concat(" Costo: ").concat(mandate.getCost()).concat("Beneficio: ").concat(mandate.getBenefit()).concat("\n")).toList();
        String systemInstructionPrompt = systemInstruction.replaceFirst(HISTORY_MANDATES_TOKEN, String.join(" \n ", historyMandates));
        var newMandates = newMandatesRequest.stream().map(mandate -> "*".concat(mandate.getDescription())).toList();
        String contentPrompt = String.join("\n", newMandates);
        return PromptDto.builder().systemInstruction(systemInstructionPrompt).content(contentPrompt).build();
    }
}
