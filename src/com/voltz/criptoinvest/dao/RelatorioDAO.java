package com.voltz.criptoinvest.dao;

import com.voltz.criptoinvest.db.OracleConnectionFactory;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.Relatorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {

    public void cadastrar(Relatorio relatorio) throws SQLException {
        String sql = "INSERT INTO T_RELATORIO (titulo, data_geracao, tipo, id_empresa, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, relatorio.getTitulo());
            stmt.setTimestamp(2, relatorio.getDataGeracao() != null ? Timestamp.valueOf(relatorio.getDataGeracao()) : null);
            stmt.setString(3, relatorio.getTipo());
            stmt.setLong(4, relatorio.getEmpresaId());
            stmt.setLong(5, relatorio.getUsuarioId());
            stmt.executeUpdate();
        }
    }

    public Relatorio buscarPorId(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM T_RELATORIO WHERE id_relatorio = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Relatório não encontrado!");
                }
                return parseRelatorio(rs);
            }
        }
    }

    public List<Relatorio> listarTodos() throws SQLException {
        String sql = "SELECT * FROM T_RELATORIO";
        List<Relatorio> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(parseRelatorio(rs));
            }
        }
        return lista;
    }

    public List<Relatorio> listarPorEmpresa(Long idEmpresa) throws SQLException {
        String sql = "SELECT * FROM T_RELATORIO WHERE id_empresa = ?";
        List<Relatorio> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idEmpresa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(parseRelatorio(rs));
                }
            }
        }
        return lista;
    }

    public void atualizar(Relatorio relatorio) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE T_RELATORIO SET titulo = ?, data_geracao = ?, tipo = ?, id_empresa = ?, id_usuario = ? WHERE id_relatorio = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, relatorio.getTitulo());
            stmt.setTimestamp(2, relatorio.getDataGeracao() != null ? Timestamp.valueOf(relatorio.getDataGeracao()) : null);
            stmt.setString(3, relatorio.getTipo());
            stmt.setLong(4, relatorio.getEmpresaId());
            stmt.setLong(5, relatorio.getUsuarioId());
            stmt.setLong(6, relatorio.getId());
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Relatório não encontrado para atualização!");
            }
        }
    }

    public void remover(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM T_RELATORIO WHERE id_relatorio = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Relatório não encontrado para remoção!");
            }
        }
    }

    private Relatorio parseRelatorio(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id_relatorio");
        String titulo = rs.getString("titulo");
        Timestamp ts = rs.getTimestamp("data_geracao");
        LocalDateTime dataGeracao = ts != null ? ts.toLocalDateTime() : null;
        String tipo = rs.getString("tipo");
        Long idEmpresa = rs.getLong("id_empresa");
        Long idUsuario = rs.getLong("id_usuario");
        return new Relatorio(id, titulo, dataGeracao, tipo, idEmpresa, idUsuario);
    }
}
