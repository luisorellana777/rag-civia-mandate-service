package com.civia.mandate.dto;

public enum Status {
    CREADO("Creado"),
    EN_PROCESO("En Proceso"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    public static final String DEFAULT_STATUS = "Creado";

    private String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
