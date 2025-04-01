package com.civia.mandate.mapper;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.dto.inout.MandateRequest;
import com.civia.mandate.dto.inout.MandateResponse;
import com.civia.mandate.repository.model.MandateModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MandateMapper {

    List<MandateDto> requestToDtoList(List<MandateRequest> request);

    MandateDto requestToDto(MandateRequest request);

    List<MandateModel> dtoToModel(List<MandateDto> noExistingMandatesDtos);

    List<MandateDto> modelToDto(List<MandateModel> noExistingMandatesDtos);

    List<MandateResponse> dtoToResponses(List<MandateDto> savedMandatesDto);

    MandateResponse dtoToResponse(MandateDto savedMandateDto);

    MandateDto modelToDto(MandateModel model);

    @Mappings({
            @Mapping(target = "totalPages", source = "page.totalPages"),
            @Mapping(target = "number", source = "page.number"),
            @Mapping(target = "numberOfElements", source = "page.numberOfElements"),
            @Mapping(target = "totalElements", source = "page.totalElements")
    })
    MandatePageResponse pageToPageResponse(Page<MandateModel> page);
}
