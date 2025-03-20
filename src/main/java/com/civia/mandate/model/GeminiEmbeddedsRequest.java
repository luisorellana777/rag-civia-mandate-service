package com.civia.mandate.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeminiEmbeddedsRequest {

    private List<Request> requests;

    @Data
    @Builder
    public static class Request {
        private String model;
        private Content content;
    }
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
