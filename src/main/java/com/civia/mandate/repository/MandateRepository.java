package com.civia.mandate.repository;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.Status;
import com.civia.mandate.dto.inout.MandateHistoryResponse;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.dto.inout.MandateResponse;
import com.civia.mandate.mapper.MandateMapper;
import com.civia.mandate.repository.model.HistoryMandateModel;
import com.civia.mandate.repository.model.MandateModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
public class MandateRepository {

    private MandateModelRepository mandateModelRepository;

    private MandateMapper mapper;

    public List<MandateDto> saveNewMandates(List<MandateDto> mandateDtos) throws JsonProcessingException {

        List<MandateDto> noExistingMandatesDtos = getUnsavedMandates(mandateDtos);
        if(noExistingMandatesDtos.isEmpty()) return noExistingMandatesDtos;

        noExistingMandatesDtos = noExistingMandatesDtos.stream().map(mandateDto -> mandateDto.toBuilder().status(Status.CREADO).createdAt(LocalDateTime.now()).build()).toList();

        List<MandateModel> mandatesModel = mapper.dtoToModel(noExistingMandatesDtos);
        List<MandateDto> savedMandates = mapper.modelToDto(mandateModelRepository.saveAll(mandatesModel));

        return savedMandates;
    }

    public List<MandateDto> getUnsavedMandates(List<MandateDto> mandateDtos) {

        List<MandateDto> noExistingMandatesDtos = mandateDtos.stream().filter(mandate -> !mandateModelRepository.existsByRequest(mandate.getRequest())).toList();
        if(noExistingMandatesDtos.isEmpty()) return noExistingMandatesDtos;

        return noExistingMandatesDtos;
    }

    public MandatePageResponse findMandatesByPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<MandateModel> mandatesPage = mandateModelRepository.findAll(pageable);

        List<MandateDto> mandateDto = mandatesPage.stream().map(mapper::modelToDto).toList();
        MandatePageResponse mandatePageResponse = mapper.pageToPageResponse(mandatesPage);
        mandatePageResponse.setContent(mandateDto);

        return mandatePageResponse;
    }

    public MandateDto updateStatus(String id, Status status) {

        mandateModelRepository.updateMandatesById(id, status, LocalDateTime.now());

        MandateModel mandateModel = mandateModelRepository.findById(id).orElse(MandateModel.builder().build());

        return mapper.modelToDto(mandateModel);
    }

    public MandatePageResponse getMandates(Status status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<MandateModel> mandatesPage = mandateModelRepository.findByStatus(status, pageable);

        List<MandateDto> mandateDto = mandatesPage.stream().map(mapper::modelToDto).toList();
        MandatePageResponse mandatePageResponse = mapper.pageToPageResponse(mandatesPage);
        mandatePageResponse.setContent(mandateDto);

        return mandatePageResponse;
    }
}
