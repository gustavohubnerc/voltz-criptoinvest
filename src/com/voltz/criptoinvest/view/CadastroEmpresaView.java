package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.EmpresaDAO;
import com.voltz.criptoinvest.model.Empresa;

import java.sql.SQLException;

public class CadastroEmpresaView {
    public static void main(String[] args) {

        try{
            EmpresaDAO dao = new EmpresaDAO();
            /*
             TODO: Adicionar o ID da Carteira assim que a tabela T_CARTEIRA estiver integrada.
            Carteira carteira = new Carteira(2L);
            List<Carteira> lista = new ArrayList<>();
            lista.add(carteira);
            Empresa empresa = new Empresa("11111111111111", "Yas Technologies LTDA", lista);
            */
            Empresa empresa = new Empresa("22222222222222", "Antonielly LTDA");
            dao.cadastrarEmpresa(empresa);
            dao.fecharConexao();
            System.out.println("Empresa Cadastrada com sucesso");
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
}