package com.civia.mandate.dto.inout;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LlmSummarizingResponse {

    @EqualsAndHashCode.Include
    private String requestSummarization;

    @JsonCreator
    public LlmSummarizingResponse(@JsonProperty("requestSummarization") String requestSummarization) {
        this.requestSummarization = requestSummarization;
    }
}
