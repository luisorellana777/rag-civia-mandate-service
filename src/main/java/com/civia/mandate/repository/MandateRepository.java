package com.civia.mandate.repository;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.Status;
import com.civia.mandate.dto.inout.ClusterPageResponse;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.mapper.MandateMapper;
import com.civia.mandate.repository.model.MandateModel;
import com.civia.mandate.service.gemini.client.GeminiFlashLiteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
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
public class MandateRepository {

    private GeminiFlashLiteService geminiFlashLiteService;

    private MandateModelRepository mandateModelRepository;

    private NonFieldWorkClusterRepository nonFieldWorkClusterRepository;

    private FieldWorkClusterRepository fieldWorkClusterRepository;

    private MandateMapper mapper;

    @Value("${cluster.similarity-threshold:50}")
    private Integer similarityThreshold;

    public MandateRepository(GeminiFlashLiteService geminiFlashLiteService, MandateModelRepository mandateModelRepository, NonFieldWorkClusterRepository nonFieldWorkClusterRepository, FieldWorkClusterRepository fieldWorkClusterRepository, MandateMapper mapper) {
        this.geminiFlashLiteService = geminiFlashLiteService;
        this.mandateModelRepository = mandateModelRepository;
        this.nonFieldWorkClusterRepository = nonFieldWorkClusterRepository;
        this.fieldWorkClusterRepository = fieldWorkClusterRepository;
        this.mapper = mapper;
    }

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

    public ClusterPageResponse getCluster(String id, Status status, String department, int kilometers, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        MandateModel mandateModel = mandateModelRepository.findById(id).orElse(null);
        if(Objects.isNull(mandateModel)) return null;

        List<Double> embeddedCentroid = mandateModel.getEmbedding();

        boolean noDepartment = Objects.isNull(department) || department.isEmpty() || department.isBlank();

        Double latitude = Objects.nonNull(mandateModel.getLocation()) ? mandateModel.getLocation().getCoordinates().get(0) : null;
        Double longitude = Objects.nonNull(mandateModel.getLocation()) ? mandateModel.getLocation().getCoordinates().get(1) : null;

        boolean isFieldWork = Objects.nonNull(latitude) && Objects.nonNull(longitude) && Objects.nonNull(mandateModel.getFieldWork()) && mandateModel.getFieldWork() && kilometers > 0;

        Slice<MandateModel> mandateCluster = getClusterByFieldWork(isFieldWork, id, status, department, noDepartment,  embeddedCentroid, kilometers, latitude, longitude, pageable);

        List<MandateDto> mandateResponses = mandateCluster.stream().map(mapper::modelToDto).toList();
        return ClusterPageResponse.builder().hasNext(mandateCluster.hasNext()).content(mandateResponses).build();
    }

    private Slice<MandateModel> getClusterByFieldWork(boolean isFieldWork, String id, Status status, String department, boolean noDepartment, List<Double> embeddedCentroVector,  int kilometers, Double latitude, Double longitude, Pageable pageable){

        float centroidProximityScore = (float) similarityThreshold / 100;
        int metersProximity = kilometers * 1000;

        if(Objects.isNull(status) && noDepartment){

            return isFieldWork ?
                    fieldWorkClusterRepository.vectorSearchAll(embeddedCentroVector, latitude, longitude, metersProximity, id, centroidProximityScore, pageable) :
                    nonFieldWorkClusterRepository.vectorSearchAll(embeddedCentroVector, id, centroidProximityScore, pageable);

        }else if (Objects.nonNull(status) && noDepartment){

            return isFieldWork ?
                    fieldWorkClusterRepository.vectorSearchByStatus(embeddedCentroVector, latitude, longitude, metersProximity, id, centroidProximityScore, status, pageable) :
                    nonFieldWorkClusterRepository.vectorSearchByStatus(embeddedCentroVector, id, centroidProximityScore, status, pageable);

        }else if(Objects.isNull(status)){

            return isFieldWork ?
                    fieldWorkClusterRepository.vectorSearchByDepartment(embeddedCentroVector, latitude, longitude, metersProximity, id, centroidProximityScore, department, pageable) :
                    nonFieldWorkClusterRepository.vectorSearchByDepartment(embeddedCentroVector, id, centroidProximityScore, department, pageable);

        }else{

            return isFieldWork ?
                    fieldWorkClusterRepository.vectorSearchByStatusAndDepartment(embeddedCentroVector, latitude, longitude, metersProximity, id, centroidProximityScore, status, department, pageable) :
                    nonFieldWorkClusterRepository.vectorSearchByStatusAndDepartment(embeddedCentroVector, id, centroidProximityScore, status, department, pageable);
        }
    }
}
