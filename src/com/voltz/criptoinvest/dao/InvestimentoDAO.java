package com.voltz.criptoinvest.dao;

import com.voltz.criptoinvest.db.OracleConnectionFactory;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.Investimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestimentoDAO {

    public void cadastrar(Investimento investimento) throws SQLException {
        String sql = "INSERT INTO T_INVESTIMENTO (ativo, quantidade, preco_medio, id_carteira) VALUES (?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, investimento.getAtivo());
            stmt.setDouble(2, investimento.getQuantidade());
            stmt.setDouble(3, investimento.getPrecoMedio());
            stmt.setLong(4, investimento.getCarteiraId());
            stmt.executeUpdate();
        }
    }

    public Investimento buscarPorId(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM T_INVESTIMENTO WHERE id_investimento = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Investimento não encontrado!");
                }
                return parseInvestimento(rs);
            }
        }
    }

    public List<Investimento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM T_INVESTIMENTO";
        List<Investimento> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(parseInvestimento(rs));
            }
        }
        return lista;
    }

    public List<Investimento> listarPorCarteira(Long idCarteira) throws SQLException {
        String sql = "SELECT * FROM T_INVESTIMENTO WHERE id_carteira = ?";
        List<Investimento> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idCarteira);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(parseInvestimento(rs));
                }
            }
        }
        return lista;
    }

    public void atualizar(Investimento investimento) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE T_INVESTIMENTO SET ativo = ?, quantidade = ?, preco_medio = ?, id_carteira = ? WHERE id_investimento = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, investimento.getAtivo());
            stmt.setDouble(2, investimento.getQuantidade());
            stmt.setDouble(3, investimento.getPrecoMedio());
            stmt.setLong(4, investimento.getCarteiraId());
            stmt.setLong(5, investimento.getId());
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Investimento não encontrado para atualização!");
            }
        }
    }

    public void remover(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM T_INVESTIMENTO WHERE id_investimento = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Investimento não encontrado para remoção!");
            }
        }
    }

    private Investimento parseInvestimento(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id_investimento");
        String ativo = rs.getString("ativo");
        double quantidade = rs.getDouble("quantidade");
        double precoMedio = rs.getDouble("preco_medio");
        Long idCarteira = rs.getLong("id_carteira");
        return new Investimento(id, ativo, quantidade, precoMedio, idCarteira);
    }
}
