package com.civia.mandate.dto.inout;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ClusterPageResponse {

    private List content;
    private final boolean hasNext;
}
