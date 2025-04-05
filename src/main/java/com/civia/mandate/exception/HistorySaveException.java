package com.civia.mandate.exception;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder=true)
public class HistorySaveException extends RuntimeException{

    @Builder.Default
    private List<String> messages = new ArrayList<>();

    public void addMessage(String message){
        this.messages.add(message);
    }

    public String getMessages(){
        return "Following mandates were not saved: ".concat(String.join("\n", messages));
    }
}
