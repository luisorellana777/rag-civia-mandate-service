package com.civia.mandate.mapper;

import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.inout.MandateHistoryRequest;
import com.civia.mandate.dto.inout.MandateHistoryResponse;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.repository.model.HistoryMandateModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMandateMapper {

    @Mappings({
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "cost", source = "request.cost"),
            @Mapping(target = "benefit", source = "request.benefit")
    })
    List<HistoryMandateDto> requestToDtoList(List<MandateHistoryRequest> request);

    @Mappings({
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "cost", source = "cost"),
            @Mapping(target = "benefit", source = "benefit")
    })
    HistoryMandateDto requestToDto(MandateHistoryRequest request);

    @Mappings({
            @Mapping(target = "id", source = "dto.id"),
            @Mapping(target = "description", source = "dto.description"),
            @Mapping(target = "cost", source = "dto.cost"),
            @Mapping(target = "benefit", source = "dto.benefit")
    })
    HistoryMandateModel dtoToModel(HistoryMandateDto dto);

    HistoryMandateDto modelToDto(HistoryMandateModel model);

    @Mappings({
    @Mapping(target = "id", source = "model.id"),
    @Mapping(target = "description", source = "model.description"),
    @Mapping(target = "inferredCost", source = "model.cost"),
    @Mapping(target = "inferredBenefit", source = "model.benefit")})
    MandateHistoryResponse modelToResponse(HistoryMandateModel model);

    @Mappings({
            @Mapping(target = "totalPages", source = "page.totalPages"),
            @Mapping(target = "number", source = "page.number"),
            @Mapping(target = "numberOfElements", source = "page.numberOfElements"),
            @Mapping(target = "totalElements", source = "page.totalElements")
    })
    MandatePageResponse pageToPageResponse(Page<HistoryMandateModel> page);
}
