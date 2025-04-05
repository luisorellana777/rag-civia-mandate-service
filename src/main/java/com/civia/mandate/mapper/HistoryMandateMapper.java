package com.civia.mandate.mapper;

import com.civia.mandate.dto.HistoryMandateDto;
import com.civia.mandate.dto.inout.MandateExistingHistoryRequest;
import com.civia.mandate.dto.inout.MandateNewHistoryRequest;
import com.civia.mandate.dto.inout.MandateHistoryResponse;
import com.civia.mandate.dto.inout.MandatePageResponse;
import com.civia.mandate.repository.model.HistoryMandateModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistoryMandateMapper {

    @Mappings({
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "cost", source = "request.cost"),
            @Mapping(target = "benefit", source = "request.benefit")
    })
    List<HistoryMandateDto> newRequestToDtoList(List<MandateNewHistoryRequest> request);

    @Mappings({
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "cost", source = "cost"),
            @Mapping(target = "benefit", source = "benefit"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "score", ignore = true)
    })
    HistoryMandateDto requestToDto(MandateNewHistoryRequest request);

    @Mappings({
            @Mapping(target = "id", source = "dto.id"),
            @Mapping(target = "description", source = "dto.description"),
            @Mapping(target = "cost", source = "dto.cost"),
            @Mapping(target = "benefit", source = "dto.benefit"),
            @Mapping(target = "embedding", ignore = true)
    })
    HistoryMandateModel dtoToModel(HistoryMandateDto dto);

    HistoryMandateDto modelToDto(HistoryMandateModel model);

    @Mappings({
    @Mapping(target = "id", source = "model.id"),
    @Mapping(target = "description", source = "model.description"),
    @Mapping(target = "inferredCost", source = "model.cost"),
    @Mapping(target = "inferredBenefit", source = "model.benefit"),
    @Mapping(target = "inferredDepartment", source = "model.department")})
    MandateHistoryResponse modelToResponse(HistoryMandateModel model);

    @Mappings({
            @Mapping(target = "totalPages", source = "page.totalPages"),
            @Mapping(target = "number", source = "page.number"),
            @Mapping(target = "numberOfElements", source = "page.numberOfElements"),
            @Mapping(target = "totalElements", source = "page.totalElements")
    })
    MandatePageResponse pageToPageResponse(Page<HistoryMandateModel> page);

    @Mappings({
            @Mapping(target = "id", source = "request.id"),
            @Mapping(target = "cost", source = "request.cost"),
            @Mapping(target = "benefit", source = "request.benefit"),
            @Mapping(target = "department", source = "request.department"),
            @Mapping(target = "description", ignore = true),
            @Mapping(target = "score", ignore = true)
    })
    List<HistoryMandateDto> existingRequestToDtoList(List<MandateExistingHistoryRequest> request);
}
