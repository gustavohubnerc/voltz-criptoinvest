package com.voltz.criptoinvest.dao;

import com.voltz.criptoinvest.db.OracleConnectionFactory;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.Transacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    public void cadastrar(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO T_TRANSACAO (tipo, ativo, quantidade, preco, data_transacao, status, id_carteira, id_usuario, id_investimento) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transacao.getTipo());
            stmt.setString(2, transacao.getAtivo());
            stmt.setDouble(3, transacao.getQuantidade());
            stmt.setDouble(4, transacao.getPreco());
            stmt.setTimestamp(5, transacao.getData() != null ? Timestamp.valueOf(transacao.getData()) : null);
            stmt.setString(6, transacao.getStatus());
            stmt.setLong(7, transacao.getCarteiraId());
            stmt.setLong(8, transacao.getUsuarioId());
            if (transacao.getInvestimentoId() != null) {
                stmt.setLong(9, transacao.getInvestimentoId());
            } else {
                stmt.setNull(9, Types.NUMERIC);
            }
            stmt.executeUpdate();
        }
    }

    public Transacao buscarPorId(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM T_TRANSACAO WHERE id_transacao = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Transação não encontrada!");
                }
                return parseTransacao(rs);
            }
        }
    }

    public List<Transacao> listarTodos() throws SQLException {
        String sql = "SELECT * FROM T_TRANSACAO";
        List<Transacao> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(parseTransacao(rs));
            }
        }
        return lista;
    }

    public List<Transacao> listarPorCarteira(Long idCarteira) throws SQLException {
        String sql = "SELECT * FROM T_TRANSACAO WHERE id_carteira = ?";
        List<Transacao> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idCarteira);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(parseTransacao(rs));
                }
            }
        }
        return lista;
    }

    public void atualizar(Transacao transacao) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE T_TRANSACAO SET tipo = ?, ativo = ?, quantidade = ?, preco = ?, data_transacao = ?, status = ?, "
                   + "id_carteira = ?, id_usuario = ?, id_investimento = ? WHERE id_transacao = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transacao.getTipo());
            stmt.setString(2, transacao.getAtivo());
            stmt.setDouble(3, transacao.getQuantidade());
            stmt.setDouble(4, transacao.getPreco());
            stmt.setTimestamp(5, transacao.getData() != null ? Timestamp.valueOf(transacao.getData()) : null);
            stmt.setString(6, transacao.getStatus());
            stmt.setLong(7, transacao.getCarteiraId());
            stmt.setLong(8, transacao.getUsuarioId());
            if (transacao.getInvestimentoId() != null) {
                stmt.setLong(9, transacao.getInvestimentoId());
            } else {
                stmt.setNull(9, Types.NUMERIC);
            }
            stmt.setLong(10, transacao.getId());
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Transação não encontrada para atualização!");
            }
        }
    }

    public void remover(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM T_TRANSACAO WHERE id_transacao = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Transação não encontrada para remoção!");
            }
        }
    }

    private Transacao parseTransacao(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id_transacao");
        String tipo = rs.getString("tipo");
        String ativo = rs.getString("ativo");
        double quantidade = rs.getDouble("quantidade");
        double preco = rs.getDouble("preco");
        Timestamp ts = rs.getTimestamp("data_transacao");
        LocalDateTime data = ts != null ? ts.toLocalDateTime() : null;
        String status = rs.getString("status");
        Long idCarteira = rs.getLong("id_carteira");
        Long idUsuario = rs.getLong("id_usuario");
        long idInvestimentoRaw = rs.getLong("id_investimento");
        Long idInvestimento = rs.wasNull() ? null : idInvestimentoRaw;
        return new Transacao(id, tipo, ativo, quantidade, preco, data, status, idCarteira, idUsuario, idInvestimento);
    }
}
