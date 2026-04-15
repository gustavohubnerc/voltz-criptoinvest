package com.voltz.criptoinvest.model;

import java.time.LocalDateTime;

/**
 * Entidade TRANSACAO
 * Representa uma operação financeira (compra, venda, depósito ou saque) em uma carteira.
 * 
 * Chave Primária: id (Long)
 * Chaves Estrangeiras: 
 *   - carteiraId (Long) - referencia CARTEIRA.id
 *   - usuarioId (Long) - referencia USUARIO.id (quem executou a transação)
 *   - investimentoId (Long) - referencia INVESTIMENTO.id (opcional, quando aplicável)
 */
public class Transacao {
    // CHAVE PRIMÁRIA
    private Long id; // PK - Identificador único da transação
    
    // ATRIBUTOS
    private String tipo; // Tipo da operação: COMPRA, VENDA, DEPOSITO, SAQUE
    private String ativo; // Símbolo do ativo movimentado (ex: BTC, ETH, BRL)
    private double quantidade; // Quantidade negociada
    private double preco; // Preço unitário da operação (em R$)
    private LocalDateTime data; // Data e hora da transação
    private String status; // Status: PENDENTE, CONFIRMADA, CANCELADA, FALHA
    
    // CHAVES ESTRANGEIRAS
    private Long carteiraId; // FK - Carteira onde ocorreu a transação (CARTEIRA.id)
    private Long usuarioId; // FK - Usuário que executou a transação (USUARIO.id)
    private Long investimentoId; // FK - Investimento relacionado (INVESTIMENTO.id) - opcional
    
    // Construtor completo
    public Transacao(Long id, String tipo, String ativo, double quantidade, double preco, 
                     LocalDateTime data, String status, Long carteiraId, Long usuarioId, Long investimentoId) {
        this.id = id;
        this.tipo = tipo;
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.preco = preco;
        this.data = data;
        this.status = status;
        this.carteiraId = carteiraId;
        this.usuarioId = usuarioId;
        this.investimentoId = investimentoId;
    }
    
    // Construtor simplificado
    public Transacao(Long id, String tipo, String ativo, double quantidade, double preco, 
                     LocalDateTime data, Long carteiraId, Long usuarioId) {
        this(id, tipo, ativo, quantidade, preco, data, "CONFIRMADA", carteiraId, usuarioId, null);
    }
    
    // Construtor para compatibilidade (sem id e FKs)
    public Transacao(String tipo, String ativo, double quantidade, double preco, LocalDateTime data) {
        this(null, tipo, ativo, quantidade, preco, data, "CONFIRMADA", null, null, null);
    }

    // Métodos de negócio
    
    // Calcula o valor total da transação
    public double calcularValorTotal() {
        return quantidade * preco;
    }
    
    // Verifica se a transação foi confirmada
    public boolean isConfirmada() {
        return "CONFIRMADA".equalsIgnoreCase(status);
    }
    
    // Verifica se é uma transação de entrada (compra ou depósito)
    public boolean isEntrada() {
        return "COMPRA".equalsIgnoreCase(tipo) || "DEPOSITO".equalsIgnoreCase(tipo);
    }
    
    // Verifica se é uma transação de saída (venda ou saque)
    public boolean isSaida() {
        return "VENDA".equalsIgnoreCase(tipo) || "SAQUE".equalsIgnoreCase(tipo);
    }
    
    @Override
    public String toString() {
        return "Transacao{id=" + id + ", tipo='" + tipo + "', ativo='" + ativo + 
               "', quantidade=" + quantidade + ", preco=" + preco + ", valorTotal=" + calcularValorTotal() +
               ", data=" + data + ", status='" + status + "', carteiraId=" + carteiraId + 
               ", usuarioId=" + usuarioId + ", investimentoId=" + investimentoId + "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCarteiraId() {
        return carteiraId;
    }

    public void setCarteiraId(Long carteiraId) {
        this.carteiraId = carteiraId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getInvestimentoId() {
        return investimentoId;
    }

    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }
}