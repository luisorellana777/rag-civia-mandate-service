package com.civia.mandate.dto.inout;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MandatePageResponse {

    private List content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int numberOfElements;


}
