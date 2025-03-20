package com.civia.mandate.service.prompt;


import com.civia.mandate.model.MandateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptCreator {

    @Value("${integration.gemini.prompt-model}")
    private String promptModel;
    private static final String HISTORY_MANDATES_TOKEN = "<HISTORY_MANDATES_LIST>";
    private static final String NEW_MANDATES_TOKEN = "<NEW_MANDATES_LIST>";

    public String createPrompt(List<MandateRequest> historyMandatesRequest, List<MandateRequest> newMandatesRequest){

        var historyMandates = historyMandatesRequest.stream().map(mandate -> " \n *Descripción de la tarea: ".concat(mandate.getDescription()).concat(" \n Costo (numérico; cuanto menor, mejor): ").concat(mandate.getCost()).concat(" \n Puntaje de beneficio (numérico; cuanto mayor, mejor): ").concat(mandate.getBenefit())).toList();
        var newMandates = newMandatesRequest.stream().map(mandate -> "*".concat(mandate.getDescription())).toList();
        String partialRequest = promptModel.replaceFirst(NEW_MANDATES_TOKEN, String.join(" \n ", newMandates));
        return partialRequest.replaceFirst(HISTORY_MANDATES_TOKEN, String.join(" \n ", historyMandates));
    }
}
