package com.voltz.criptoinvest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade CARTEIRA
 * Representa uma carteira digital vinculada a uma empresa.
 * 
 * Chave Primária: id (Long)
 * Chave Estrangeira: empresaId (Long) - referencia EMPRESA.id
 * Relacionamento: 1 Carteira contém N Investimentos (1:N)
 */
public class Carteira {
    // CHAVE PRIMÁRIA
    private Long id; // PK - Identificador único da carteira
    
    // ATRIBUTOS
    private String endereco; // Endereço único no blockchain
    private String custodiante; // Nome do provedor de custódia
    private double saldo; // Saldo disponível em reais (R$)
    
    // CHAVE ESTRANGEIRA
    private Long empresaId; // FK - Referencia a empresa dona da carteira (EMPRESA.id)
    
    // RELACIONAMENTO (1:N com Investimento)
    private List<Investimento> investimentos; // Lista de investimentos/ativos na carteira
    
    // Construtor completo
    public Carteira(Long id, String endereco, String custodiante, double saldo, Long empresaId) {
        this.id = id;
        this.endereco = endereco;
        this.custodiante = custodiante;
        this.saldo = saldo;
        this.empresaId = empresaId;
        this.investimentos = new ArrayList<>();
    }
    
    // Construtor com investimentos
    public Carteira(Long id, String endereco, String custodiante, double saldo, Long empresaId, List<Investimento> investimentos) {
        this.id = id;
        this.endereco = endereco;
        this.custodiante = custodiante;
        this.saldo = saldo;
        this.empresaId = empresaId;
        this.investimentos = investimentos != null ? investimentos : new ArrayList<>();
    }
    
    // Construtor para compatibilidade (sem id e empresaId)
    public Carteira(String endereco, String custodiante, double saldo, List<Investimento> investimentos) {
        this(null, endereco, custodiante, saldo, null, investimentos);
    }

    public Carteira(long idCarteira) {
        this.id = idCarteira;
    }

    // Métodos de negócio
    public void adicionarInvestimento(Investimento inv) {
        if (inv != null) {
            inv.setCarteiraId(this.id); // Define a FK no investimento
            this.investimentos.add(inv);
        }
    }
    
    public void removerInvestimento(Investimento inv) {
        this.investimentos.remove(inv);
    }
    
    // Calcula o valor total investido na carteira
    public double calcularValorTotalInvestido() {
        return investimentos.stream()
            .mapToDouble(inv -> inv.getQuantidade() * inv.getPrecoMedio())
            .sum();
    }
    
    @Override
    public String toString() {
        return "Carteira{id=" + id + ", endereco='" + endereco + "', custodiante='" + custodiante + 
               "', saldo=" + saldo + ", empresaId=" + empresaId + ", qtdInvestimentos=" + investimentos.size() + "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCustodiante() {
        return custodiante;
    }

    public void setCustodiante(String custodiante) {
        this.custodiante = custodiante;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public List<Investimento> getInvestimentos() {
        return investimentos;
    }

    public void setInvestimentos(List<Investimento> investimentos) {
        this.investimentos = investimentos;
    }
}