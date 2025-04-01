package com.civia.mandate.repository;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.mapper.MandateMapper;
import com.civia.mandate.repository.model.MandateModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class MandateRepository {

    private MandateModelRepository mandateModelRepository;

    private MandateMapper mapper;

    public List<MandateDto> saveMandates(List<MandateDto> mandateDtos) throws JsonProcessingException {

        List<MandateDto> noExistingMandatesDtos = getUnsavedMandates(mandateDtos);
        if(noExistingMandatesDtos.isEmpty()) return noExistingMandatesDtos;

        List<MandateModel> mandatesModel = mapper.dtoToModel(noExistingMandatesDtos);
        List<MandateDto> savedMandates = mapper.modelToDto(mandateModelRepository.saveAll(mandatesModel));

        return savedMandates;
    }

    public List<MandateDto> getUnsavedMandates(List<MandateDto> mandateDtos) {

        List<MandateDto> noExistingMandatesDtos = mandateDtos.stream().filter(mandate -> !mandateModelRepository.existsByRequest(mandate.getRequest())).toList();
        if(noExistingMandatesDtos.isEmpty()) return noExistingMandatesDtos;

        return noExistingMandatesDtos;
    }
}
