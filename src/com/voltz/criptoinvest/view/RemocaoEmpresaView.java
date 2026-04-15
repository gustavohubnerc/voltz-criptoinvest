package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.EmpresaDAO;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;

public class RemocaoEmpresaView {
    public static void main(String[] args) {
        try{
            EmpresaDAO dao = new EmpresaDAO();
            dao.removerEmpresa(2L);
            dao.fecharConexao();
            System.out.println("Empresa removida com sucesso!");
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }catch(EntidadeNaoEncontradaException e){
            System.err.println("Produto não encontrado!");
        }
    }
}
