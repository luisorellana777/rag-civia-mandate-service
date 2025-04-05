package com.civia.mandate.repository;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.Status;
import com.civia.mandate.dto.inout.MandateHistoryResponse;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.exception.HistorySaveException;
import com.civia.mandate.mapper.HistoryMandateMapper;
import com.civia.mandate.repository.model.MandateModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.civia.mandate.repository.model.HistoryMandateModel;
import com.civia.mandate.service.gemini.client.GeminiFlashLiteService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
@AllArgsConstructor
public class HistoryMandateRepository {

    private HistoryMandateModelRepository historyMandateModelRepository;

    private GeminiFlashLiteService geminiFlashLiteService;

    private MandateModelRepository mandateModelRepository;

    private HistoryMandateMapper mapper;

    public List<HistoryMandateDto> getSimilarHistoryByNewMandates(List<MandateDto> mandatesDto) throws JsonProcessingException {

        List<String> newMandatesSummarizationRequests = mandatesDto.stream().map(mandate -> mandate.getRequestSummarization()).toList();
        List<List<Double>> newVectors = geminiFlashLiteService.getModelEmbeddings(newMandatesSummarizationRequests);

        List<HistoryMandateDto> mandatesRecommended = newVectors.stream().parallel().map(historyMandateModelRepository::vectorSearch).flatMap(List::stream)
        .distinct()
        .map(mapper::modelToDto)
        .collect(Collectors.toList());

        return mandatesRecommended;
    }

    public void saveNewMandatesHistory(List<HistoryMandateDto> historyMandateDtos) throws JsonProcessingException {

        List<HistoryMandateDto> noExistingMandatesHistoryDtos = historyMandateDtos.stream().filter(mandate -> !historyMandateModelRepository.existsByDescription(mandate.getDescription())).toList();
        if(noExistingMandatesHistoryDtos.isEmpty()) return;

        List<String> newMandatesDescriptions = noExistingMandatesHistoryDtos.stream().map(mandate -> mandate.getDescription()).toList();
        List<List<Double>> historyVectors = geminiFlashLiteService.getModelEmbeddings(newMandatesDescriptions);

        List<HistoryMandateModel> historyMandateModels = IntStream.range(0, noExistingMandatesHistoryDtos.size())
                .mapToObj(index -> {
                    var mandateHistoryDto = noExistingMandatesHistoryDtos.get(index);
                    var vector = historyVectors.get(index);
                    HistoryMandateModel historyMandateModel = mapper.dtoToModel(mandateHistoryDto);
                    historyMandateModel.setEmbedding(vector);
                    return historyMandateModel;
                }).toList();
        historyMandateModelRepository.saveAll(historyMandateModels);
    }

    public MandatePageResponse findMandatesHistoryByPagination(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<HistoryMandateModel> historyMandatesPage = historyMandateModelRepository.findAll(pageable);

        List<MandateHistoryResponse> mandateResponses = historyMandatesPage.stream().map(mapper::modelToResponse).toList();
        MandatePageResponse mandatePageResponse = mapper.pageToPageResponse(historyMandatesPage);
        mandatePageResponse.setContent(mandateResponses);

        return mandatePageResponse;
    }

    public void saveExistingMandatesHistory(List<HistoryMandateDto> historyMandateDtos) {


        List<HistoryMandateModel> historyMandatesToSave = new ArrayList<>();
        HistorySaveException historySaveException = HistorySaveException.builder().build();

        historyMandateDtos.stream().forEach(historyMandateDto->{

            MandateModel mandateModel = mandateModelRepository.findById(historyMandateDto.getId()).orElseGet(()-> {
                historySaveException.addMessage("Not Found: ".concat(historyMandateDto.getId()));
                return null;
            });

            Optional.ofNullable(mandateModel).ifPresent(model->{

                String requestSummarization = mandateModel.getRequestSummarization();
                List<Double> embedding = mandateModel.getEmbedding();
                HistoryMandateModel newHistoryMandateModel = mapper.dtoToModel(historyMandateDto)
                        .toBuilder()
                        .embedding(embedding)
                        .description(requestSummarization).build();

                if(historyMandateModelRepository.existsByDescription(requestSummarization)){

                    historySaveException.addMessage("Already Exists: ".concat(historyMandateDto.getId()));

                }else if (!mandateModel.getStatus().equals(Status.FINALIZADO)){

                    historySaveException.addMessage("Status Not Finalized: ".concat(historyMandateDto.getId()));

                }else{

                    historyMandatesToSave.add(newHistoryMandateModel);
                }
            });

        });

        historyMandateModelRepository.saveAll(historyMandatesToSave);

        if(!historySaveException.getMessages().isEmpty()) throw historySaveException;
    }
}
