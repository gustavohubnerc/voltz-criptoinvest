package com.voltz.criptoinvest.model;

import com.voltz.criptoinvest.dao.EmpresaDAO;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.util.List;

import static com.voltz.criptoinvest.db.OracleConnectionFactory.testarConexao;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTÃO DE CRIPTOINVESTIMENTOS      ║");
        System.out.println("║   Teste de Integração com Banco de Dados Oracle ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // --- Teste de conexão ---
        System.out.println("=== ETAPA 0: TESTE DE CONEXÃO ===");
        try {
            testarConexao();
        } catch (Exception e) {
            System.err.println("✗ Falha na conexão com o banco. Encerrando testes.");
            System.err.println("  Erro: " + e.getMessage());
            return;
        }
        System.out.println();

        // --- Instanciar o DAO ---
        EmpresaDAO dao;
        try {
            dao = new EmpresaDAO();
        } catch (SQLException e) {
            System.err.println("✗ Não foi possível criar o EmpresaDAO: " + e.getMessage());
            return;
        }

        // --- Executar todos os testes em sequência ---
        try {
            Long idCriado = testarCadastro(dao);
            testarListagem(dao);

            if (idCriado != null) {
                testarPesquisaPorId(dao, idCriado);
                testarAtualizacao(dao, idCriado);
                testarRemocao(dao, idCriado);
            } else {
                System.out.println("\n⚠ Pulando testes de pesquisa, atualização e remoção (cadastro não retornou ID).");
                // Tenta com um ID fixo caso existam dados no banco
                testarPesquisaPorId(dao, 1L);
            }
        } finally {
            // --- Fechar conexão ---
            try {
                dao.fecharConexao();
                System.out.println("\n✓ Conexão com o banco fechada com sucesso.");
            } catch (SQLException e) {
                System.err.println("\n✗ Erro ao fechar conexão: " + e.getMessage());
            }
        }

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   TESTES FINALIZADOS                             ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    // ====================================================================
    // MÉTODO 1: Testar CADASTRO (INSERT)
    // ====================================================================
    /**
     * Testa a inserção de uma nova empresa no banco de dados.
     * 
     * @param dao instância de EmpresaDAO
     * @return o ID da empresa criada (obtido via listagem), ou null se não for
     *         possível recuperar
     */
    private static Long testarCadastro(EmpresaDAO dao) {
        System.out.println("=== ETAPA 1: TESTE DE CADASTRO (INSERT) ===");
        try {
            Empresa novaEmpresa = new Empresa("99999999000199", "Empresa Teste LTDA");
            dao.cadastrarEmpresa(novaEmpresa);
            System.out.println("✓ Empresa cadastrada com sucesso!");
            System.out.println("  CNPJ: " + novaEmpresa.getCnpj());
            System.out.println("  Razão Social: " + novaEmpresa.getRazaoSocial());

            // Recuperar o ID da empresa recém-criada buscando na listagem
            List<Empresa> todas = dao.listarEmpresas();
            for (Empresa emp : todas) {
                if ("99.999.999/0001-99".equals(emp.getCnpj())) {
                    System.out.println("  ID gerado pelo banco: " + emp.getId());
                    return emp.getId();
                }
            }
            System.out.println("  ⚠ Empresa inserida, mas não foi possível recuperar o ID.");
            return null;

        } catch (SQLException e) {
            System.err.println("✗ Erro ao cadastrar empresa: " + e.getMessage());
            return null;
        }
    }

    // ====================================================================
    // MÉTODO 2: Testar LISTAGEM (SELECT ALL)
    // ====================================================================
    /**
     * Testa a listagem de todas as empresas cadastradas no banco.
     * 
     * @param dao instância de EmpresaDAO
     */
    private static void testarListagem(EmpresaDAO dao) {
        System.out.println("\n=== ETAPA 2: TESTE DE LISTAGEM (SELECT ALL) ===");
        try {
            List<Empresa> empresas = dao.listarEmpresas();
            if (empresas.isEmpty()) {
                System.out.println("  ⚠ Nenhuma empresa encontrada no banco.");
            } else {
                System.out.println("✓ Total de empresas encontradas: " + empresas.size());
                System.out.println("  ┌──────┬────────────────────┬──────────────────────────────────┐");
                System.out.println("  │  ID  │       CNPJ         │         RAZÃO SOCIAL             │");
                System.out.println("  ├──────┼────────────────────┼──────────────────────────────────┤");
                for (Empresa emp : empresas) {
                    System.out.printf("  │ %4d │ %-18s │ %-32s │%n",
                            emp.getId(), emp.getCnpj(), emp.getRazaoSocial());
                }
                System.out.println("  └──────┴────────────────────┴──────────────────────────────────┘");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao listar empresas: " + e.getMessage());
        }
    }

    // ====================================================================
    // MÉTODO 3: Testar PESQUISA POR ID (SELECT WHERE)
    // ====================================================================
    /**
     * Testa a pesquisa de uma empresa específica pelo ID.
     * 
     * @param dao instância de EmpresaDAO
     * @param id  ID da empresa a ser pesquisada
     */
    private static void testarPesquisaPorId(EmpresaDAO dao, Long id) {
        System.out.println("\n=== ETAPA 3: TESTE DE PESQUISA POR ID (SELECT WHERE id = " + id + ") ===");
        try {
            Empresa empresa = dao.pesquisarEmpresa(id);
            System.out.println("✓ Empresa encontrada:");
            System.out.println("  ID: " + empresa.getId());
            System.out.println("  CNPJ: " + empresa.getCnpj());
            System.out.println("  Razão Social: " + empresa.getRazaoSocial());

        } catch (EntidadeNaoEncontradaException e) {
            System.err.println("✗ Empresa com ID " + id + " não encontrada: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Erro ao pesquisar empresa: " + e.getMessage());
        }
    }

    // ====================================================================
    // MÉTODO 4: Testar ATUALIZAÇÃO (UPDATE)
    // ====================================================================
    /**
     * Testa a atualização dos dados de uma empresa existente.
     * Altera CNPJ e Razão Social, depois verifica se a mudança foi persistida.
     * 
     * @param dao instância de EmpresaDAO
     * @param id  ID da empresa a ser atualizada
     */
    private static void testarAtualizacao(EmpresaDAO dao, Long id) {
        System.out.println("\n=== ETAPA 4: TESTE DE ATUALIZAÇÃO (UPDATE) ===");
        try {
            // Buscar empresa original
            Empresa empresa = dao.pesquisarEmpresa(id);
            String cnpjOriginal = empresa.getCnpj();
            String razaoOriginal = empresa.getRazaoSocial();

            System.out.println("  Antes da atualização:");
            System.out.println("    CNPJ: " + cnpjOriginal);
            System.out.println("    Razão Social: " + razaoOriginal);

            // Alterar dados
            empresa.setCnpj("88.888.888/0001-88");
            empresa.setRazaoSocial("Empresa Atualizada Teste LTDA");
            dao.atualizarEmpresa(empresa);

            // Verificar se a atualização foi persistida
            Empresa empresaAtualizada = dao.pesquisarEmpresa(id);
            System.out.println("\n  Depois da atualização:");
            System.out.println("    CNPJ: " + empresaAtualizada.getCnpj());
            System.out.println("    Razão Social: " + empresaAtualizada.getRazaoSocial());

            // Validar
            if ("88.888.888/0001-88".equals(empresaAtualizada.getCnpj()) &&
                    "Empresa Atualizada Teste LTDA".equals(empresaAtualizada.getRazaoSocial())) {
                System.out.println("\n✓ Atualização verificada com sucesso! Dados foram persistidos.");
            } else {
                System.out.println("\n⚠ Atualização executada, mas a verificação não conferiu.");
            }

        } catch (EntidadeNaoEncontradaException e) {
            System.err.println("✗ Empresa com ID " + id + " não encontrada para atualização: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Erro ao atualizar empresa: " + e.getMessage());
        }
    }

    // ====================================================================
    // MÉTODO 5: Testar REMOÇÃO (DELETE)
    // ====================================================================
    /**
     * Testa a remoção de uma empresa do banco de dados e verifica se foi realmente
     * excluída.
     * 
     * @param dao instância de EmpresaDAO
     * @param id  ID da empresa a ser removida
     */
    private static void testarRemocao(EmpresaDAO dao, Long id) {
        System.out.println("\n=== ETAPA 5: TESTE DE REMOÇÃO (DELETE) ===");
        try {
            dao.removerEmpresa(id);
            System.out.println("✓ Empresa com ID " + id + " removida com sucesso!");

            // Verificar se a empresa realmente foi excluída
            try {
                dao.pesquisarEmpresa(id);
                System.out.println("⚠ Atenção: empresa ainda encontrada após remoção!");
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("✓ Verificação confirmada: empresa não existe mais no banco.");
            }

        } catch (EntidadeNaoEncontradaException e) {
            System.err.println("✗ Empresa com ID " + id + " não encontrada para remoção: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("✗ Erro ao remover empresa: " + e.getMessage());
        }
    }
}