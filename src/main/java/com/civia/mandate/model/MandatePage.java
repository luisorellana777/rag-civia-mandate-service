package com.civia.mandate.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MandatePage {

    private List content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int numberOfElements;


}
