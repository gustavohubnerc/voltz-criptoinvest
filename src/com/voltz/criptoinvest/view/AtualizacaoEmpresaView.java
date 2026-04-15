package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.EmpresaDAO;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;
import com.voltz.criptoinvest.model.Empresa;

import java.sql.SQLException;

public class AtualizacaoEmpresaView {
    public static void main(String[] args) {
        try{
            EmpresaDAO dao = new EmpresaDAO();
            Empresa empresa = dao.pesquisarEmpresa(1L);
            empresa.setCnpj("11111111111110");
            empresa.setRazaoSocial("Yasmim LTDA");
            dao.atualizarEmpresa(empresa);
            dao.fecharConexao();
            System.out.println("Empresa atualizada com sucesso!");
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }catch(EntidadeNaoEncontradaException e){
            System.err.println("Empresa não existente!");
        }

    }
}
