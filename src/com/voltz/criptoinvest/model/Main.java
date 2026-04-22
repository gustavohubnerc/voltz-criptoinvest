package com.voltz.criptoinvest.model;

import com.voltz.criptoinvest.dao.*;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.voltz.criptoinvest.db.OracleConnectionFactory.testarConexao;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTÃO DE CRIPTOINVESTIMENTOS       ║");
        System.out.println("║   Teste de Integração com Banco de Dados Oracle  ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        System.out.println("=== ETAPA 0: TESTE DE CONEXÃO ===");
        try {
            testarConexao();
        } catch (Exception e) {
            System.err.println("✗ Falha na conexão com o banco. Encerrando testes.");
            e.printStackTrace();
            return;
        }
        System.out.println();

        EmpresaDAO empresaDAO = null;
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        CarteiraDAO carteiraDAO = new CarteiraDAO();
        InvestimentoDAO investimentoDAO = new InvestimentoDAO();
        RelatorioDAO relatorioDAO = new RelatorioDAO();
        TransacaoDAO transacaoDAO = new TransacaoDAO();
        RelatorioInvestimentoDAO relatorioInvestimentoDAO = new RelatorioInvestimentoDAO();

        try {
            empresaDAO = new EmpresaDAO();

            // IDs que vamos preencher durante a criação
            Long idEmpresa = null;
            Long idUsuario = null;
            Long idCarteira = null;
            Long idInvestimento = null;
            Long idRelatorio = null;
            Long idTransacao = null;

            System.out.println("=== ETAPA 1: TESTE DE INSERÇÃO E LEITURA (CREATE & READ) ===");
            
            // 1. Empresa
            try {
                Empresa empresa = new Empresa("11222333000199", "Empresa Holding Cripto");
                empresaDAO.cadastrarEmpresa(empresa);
                for (Empresa e : empresaDAO.listarEmpresas()) {
                    if (e.getCnpj().equals("11222333000199")) idEmpresa = e.getId();
                }
                if (idEmpresa != null) System.out.println("✓ Empresa criada (ID: " + idEmpresa + ")");
            } catch (SQLException e) { System.err.println("Erro Empresa: " + e.getMessage()); }

            if (idEmpresa == null) throw new RuntimeException("Falha ao criar Empresa base, abortando restante.");

            // 2. Usuario
            try {
                Usuario usuario = new Usuario(null, "João Cripto", "joao@cripto.com", "hash123", "ADMIN", idEmpresa);
                usuarioDAO.cadastrar(usuario);
                for (Usuario u : usuarioDAO.listarTodos()) {
                    if (u.getEmail().equals("joao@cripto.com")) idUsuario = u.getId();
                }
                if (idUsuario != null) System.out.println("✓ Usuario criado (ID: " + idUsuario + ")");
            } catch (SQLException e) { System.err.println("Erro Usuario: " + e.getMessage()); }

            // 3. Carteira
            try {
                Carteira carteira = new Carteira(null, "0xABC123", "Binance", 1000.50, idEmpresa);
                carteiraDAO.cadastrar(carteira);
                for (Carteira c : carteiraDAO.listarTodos()) {
                    if (c.getEndereco().equals("0xABC123")) idCarteira = c.getId();
                }
                if (idCarteira != null) System.out.println("✓ Carteira criada (ID: " + idCarteira + ")");
            } catch (SQLException e) { System.err.println("Erro Carteira: " + e.getMessage()); }

            // 4. Investimento
            try {
                if (idCarteira != null) {
                    Investimento investimento = new Investimento(null, "BTC", 0.5, 60000.0, idCarteira);
                    investimentoDAO.cadastrar(investimento);
                    for (Investimento inv : investimentoDAO.listarTodos()) {
                        if (inv.getAtivo().equals("BTC") && inv.getCarteiraId().equals(idCarteira)) idInvestimento = inv.getId();
                    }
                    if (idInvestimento != null) System.out.println("✓ Investimento criado (ID: " + idInvestimento + ")");
                }
            } catch (SQLException e) { System.err.println("Erro Investimento: " + e.getMessage()); }

            // 5. Relatorio
            try {
                if (idUsuario != null) {
                    Relatorio relatorio = new Relatorio(null, "Relatorio Q1", LocalDateTime.now(), "TRIMESTRAL", idEmpresa, idUsuario);
                    relatorioDAO.cadastrar(relatorio);
                    for (Relatorio r : relatorioDAO.listarTodos()) {
                        if (r.getTitulo().equals("Relatorio Q1")) idRelatorio = r.getId();
                    }
                    if (idRelatorio != null) System.out.println("✓ Relatorio criado (ID: " + idRelatorio + ")");
                }
            } catch (SQLException e) { System.err.println("Erro Relatorio: " + e.getMessage()); }

            // 6. Transacao
            try {
                if (idCarteira != null && idUsuario != null) {
                    Transacao transacao = new Transacao(null, "COMPRA", "BTC", 0.1, 61000.0, LocalDateTime.now(), "CONCLUIDA", idCarteira, idUsuario, idInvestimento);
                    transacaoDAO.cadastrar(transacao);
                    for (Transacao t : transacaoDAO.listarTodos()) {
                        if (t.getAtivo().equals("BTC") && "COMPRA".equals(t.getTipo())) idTransacao = t.getId();
                    }
                    if (idTransacao != null) System.out.println("✓ Transacao criada (ID: " + idTransacao + ")");
                }
            } catch (SQLException e) { System.err.println("Erro Transacao: " + e.getMessage()); }

            // 7. RelatorioInvestimento
            try {
                if (idRelatorio != null && idInvestimento != null) {
                    RelatorioInvestimento ri = new RelatorioInvestimento(idRelatorio, idInvestimento, LocalDateTime.now(), 62000.0, "Alta no fechamento");
                    relatorioInvestimentoDAO.cadastrar(ri);
                    System.out.println("✓ RelatorioInvestimento criado (Relatorio: " + idRelatorio + ", Investimento: " + idInvestimento + ")");
                }
            } catch (SQLException e) { System.err.println("Erro RelatorioInvestimento: " + e.getMessage()); }

            System.out.println("\n=== ETAPA 2: TESTE DE ATUALIZAÇÃO (UPDATE) ===");
            try {
                if (idRelatorio != null && idInvestimento != null) {
                    RelatorioInvestimento ri = relatorioInvestimentoDAO.buscar(idRelatorio, idInvestimento);
                    ri.setObservacoes("Atualizado: Alta de 5%");
                    relatorioInvestimentoDAO.atualizar(ri);
                    System.out.println("✓ RelatorioInvestimento atualizado.");
                }

                if (idTransacao != null) {
                    Transacao t = transacaoDAO.buscarPorId(idTransacao);
                    t.setStatus("CANCELADA");
                    transacaoDAO.atualizar(t);
                    System.out.println("✓ Transacao atualizada.");
                }

                if (idRelatorio != null) {
                    Relatorio r = relatorioDAO.buscarPorId(idRelatorio);
                    r.setTipo("MENSAL");
                    relatorioDAO.atualizar(r);
                    System.out.println("✓ Relatorio atualizado.");
                }

                if (idInvestimento != null) {
                    Investimento i = investimentoDAO.buscarPorId(idInvestimento);
                    i.setQuantidade(0.8);
                    investimentoDAO.atualizar(i);
                    System.out.println("✓ Investimento atualizado.");
                }

                if (idCarteira != null) {
                    Carteira c = carteiraDAO.buscarPorId(idCarteira);
                    c.setSaldo(2000.0);
                    carteiraDAO.atualizar(c);
                    System.out.println("✓ Carteira atualizada.");
                }

                if (idUsuario != null) {
                    Usuario u = usuarioDAO.buscarPorId(idUsuario);
                    u.setPapel("GESTOR");
                    usuarioDAO.atualizar(u);
                    System.out.println("✓ Usuario atualizado.");
                }

                if (idEmpresa != null) {
                    Empresa e = empresaDAO.pesquisarEmpresa(idEmpresa);
                    e.setRazaoSocial("Empresa Holding Editada");
                    empresaDAO.atualizarEmpresa(e);
                    System.out.println("✓ Empresa atualizada.");
                }

            } catch (Exception e) {
                System.err.println("Erro em Atualizacao: " + e.getMessage());
            }

            System.out.println("\n=== ETAPA 3: TESTE DE EXCLUSÃO (DELETE) ===");
            try {
                if (idRelatorio != null && idInvestimento != null) {
                    relatorioInvestimentoDAO.remover(idRelatorio, idInvestimento);
                    System.out.println("✓ RelatorioInvestimento removido.");
                }
                if (idTransacao != null) { transacaoDAO.remover(idTransacao); System.out.println("✓ Transacao removida."); }
                if (idRelatorio != null) { relatorioDAO.remover(idRelatorio); System.out.println("✓ Relatorio removido."); }
                if (idInvestimento != null) { investimentoDAO.remover(idInvestimento); System.out.println("✓ Investimento removido."); }
                if (idCarteira != null) { carteiraDAO.remover(idCarteira); System.out.println("✓ Carteira removida."); }
                if (idUsuario != null) { usuarioDAO.remover(idUsuario); System.out.println("✓ Usuario removido."); }
                if (idEmpresa != null) { empresaDAO.removerEmpresa(idEmpresa); System.out.println("✓ Empresa removida."); }
            } catch (Exception e) {
                System.err.println("Erro na exclusão em cadeia: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Erro geral de SQL: " + e.getMessage());
        } catch (RuntimeException re) {
            System.err.println(re.getMessage());
        } finally {
            if (empresaDAO != null) {
                try {
                    empresaDAO.fecharConexao();
                    System.out.println("\n✓ Conexão com o banco fechada com sucesso.");
                } catch (SQLException e) {
                    System.err.println("\n✗ Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   TESTES FINALIZADOS COM SUCESSO                 ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }
}