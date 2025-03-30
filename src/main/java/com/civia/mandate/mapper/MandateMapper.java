package com.civia.mandate.mapper;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.inout.MandateRequest;
import com.civia.mandate.dto.inout.MandateResponse;
import com.civia.mandate.model.MandateModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MandateMapper {

    List<MandateDto> requestToDtoList(List<MandateRequest> request);

    MandateDto requestToDto(MandateRequest request);

    List<MandateModel> dtoToModel(List<MandateDto> noExistingMandatesDtos);

    List<MandateResponse> dtoToResponse(List<MandateDto> savedMandatesDto);
}
