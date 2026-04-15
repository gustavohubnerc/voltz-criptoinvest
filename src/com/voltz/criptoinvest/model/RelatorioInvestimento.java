package com.voltz.criptoinvest.model;

import java.time.LocalDateTime;

/**
 * Entidade Associativa RELATORIO_INVESTIMENTO
 * Representa o relacionamento N:N entre Relatorio e Investimento.
 * Esta tabela associativa permite que um relatório contenha múltiplos investimentos
 * e que um investimento apareça em múltiplos relatórios.
 * 
 * Chave Primária Composta: (relatorioId, investimentoId)
 * Chaves Estrangeiras:
 *   - relatorioId (Long) - referencia RELATORIO.id
 *   - investimentoId (Long) - referencia INVESTIMENTO.id
 */
public class RelatorioInvestimento {
    public void gerarRelatorio() {
        System.out.println("Gerando relatório geral...");
    }
    public void gerarRelatorio(String filtro) {
        System.out.println("Gerando relatório filtrado por: " + filtro);
    }

    // CHAVES PRIMÁRIAS (Composta)
    private Long relatorioId; // PK/FK - Referencia o relatório (RELATORIO.id)
    private Long investimentoId; // PK/FK - Referencia o investimento (INVESTIMENTO.id)
    
    // ATRIBUTOS ADICIONAIS (específicos da associação)
    private LocalDateTime dataInclusao; // Data quando o investimento foi incluído no relatório
    private double precoMercadoCapturado; // Preço de mercado no momento da inclusão no relatório
    private String observacoes; // Observações específicas sobre este investimento no relatório
    
    // Construtor completo
    public RelatorioInvestimento(Long relatorioId, Long investimentoId, 
                                 LocalDateTime dataInclusao, double precoMercadoCapturado, String observacoes) {
        this.relatorioId = relatorioId;
        this.investimentoId = investimentoId;
        this.dataInclusao = dataInclusao;
        this.precoMercadoCapturado = precoMercadoCapturado;
        this.observacoes = observacoes;
    }
    
    // Construtor simplificado
    public RelatorioInvestimento(Long relatorioId, Long investimentoId, double precoMercadoCapturado) {
        this(relatorioId, investimentoId, LocalDateTime.now(), precoMercadoCapturado, null);
    }
    
    // Construtor mínimo
    public RelatorioInvestimento(Long relatorioId, Long investimentoId) {
        this(relatorioId, investimentoId, LocalDateTime.now(), 0.0, null);
    }

    @Override
    public String toString() {
        return "RelatorioInvestimento{relatorioId=" + relatorioId + 
               ", investimentoId=" + investimentoId + 
               ", dataInclusao=" + dataInclusao + 
               ", precoMercadoCapturado=" + precoMercadoCapturado + 
               ", observacoes='" + observacoes + "'}";
    }
    public Long getRelatorioId() {
        return relatorioId;
    }
    public void setRelatorioId(Long relatorioId) {
        this.relatorioId = relatorioId;
    }
    public Long getInvestimentoId() {
        return investimentoId;
    }
    public void setInvestimentoId(Long investimentoId) {
        this.investimentoId = investimentoId;
    }
    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }
    public void setDataInclusao(LocalDateTime dataInclusao) {
        this.dataInclusao = dataInclusao;
    }
    public double getPrecoMercadoCapturado() {
        return precoMercadoCapturado;
    }
    public void setPrecoMercadoCapturado(double precoMercadoCapturado) {
        this.precoMercadoCapturado = precoMercadoCapturado;
    }
    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}