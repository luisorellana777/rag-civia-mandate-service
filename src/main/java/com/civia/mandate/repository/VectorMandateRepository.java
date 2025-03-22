package com.civia.mandate.repository;

import com.civia.mandate.model.MandatePage;
import com.civia.mandate.model.MandateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.civia.mandate.model.MandateRequest;
import com.civia.mandate.repository.model.HistoryMandate;
import com.civia.mandate.service.gemini.client.GeminiFlashLiteService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
@AllArgsConstructor
public class VectorMandateRepository {

    private HistoryMandateRepository historyMandateRepository;

    private GeminiFlashLiteService geminiFlashLiteService;

    public List<MandateRequest> getHistoryByNewMandates(List<MandateRequest> newMandatesRequest) throws JsonProcessingException {

        List<String> newMandatesDescriptions = newMandatesRequest.stream().map(mandate -> mandate.getDescription()).toList();
        List<List<Double>> newVectors = geminiFlashLiteService.getModelEmbeddings(newMandatesDescriptions);

        List<MandateRequest> mandatesRecommended = newVectors.stream().parallel().map(historyMandateRepository::vectorSearch).flatMap(List::stream)
        .distinct()
        .map(historyMandate -> MandateRequest.builder().description(historyMandate.getDescription()).cost(historyMandate.getCost()).benefit(historyMandate.getBenefit()).build())
        .filter(mandate -> newMandatesRequest.contains(mandate.getDescription()))
        .collect(Collectors.toList());


        return mandatesRecommended;
    }

    public void saveMandatesHistory(List<MandateRequest> newMandatesRequest) throws JsonProcessingException {

        List<MandateRequest> noExistingMandatesHistory = newMandatesRequest.stream().filter(mandate -> !historyMandateRepository.existsByDescription(mandate.getDescription())).toList();
        if(noExistingMandatesHistory.isEmpty()) return;

        List<String> newMandatesDescriptions = noExistingMandatesHistory.stream().map(mandate -> mandate.getDescription()).toList();
        List<List<Double>> historyVectors = geminiFlashLiteService.getModelEmbeddings(newMandatesDescriptions);

        List<HistoryMandate> historyMandates = IntStream.range(0, noExistingMandatesHistory.size())
                .mapToObj(index -> {
                    var mandateRequest = noExistingMandatesHistory.get(index);
                    var vector = historyVectors.get(index);
                    return HistoryMandate.builder().description(mandateRequest.getDescription()).cost(mandateRequest.getCost()).benefit(mandateRequest.getBenefit()).embedding(vector).build();
                }).toList();
        historyMandateRepository.saveAll(historyMandates);
    }

    public MandatePage findMandatesHistoryByPagination(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<HistoryMandate> historyMandatesPage = historyMandateRepository.findAll(pageable);

        List<MandateResponse> mandateResponses = historyMandatesPage.stream().map(mandateHistory -> MandateResponse.builder().description(mandateHistory.getDescription()).inferredBenefit(mandateHistory.getBenefit()).inferredCost(mandateHistory.getCost()).build()).toList();
        MandatePage mandatePage = MandatePage.builder().content(mandateResponses).totalPages(historyMandatesPage.getTotalPages()).number(historyMandatesPage.getNumber()).numberOfElements(historyMandatesPage.getNumberOfElements()).totalElements(historyMandatesPage.getTotalElements()).build();
        return mandatePage;
    }
}
