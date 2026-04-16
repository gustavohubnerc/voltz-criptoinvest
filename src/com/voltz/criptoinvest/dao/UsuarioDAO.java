package com.voltz.criptoinvest.dao;

import com.voltz.criptoinvest.db.OracleConnectionFactory;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void cadastrar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO T_USUARIO (nome, email, senha_hash, papel, id_empresa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenhaHash());
            stmt.setString(4, usuario.getPapel());
            if (usuario.getEmpresaId() != null) {
                stmt.setLong(5, usuario.getEmpresaId());
            } else {
                stmt.setNull(5, Types.NUMERIC);
            }
            stmt.executeUpdate();
        }
    }

    public Usuario buscarPorId(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM T_USUARIO WHERE id_usuario = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Usuário não encontrado!");
                }
                return parseUsuario(rs);
            }
        }
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM T_USUARIO";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(parseUsuario(rs));
            }
        }
        return lista;
    }

    public void atualizar(Usuario usuario) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE T_USUARIO SET nome = ?, email = ?, senha_hash = ?, papel = ?, id_empresa = ? WHERE id_usuario = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenhaHash());
            stmt.setString(4, usuario.getPapel());
            if (usuario.getEmpresaId() != null) {
                stmt.setLong(5, usuario.getEmpresaId());
            } else {
                stmt.setNull(5, Types.NUMERIC);
            }
            stmt.setLong(6, usuario.getId());
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Usuário não encontrado para atualização!");
            }
        }
    }

    public void remover(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM T_USUARIO WHERE id_usuario = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Usuário não encontrado para remoção!");
            }
        }
    }

    private Usuario parseUsuario(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id_usuario");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senhaHash = rs.getString("senha_hash");
        String papel = rs.getString("papel");
        long idEmpresaRaw = rs.getLong("id_empresa");
        Long idEmpresa = rs.wasNull() ? null : idEmpresaRaw;
        return new Usuario(id, nome, email, senhaHash, papel, idEmpresa);
    }
}
