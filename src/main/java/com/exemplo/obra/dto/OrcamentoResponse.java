package com.exemplo.obra.dto;

import java.time.LocalDateTime;

public class OrcamentoResponse {

    private Long id;
    private String nomeUsuario;
    private String tipo;
    private Double resultado;
    private LocalDateTime dataCriacao;

    public OrcamentoResponse(Long id, String nomeUsuario, String tipo, Double resultado, LocalDateTime dataCriacao) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.tipo = tipo;
        this.resultado = resultado;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() { return id; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getTipo() { return tipo; }
    public Double getResultado() { return resultado; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}