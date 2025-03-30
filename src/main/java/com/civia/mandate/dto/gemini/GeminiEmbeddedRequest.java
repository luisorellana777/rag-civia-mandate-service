package com.civia.mandate.dto.gemini;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeminiEmbeddedRequest {

    private Content content;

    @Data
    @Builder
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @Builder
    public static class Part {
        private String text;
    }
}
