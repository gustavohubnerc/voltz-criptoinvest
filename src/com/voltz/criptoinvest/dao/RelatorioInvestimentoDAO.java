package com.voltz.criptoinvest.dao;

import com.voltz.criptoinvest.db.OracleConnectionFactory;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.RelatorioInvestimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatorioInvestimentoDAO {

    public void cadastrar(RelatorioInvestimento ri) throws SQLException {
        String sql = "INSERT INTO T_RELATORIO_INVESTIMENTO (id_relatorio, id_investimento, data_inclusao, preco_mercado_capturado, observacoes) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ri.getRelatorioId());
            stmt.setLong(2, ri.getInvestimentoId());
            stmt.setTimestamp(3, ri.getDataInclusao() != null ? Timestamp.valueOf(ri.getDataInclusao()) : null);
            stmt.setDouble(4, ri.getPrecoMercadoCapturado());
            stmt.setString(5, ri.getObservacoes());
            stmt.executeUpdate();
        }
    }

    public RelatorioInvestimento buscar(Long idRelatorio, Long idInvestimento) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM T_RELATORIO_INVESTIMENTO WHERE id_relatorio = ? AND id_investimento = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idRelatorio);
            stmt.setLong(2, idInvestimento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Associação relatório-investimento não encontrada!");
                }
                return parse(rs);
            }
        }
    }

    public List<RelatorioInvestimento> listarPorRelatorio(Long idRelatorio) throws SQLException {
        String sql = "SELECT * FROM T_RELATORIO_INVESTIMENTO WHERE id_relatorio = ?";
        List<RelatorioInvestimento> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idRelatorio);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(parse(rs));
                }
            }
        }
        return lista;
    }

    public List<RelatorioInvestimento> listarPorInvestimento(Long idInvestimento) throws SQLException {
        String sql = "SELECT * FROM T_RELATORIO_INVESTIMENTO WHERE id_investimento = ?";
        List<RelatorioInvestimento> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idInvestimento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(parse(rs));
                }
            }
        }
        return lista;
    }

    public void atualizar(RelatorioInvestimento ri) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE T_RELATORIO_INVESTIMENTO SET data_inclusao = ?, preco_mercado_capturado = ?, observacoes = ? "
                   + "WHERE id_relatorio = ? AND id_investimento = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, ri.getDataInclusao() != null ? Timestamp.valueOf(ri.getDataInclusao()) : null);
            stmt.setDouble(2, ri.getPrecoMercadoCapturado());
            stmt.setString(3, ri.getObservacoes());
            stmt.setLong(4, ri.getRelatorioId());
            stmt.setLong(5, ri.getInvestimentoId());
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Associação relatório-investimento não encontrada para atualização!");
            }
        }
    }

    public void remover(Long idRelatorio, Long idInvestimento) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM T_RELATORIO_INVESTIMENTO WHERE id_relatorio = ? AND id_investimento = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idRelatorio);
            stmt.setLong(2, idInvestimento);
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Associação relatório-investimento não encontrada para remoção!");
            }
        }
    }

    private RelatorioInvestimento parse(ResultSet rs) throws SQLException {
        Long idRelatorio = rs.getLong("id_relatorio");
        Long idInvestimento = rs.getLong("id_investimento");
        Timestamp ts = rs.getTimestamp("data_inclusao");
        LocalDateTime dataInclusao = ts != null ? ts.toLocalDateTime() : null;
        double precoMercadoCapturado = rs.getDouble("preco_mercado_capturado");
        String observacoes = rs.getString("observacoes");
        return new RelatorioInvestimento(idRelatorio, idInvestimento, dataInclusao, precoMercadoCapturado, observacoes);
    }
}
