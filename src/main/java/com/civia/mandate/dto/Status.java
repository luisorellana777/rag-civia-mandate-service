package com.civia.mandate.dto;

import lombok.Getter;

@Getter
public enum Status {
    CREADO("Creado"),
    EN_PROCESO("En Proceso"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    private String text;
    Status(String text) {
            this.text = text;
        }
}
