package com.voltz.criptoinvest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade EMPRESA
 * Representa uma entidade jurídica que investe em criptoativos.
 * 
 * Chave Primária: id (Long)
 * Relacionamento: 1 Empresa possui N Carteiras (1:N)
 */
public class Empresa {
    // CHAVE PRIMÁRIA
    private Long id; // PK - Identificador único da empresa
    
    // ATRIBUTOS
    private String cnpj; // CNPJ da empresa (identificação fiscal única)
    private String razaoSocial; // Nome/Razão social da empresa
    
    // RELACIONAMENTO (1:N com Carteira)
    private List<Carteira> carteiras; // Lista de carteiras da empresa
    
    // Construtor completo
    public Empresa(Long id, String cnpj, String razaoSocial) {
        this.id = id;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.carteiras = new ArrayList<>();
    }
    // Construtor sem id

    public Empresa(String cnpj, String razaoSocial, List<Carteira> carteiras) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.carteiras = carteiras;
    }

    //Criando construtor enquanto não é criada a classe tb_carteira
    public Empresa(String cnpj, String razaoSocial) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    // Construtor com carteiras
    public Empresa(Long id, String cnpj, String razaoSocial, List<Carteira> carteiras) {
        this.id = id;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.carteiras = carteiras != null ? carteiras : new ArrayList<>();
    }

    // Métodos de negócio
    public void adicionarCarteira(Carteira carteira) {
        if (carteira != null) {
            carteira.setEmpresaId(this.id); // Define a FK na carteira
            this.carteiras.add(carteira);
        }
    }
    
    public void removerCarteira(Carteira carteira) {
        this.carteiras.remove(carteira);
    }
    
    // Calcula o saldo total de todas as carteiras
    public double calcularSaldoTotal() {
        return carteiras.stream().mapToDouble(c -> c.getSaldo()).sum();
    }
    
    @Override
    public String toString() {
        return "Empresa{id=" + id + ", cnpj='" + cnpj + "', razaoSocial='" + razaoSocial + 
               "', qtdCarteiras=" + carteiras.size() + "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public List<Carteira> getCarteiras() {
        return carteiras;
    }

    public void setCarteiras(List<Carteira> carteiras) {
        this.carteiras = carteiras;
    }
}