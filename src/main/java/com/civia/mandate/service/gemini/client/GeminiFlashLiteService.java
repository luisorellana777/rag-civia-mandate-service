package com.civia.mandate.service.gemini.client;

import com.civia.mandate.dto.MandateDto;
import com.civia.mandate.dto.inout.LlmInferringResponse;
import com.civia.mandate.dto.PromptDto;
import com.civia.mandate.dto.gemini.GeminiEmbeddedRequest;
import com.civia.mandate.dto.gemini.GeminiEmbeddedsRequest;
import com.civia.mandate.dto.gemini.GeminiPromptRequest;
import com.civia.mandate.dto.inout.LlmSummarizingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class GeminiFlashLiteService {

    private final WebClient geminiWebClientRecommendation;
    private final WebClient geminiWebClientEmbedding;
    private final WebClient geminiWebClientEmbeddings;

    public List<MandateDto> getRequestSummarization(PromptDto promptDto, List<MandateDto> mandatesDto) throws JsonProcessingException {

        var response = this.getPromptingResult(promptDto);

        var llmMandateResponse = new ObjectMapper().readValue(response, new TypeReference<List<LlmSummarizingResponse>>() {});

        return IntStream.range(0, mandatesDto.size())
                .mapToObj(index -> {
                    var mandateDto = mandatesDto.get(index);
                    mandateDto.setRequestSummarization(llmMandateResponse.get(index).getRequestSummarization());
                    return mandateDto;
        }).toList();
    }

    public List<MandateDto> getModelRecommendation(PromptDto promptDto, List<MandateDto> mandatesDto) throws JsonProcessingException {

        var response = this.getPromptingResult(promptDto);

        var llmMandateResponse = new ObjectMapper().readValue(response, new TypeReference<List<LlmInferringResponse>>() {});

        List<LlmInferringResponse> llmUniqueMandateResponses = llmMandateResponse.stream().parallel().filter(llmMandate -> mandatesDto.stream().map(MandateDto::getRequestSummarization).anyMatch(newMandate -> newMandate.equals(llmMandate.getRequest()))).toList();

        return mandatesDto.stream().map(mandateDto->{
            var llmMandate = llmUniqueMandateResponses.stream().filter(llMandate -> llMandate.getRequest().equalsIgnoreCase(mandateDto.getRequestSummarization())).findFirst().get();
            mandateDto.setInferredCost(llmMandate.getInferredCost());
            mandateDto.setInferredBenefit(llmMandate.getInferredBenefit());
            mandateDto.setPriority(llmMandate.getPriority());
            mandateDto.setExplanation(llmMandate.getExplanation());
            mandateDto.setDepartment(llmMandate.getDepartment());
            mandateDto.setFieldWork(llmMandate.getFieldWork());
            return mandateDto;
        }).toList();
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

        return new ObjectMapper().readValue(textNode.toString(), new TypeReference<>() {});
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

        var embedsResponse = new ArrayList<List<Double>>();
        while(embeddings.hasNext()){

            List<Double> embeddedResponse = new ObjectMapper().readValue(embeddings.next().path("values").toString(), new TypeReference<>() {});
            embedsResponse.add(embeddedResponse);
        }

        return embedsResponse;
    }

    private String getPromptingResult(PromptDto promptDto) throws JsonProcessingException {

        var geminiPromptRequest = GeminiPromptRequest.builder().contents(List.of(GeminiPromptRequest.Content.builder().parts(List.of(GeminiPromptRequest.Part.builder().text(promptDto.getContent()).build())).role("user").build())).generationConfig(GeminiPromptRequest.GenerationConfig.builder().response_mime_type("application/json").build()).systemInstruction(GeminiPromptRequest.Content.builder().parts(List.of(GeminiPromptRequest.Part.builder().text(promptDto.getSystemInstruction()).build())).role("model").build()).build();
        var jsonString = new ObjectMapper().writeValueAsString(geminiPromptRequest);

        var jsonResponse = geminiWebClientRecommendation.post()
                .bodyValue(jsonString)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        var rootNode = new ObjectMapper().readTree(jsonResponse);

        return rootNode.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text").asText();
    }
}