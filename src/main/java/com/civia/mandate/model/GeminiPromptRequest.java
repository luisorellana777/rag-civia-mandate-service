package com.civia.mandate.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeminiPromptRequest {

    private List<Content> contents;

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
