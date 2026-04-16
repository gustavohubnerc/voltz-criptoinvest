package com.voltz.criptoinvest.dao;

import com.voltz.criptoinvest.db.OracleConnectionFactory;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.Carteira;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarteiraDAO {

    public void cadastrar(Carteira carteira) throws SQLException {
        String sql = "INSERT INTO T_CARTEIRA (endereco, custodiante, saldo, id_empresa) VALUES (?, ?, ?, ?)";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carteira.getEndereco());
            stmt.setString(2, carteira.getCustodiante());
            stmt.setDouble(3, carteira.getSaldo());
            stmt.setLong(4, carteira.getEmpresaId());
            stmt.executeUpdate();
        }
    }

    public Carteira buscarPorId(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM T_CARTEIRA WHERE id_carteira = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Carteira não encontrada!");
                }
                return parseCarteira(rs);
            }
        }
    }

    public List<Carteira> listarTodos() throws SQLException {
        String sql = "SELECT * FROM T_CARTEIRA";
        List<Carteira> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(parseCarteira(rs));
            }
        }
        return lista;
    }

    public List<Carteira> listarPorEmpresa(Long idEmpresa) throws SQLException {
        String sql = "SELECT * FROM T_CARTEIRA WHERE id_empresa = ?";
        List<Carteira> lista = new ArrayList<>();
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idEmpresa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(parseCarteira(rs));
                }
            }
        }
        return lista;
    }

    public void atualizar(Carteira carteira) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE T_CARTEIRA SET endereco = ?, custodiante = ?, saldo = ?, id_empresa = ? WHERE id_carteira = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carteira.getEndereco());
            stmt.setString(2, carteira.getCustodiante());
            stmt.setDouble(3, carteira.getSaldo());
            stmt.setLong(4, carteira.getEmpresaId());
            stmt.setLong(5, carteira.getId());
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Carteira não encontrada para atualização!");
            }
        }
    }

    public void remover(Long id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM T_CARTEIRA WHERE id_carteira = ?";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new EntidadeNaoEncontradaException("Carteira não encontrada para remoção!");
            }
        }
    }

    private Carteira parseCarteira(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id_carteira");
        String endereco = rs.getString("endereco");
        String custodiante = rs.getString("custodiante");
        double saldo = rs.getDouble("saldo");
        Long idEmpresa = rs.getLong("id_empresa");
        return new Carteira(id, endereco, custodiante, saldo, idEmpresa);
    }
}
