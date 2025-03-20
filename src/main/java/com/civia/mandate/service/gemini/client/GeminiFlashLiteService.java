package com.civia.mandate.service.gemini.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.civia.mandate.model.GeminiEmbeddedRequest;
import com.civia.mandate.model.GeminiEmbeddedsRequest;
import com.civia.mandate.model.GeminiPromptRequest;
import com.civia.mandate.model.MandateResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class GeminiFlashLiteService {

    private final WebClient geminiWebClientRecommendation;
    private final WebClient geminiWebClientEmbedding;
    private final WebClient geminiWebClientEmbeddings;

    public List<MandateResponse> getModelRecommendation(String inputText) throws JsonProcessingException {

        GeminiPromptRequest geminiPromptRequest = GeminiPromptRequest.builder().contents(List.of(GeminiPromptRequest.Content.builder().parts(List.of(GeminiPromptRequest.Part.builder().text(inputText).build())).build())).build();
        String jsonString = new ObjectMapper().writeValueAsString(geminiPromptRequest);

        String jsonResponse = geminiWebClientRecommendation.post()
                .bodyValue(jsonString)
                .retrieve()
                .bodyToMono(String.class)
                .block().replace("```", "").replace("json", "");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        JsonNode textNode = rootNode.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text");

        List<MandateResponse> mandatesResponse = new ObjectMapper().readValue(textNode.asText(), new TypeReference<List<MandateResponse>>() {});
        return mandatesResponse;
    }

    public List<Double> getModelEmbedding(String inputText) throws JsonProcessingException {

        GeminiEmbeddedRequest geminiPromptRequest = GeminiEmbeddedRequest.builder().content(GeminiEmbeddedRequest.Content.builder().parts(List.of(GeminiEmbeddedRequest.Part.builder().text(inputText).build())).build()).build();
        String jsonString = new ObjectMapper().writeValueAsString(geminiPromptRequest);

        String jsonResponse = geminiWebClientEmbedding.post()
                .bodyValue(jsonString)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        JsonNode textNode = rootNode.path("embedding")
                .path("values");

        List<Double> embeddedResponse = new ObjectMapper().readValue(textNode.toString(), new TypeReference<List<Double>>() {});
        return embeddedResponse;
    }

    public List<List<Double>> getModelEmbeddings(List<String> newMandatesDescriptions) throws JsonProcessingException {

        List<GeminiEmbeddedsRequest.Request> requests = newMandatesDescriptions.stream().map(mandateDescription -> GeminiEmbeddedsRequest.Request.builder().model("models/text-embedding-004").content(GeminiEmbeddedsRequest.Content.builder().parts(List.of(GeminiEmbeddedsRequest.Part.builder().text(mandateDescription).build())).build()).build()).toList();
        GeminiEmbeddedsRequest geminiEmbeddedsRequest = GeminiEmbeddedsRequest.builder().requests(requests).build();

        String jsonString = new ObjectMapper().writeValueAsString(geminiEmbeddedsRequest);

        String jsonResponse = geminiWebClientEmbeddings.post()
                .bodyValue(jsonString)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);

        Iterator<JsonNode> embeddings = rootNode.path("embeddings").elements();

        var embeddedsResponse = new ArrayList();
        while(embeddings.hasNext()){

            List<Double> embeddedResponse = new ObjectMapper().readValue(embeddings.next().path("values").toString(), new TypeReference<List<Double>>() {});
            embeddedsResponse.add(embeddedResponse);
        }

        return embeddedsResponse;
    }
}