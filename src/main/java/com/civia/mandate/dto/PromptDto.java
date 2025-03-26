package com.civia.mandate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromptDto {

    private String systemInstruction;
    private String content;
}
