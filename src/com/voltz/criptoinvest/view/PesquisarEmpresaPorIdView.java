package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.EmpresaDAO;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.Empresa;

import java.sql.SQLException;

public class PesquisarEmpresaPorIdView {
    public static void main(String[] args) {
        try {
            EmpresaDAO dao = new EmpresaDAO();
            Empresa empresa = dao.pesquisarEmpresa(2L);
            System.out.println(empresa.getId() + " - CNPJ:" + empresa.getCnpj() + ", Razão Social: " + empresa.getRazaoSocial());

            // TODO: Adicionar o ID da Carteira assim que a tabela T_CARTEIRA estiver integrada.
            //  System.out.println(empresa.getId() + " - CNPJ:" + empresa.getCnpj() + ", Razão Social: " + empresa.getRazaoSocial() + ", Carteiras: " + empresa.getCarteiras());
            dao.fecharConexao();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }catch (EntidadeNaoEncontradaException e){
            System.err.println("Código não existe!");
        }
    }
}
