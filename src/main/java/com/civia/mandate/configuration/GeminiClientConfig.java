package com.civia.mandate.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GeminiClientConfig {

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models";
    @Value("${GEMINI-KEY}")
    private String geminiKey;

    @Bean
    public WebClient geminiWebClientEmbeddings() {
        return WebClient.builder()
                .baseUrl(BASE_URL.concat("/text-embedding-004:batchEmbedContents?key=").concat(geminiKey))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient geminiWebClientEmbedding() {
        return WebClient.builder()
                .baseUrl(BASE_URL.concat("/text-embedding-004:embedContent?key=").concat(geminiKey))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient geminiWebClientRecommendation() {
        return WebClient.builder()
                .baseUrl(BASE_URL.concat("/gemini-2.0-flash:generateContent?key=").concat(geminiKey))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}