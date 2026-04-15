package com.voltz.criptoinvest.model;



/**
 * Entidade USUARIO
 * Representa uma pessoa autorizada para acessar o sistema.
 * * Chave Primária: id (Long)
 * Chave Estrangeira: empresaId (Long) - referencia EMPRESA.cnpj (convertido para Long por simplicidade)
 */
public class Usuario extends Pessoa {
    // CHAVE PRIMÁRIA
    private Long id; // PK - Identificador único do usuário
    
    // ATRIBUTOS
    private String senhaHash; // Senha criptografada
    private String papel; // Papel/Perfil do usuário (ex: admin, gestor, operador)
    
    // CHAVE ESTRANGEIRA
    private Long empresaId; // FK - Referencia a empresa do usuário (EMPRESA.id)
    
    // Construtor completo
    public Usuario(Long id, String nome, String email, String senhaHash, String papel, Long empresaId) {
        super(nome, email);
        this.id = id;
        this.senhaHash = senhaHash;
        this.papel = papel;
        this.empresaId = empresaId;
    }
    
    // Construtor sem empresaId (para usuários sem empresa vinculada)
    public Usuario(Long id, String nome, String email, String senhaHash, String papel) {
        this(id, nome, email, senhaHash, papel, null);
    }

    // Método de negócio - valida a senha informada
    public boolean autenticar(String senhaHash) {
        return this.senhaHash.equals(senhaHash);
    }
    
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "', email='" + email + 
               "', papel='" + papel + "', empresaId=" + empresaId + "}";
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
}