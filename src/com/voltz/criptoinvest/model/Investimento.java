package com.voltz.criptoinvest.model;

/**
 * Entidade INVESTIMENTO
 * Representa a alocação de um criptoativo específico em uma carteira.
 * 
 * Chave Primária: id (Long)
 * Chave Estrangeira: carteiraId (Long) - referencia CARTEIRA.id
 */
public class Investimento {
    // CHAVE PRIMÁRIA
    private Long id; // PK - Identificador único do investimento
    
    // ATRIBUTOS
    private String ativo; // Nome/Símbolo do criptoativo (ex: BTC, ETH, SOL)
    private double quantidade; // Quantidade do ativo (pode ser fracionado)
    private double precoMedio; // Preço médio de aquisição (em R$)
    
    // CHAVE ESTRANGEIRA
    private Long carteiraId; // FK - Referencia a carteira que contém o investimento (CARTEIRA.id)
    
    // Construtor completo
    public Investimento(Long id, String ativo, double quantidade, double precoMedio, Long carteiraId) {
        this.id = id;
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.precoMedio = precoMedio;
        this.carteiraId = carteiraId;
    }
    
    // Construtor para compatibilidade (sem id e carteiraId)
    public Investimento(String ativo, double quantidade, double precoMedio) {
        this(null, ativo, quantidade, precoMedio, null);
    }

    // Métodos de negócio
    
    // Calcula o valor atual do investimento com base no preço de mercado
    public double calcularValorAtual(double precoMercado) {
        return quantidade * precoMercado;
    }
    
    // Calcula o valor investido (custo de aquisição)
    public double calcularValorInvestido() {
        return quantidade * precoMedio;
    }
    
    // Calcula o lucro/prejuízo com base no preço de mercado atual
    public double calcularLucroPrejuizo(double precoMercado) {
        return calcularValorAtual(precoMercado) - calcularValorInvestido();
    }
    
    // Calcula a rentabilidade percentual
    public double calcularRentabilidade(double precoMercado) {
        if (precoMedio == 0) return 0;
        return ((precoMercado - precoMedio) / precoMedio) * 100;
    }
    
    @Override
    public String toString() {
        return "Investimento{id=" + id + ", ativo='" + ativo + "', quantidade=" + quantidade + 
               ", precoMedio=" + precoMedio + ", carteiraId=" + carteiraId + "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getPrecoMedio() {
        return precoMedio;
    }

    public void setPrecoMedio(double precoMedio) {
        this.precoMedio = precoMedio;
    }

    public Long getCarteiraId() {
        return carteiraId;
    }

    public void setCarteiraId(Long carteiraId) {
        this.carteiraId = carteiraId;
    }
}