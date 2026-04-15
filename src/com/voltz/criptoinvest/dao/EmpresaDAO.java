package com.voltz.criptoinvest.dao;

import com.voltz.criptoinvest.db.OracleConnectionFactory;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.Carteira;
import com.voltz.criptoinvest.model.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {
    private Connection conexao;
    public void cadastrarEmpresa(Empresa empresa) throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("INSERT INTO tb_empresa (id_empresa, cnpj_empresa, razao_social) VALUES (seq_empresa.nextval, ?, ?)");
        stmt.setString(1, empresa.getCnpj());
        stmt.setString(2, empresa.getRazaoSocial());
        // TODO: Adicionar o ID da Carteira assim que a tabela TB_CARTEIRA estiver integrada.
        // stmt.setLong(3, empresa.getCarteiras().get(0).getId());
        stmt.executeUpdate();
    }

    public void fecharConexao() throws SQLException {
        conexao.close();
    }

    public Empresa pesquisarEmpresa(Long id) throws  SQLException, EntidadeNaoEncontradaException {
        PreparedStatement stm = conexao.prepareStatement("SELECT * FROM TB_EMPRESA WHERE ID_EMPRESA = ?");
        stm.setLong(1, id);
        ResultSet result = stm.executeQuery();
        if (!result.next())
        throw new EntidadeNaoEncontradaException("Empresa não encontrado!");
        return parseEmpresa(result);
    }

    public List<Empresa> listarEmpresas() throws SQLException {
        PreparedStatement stm = conexao.prepareStatement("SELECT * FROM TB_EMPRESA");
        ResultSet result = stm.executeQuery();
        List<Empresa> lista = new ArrayList<>();
        while (result.next()) {
            lista.add(parseEmpresa(result));
        }
        return lista;
    }

    public void atualizarEmpresa(Empresa empresa) throws  SQLException {
        PreparedStatement stm = conexao.prepareStatement("UPDATE TB_EMPRESA SET cnpj_empresa = ?, razao_social = ? WHERE id_empresa = ?");
        stm.setString(1, empresa.getCnpj());
        stm.setString(2, empresa.getRazaoSocial());
        stm.setLong(3, empresa.getId());
        // TODO: Adicionar Carteira assim que a tabela TB_CARTEIRA estiver integrada.
        stm.executeUpdate();
    }

    public void removerEmpresa(Long id) throws  SQLException, EntidadeNaoEncontradaException {
        PreparedStatement stm = conexao.prepareStatement("DELETE FROM TB_EMPRESA WHERE id_empresa = ?");
        stm.setLong(1, id);
        int linha = stm.executeUpdate();
        if (linha == 0)
            throw new EntidadeNaoEncontradaException("Empresa não encontrada para ser removida");
    }

    private Empresa parseEmpresa(ResultSet result) throws SQLException {
        Long id = result.getLong("id_empresa");
        String cnpj = result.getString("cnpj_empresa");
        String razaoSocial = result.getString("razao_social");
        return new Empresa(id, cnpj, razaoSocial);
        // TODO: Adicionar o ID da Carteira assim que a tabela TB_CARTEIRA estiver integrada.
        //long idCarteira = result.getLong("id_carteira");
        //List<Carteira> listaRef = new ArrayList<>();
        //listaRef.add(new Carteira(idCarteira));
        //return new Empresa(id, cnpj, razaoSocial, listaRef);
    }

    public EmpresaDAO() throws SQLException {
        conexao = OracleConnectionFactory.getConnection();
    }
}
