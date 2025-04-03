package com.civia.mandate.repository;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.Status;
import com.civia.mandate.dto.inout.ClusterPageResponse;
import com.civia.mandate.dto.inout.MandateHistoryResponse;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.dto.inout.MandateResponse;
import com.civia.mandate.mapper.MandateMapper;
import com.civia.mandate.repository.model.HistoryMandateModel;
import com.civia.mandate.repository.model.MandateModel;
import com.civia.mandate.service.gemini.client.GeminiFlashLiteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Repository
@AllArgsConstructor
public class MandateRepository {

    private GeminiFlashLiteService geminiFlashLiteService;

    private MandateModelRepository mandateModelRepository;

    private MandateMapper mapper;

    public List<MandateDto> saveNewMandates(List<MandateDto> mandateDtos) throws JsonProcessingException {

        List<MandateDto> noExistingMandatesDtos = getUnsavedMandates(mandateDtos).stream().map(mandateDto -> mandateDto.toBuilder().status(Status.CREADO).createdAt(LocalDateTime.now()).build()).toList();
        if(noExistingMandatesDtos.isEmpty()) return noExistingMandatesDtos;

        List<String> newMandatesRequestSummarization = noExistingMandatesDtos.stream().map(mandate -> mandate.getRequestSummarization()).toList();
        List<List<Double>> requestSummarizationVectors = geminiFlashLiteService.getModelEmbeddings(newMandatesRequestSummarization);

        List<MandateModel> mandatesModelWithEmbedded = IntStream.range(0, noExistingMandatesDtos.size())
                .mapToObj(index -> {
                    var noExistingMandateDto = noExistingMandatesDtos.get(index);
                    var vector = requestSummarizationVectors.get(index);
                    MandateModel mandateModel = mapper.dtoToModel(noExistingMandateDto);
                    mandateModel.setEmbedding(vector);
                    return mandateModel;
                }).toList();

        List<MandateDto> savedMandates = mapper.modelToDto(mandateModelRepository.saveAll(mandatesModelWithEmbedded));

        return savedMandates;
    }

    public List<MandateDto> getUnsavedMandates(List<MandateDto> mandateDtos) {

        return mandateDtos.stream().filter(mandate -> !mandateModelRepository.existsByRequest(mandate.getRequest())).toList();
    }

    public MandateDto updateStatus(String id, Status status) {

        mandateModelRepository.updateMandatesById(id, status, LocalDateTime.now());

        MandateModel mandateModel = mandateModelRepository.findById(id).orElse(MandateModel.builder().build());

        return mapper.modelToDto(mandateModel);
    }

    public MandatePageResponse getMandates(Status status, String department, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<MandateModel> mandatesPage;

        boolean noDepartment = Objects.isNull(department) || department.isEmpty() || department.isBlank();

        if(Objects.isNull(status) && noDepartment){

            mandatesPage = mandateModelRepository.findAll(pageable);

        }else if (Objects.nonNull(status) && noDepartment){

            mandatesPage = mandateModelRepository.findByStatus(status, pageable);

        }else if(Objects.isNull(status)){

            mandatesPage = mandateModelRepository.findByDepartment(department, pageable);

        }else{
            mandatesPage = mandateModelRepository.findByStatusAndDepartment(status, department, pageable);
        }


        List<MandateDto> mandateDto = mandatesPage.stream().map(mapper::modelToDto).toList();
        MandatePageResponse mandatePageResponse = mapper.pageToPageResponse(mandatesPage);
        mandatePageResponse.setContent(mandateDto);

        return mandatePageResponse;
    }

    public ClusterPageResponse getCluster(String id, Status status, String department, int similarity, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        MandateModel mandateModel = mandateModelRepository.findById(id).orElse(null);
        if(Objects.isNull(mandateModel)) return null;

        List<Double> embeddedCentroid = mandateModel.getEmbedding();

        Slice<MandateModel> mandateCluster;

        boolean noDepartment = Objects.isNull(department) || department.isEmpty() || department.isBlank();

        float centroidProximityScore = (float) similarity / 100;

        if(Objects.isNull(status) && noDepartment){

            mandateCluster = mandateModelRepository.vectorSearchAll(embeddedCentroid, id, centroidProximityScore, pageable);

        }else if (Objects.nonNull(status) && noDepartment){

            mandateCluster = mandateModelRepository.vectorSearchByStatus(embeddedCentroid, id, centroidProximityScore, status, pageable);

        }else if(Objects.isNull(status)){

            mandateCluster = mandateModelRepository.vectorSearchByDepartment(embeddedCentroid, id, centroidProximityScore, department, pageable);

        }else{

            mandateCluster = mandateModelRepository.vectorSearchByStatusAndDepartment(embeddedCentroid, id, centroidProximityScore, status, department, pageable);
        }

        List<MandateDto> mandateResponses = mandateCluster.stream().map(mapper::modelToDto).toList();
        return ClusterPageResponse.builder().hasNext(mandateCluster.hasNext()).content(mandateResponses).build();
    }
}
