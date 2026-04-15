package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.EmpresaDAO;
import com.voltz.criptoinvest.model.Empresa;

import java.sql.SQLException;
import java.util.List;

public class ListagemEmpresaView {
    public static void main(String[] args) {
        try{
            EmpresaDAO dao = new EmpresaDAO();
            List<Empresa> empresas = dao.listarEmpresas();
            for (Empresa empresa : empresas) {
                System.out.println(empresa.getId() + " - CNPJ:" + empresa.getCnpj() + ", Razão Social: " + empresa.getRazaoSocial());
                // TODO: Adicionar o ID da Carteira assim que a tabela T_CARTEIRA estiver integrada.
                //  System.out.println(empresa.getId() + " - CNPJ:" + empresa.getCnpj() + ", Razão Social: " + empresa.getRazaoSocial() + ", Carteiras: " + empresa.getCarteiras());
            }
            dao.fecharConexao();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
