package com.civia.mandate.dto.gemini;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeminiPromptRequest {

    private List<Content> contents;
    private GenerationConfig generationConfig;
    private Content systemInstruction;

    @Data
    @Builder
    public static class Content {
        private String role;
        private List<Part> parts;
    }

    @Data
    @Builder
    public static class Part {
        private String text;
    }

    @Data
    @Builder
    public static class GenerationConfig {
        private String response_mime_type;
    }
}
