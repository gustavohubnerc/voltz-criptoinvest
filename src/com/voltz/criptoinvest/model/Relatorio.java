package com.voltz.criptoinvest.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade RELATORIO
 * Consolida investimentos e gera relatórios de posição.
 * 
 * Chave Primária: id (Long)
 * Chaves Estrangeiras:
 *   - empresaId (Long) - referencia EMPRESA.id (relatório pertence a uma empresa)
 *   - usuarioId (Long) - referencia USUARIO.id (usuário que gerou o relatório)
 * 
 * Relacionamento N:N com Investimento através de entidade associativa
 */
public class Relatorio {
    public void gerarRelatorio() {
        System.out.println("Gerando relatório geral...");
    }
    public void gerarRelatorio(String filtro) {
        System.out.println("Gerando relatório filtrado por: " + filtro);
    }

    // CHAVE PRIMÁRIA
    private Long id; // PK - Identificador único do relatório
    
    // ATRIBUTOS
    private String titulo; // Título/Nome do relatório
    private LocalDateTime dataGeracao; // Data e hora de geração do relatório
    private String tipo; // Tipo do relatório: CONSOLIDADO, CARTEIRA, PERFORMANCE, etc.
    
    // CHAVES ESTRANGEIRAS
    private Long empresaId; // FK - Empresa do relatório (EMPRESA.id)
    private Long usuarioId; // FK - Usuário que gerou (USUARIO.id)
    
    // RELACIONAMENTO (N:N com Investimento)
    private List<Investimento> investimentos; // Lista de investimentos analisados
    
    // Construtor completo
    public Relatorio(Long id, String titulo, LocalDateTime dataGeracao, String tipo, 
                     Long empresaId, Long usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.dataGeracao = dataGeracao;
        this.tipo = tipo;
        this.empresaId = empresaId;
        this.usuarioId = usuarioId;
        this.investimentos = new ArrayList<>();
    }
    
    // Construtor com investimentos
    public Relatorio(Long id, String titulo, LocalDateTime dataGeracao, String tipo, 
                     Long empresaId, Long usuarioId, List<Investimento> investimentos) {
        this.id = id;
        this.titulo = titulo;
        this.dataGeracao = dataGeracao;
        this.tipo = tipo;
        this.empresaId = empresaId;
        this.usuarioId = usuarioId;
        this.investimentos = investimentos != null ? investimentos : new ArrayList<>();
    }
    
    // Construtor para compatibilidade (sem id e FKs)
    public Relatorio(String titulo, List<Investimento> investimentos) {
        this(null, titulo, LocalDateTime.now(), "CONSOLIDADO", null, null, investimentos);
    }

    // Métodos de negócio
    
    public void adicionarInvestimento(Investimento inv) {
        if (inv != null && !this.investimentos.contains(inv)) {
            this.investimentos.add(inv);
        }
    }
    
    public void removerInvestimento(Investimento inv) {
        this.investimentos.remove(inv);
    }
    
    // Calcula o valor total consolidado com base no preço de mercado
    public double calcularTotal(double precoMercado) {
        return investimentos.stream()
            .mapToDouble(i -> i.calcularValorAtual(precoMercado))
            .sum();
    }
    
    // Calcula o valor total investido (custo de aquisição)
    public double calcularTotalInvestido() {
        return investimentos.stream()
            .mapToDouble(i -> i.calcularValorInvestido())
            .sum();
    }
    
    // Calcula o lucro/prejuízo total
    public double calcularLucroPrejuizoTotal(double precoMercado) {
        return investimentos.stream()
            .mapToDouble(i -> i.calcularLucroPrejuizo(precoMercado))
            .sum();
    }
    
    // Calcula a rentabilidade média
    public double calcularRentabilidadeMedia(double precoMercado) {
        if (investimentos.isEmpty()) return 0;
        return investimentos.stream()
            .mapToDouble(i -> i.calcularRentabilidade(precoMercado))
            .average()
            .orElse(0);
    }
    
    @Override
    public String toString() {
        return "Relatorio{id=" + id + ", titulo='" + titulo + "', tipo='" + tipo + 
               "', dataGeracao=" + dataGeracao + ", empresaId=" + empresaId + 
               ", usuarioId=" + usuarioId + ", qtdInvestimentos=" + investimentos.size() + "}";
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }
    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Long getEmpresaId() {
        return empresaId;
    }
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    public List<Investimento> getInvestimentos() {
        return investimentos;
    }
    public void setInvestimentos(List<Investimento> investimentos) {
        this.investimentos = investimentos;
    }
}