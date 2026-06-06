package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.RelatorioDAO;
import com.voltz.criptoinvest.model.Relatorio;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class RelatorioConsoleView {

    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        RelatorioDAO dao = new RelatorioDAO();

        while (opcao != 0) {
            System.out.println("\n--- Módulo de Relatório ---");
            System.out.println("1. Incluir Relatório");
            System.out.println("2. Alterar Relatório");
            System.out.println("3. Excluir Relatório");
            System.out.println("4. Exibir todos os Relatórios");
            System.out.println("5. Exibir Relatório por ID");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1:
                    incluir(scanner, dao);
                    break;
                case 2:
                    alterar(scanner, dao);
                    break;
                case 3:
                    excluir(scanner, dao);
                    break;
                case 4:
                    exibirTodos(dao);
                    break;
                case 5:
                    exibirPorId(scanner, dao);
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void incluir(Scanner scanner, RelatorioDAO dao) {
        System.out.print("Digite o Título do Relatório: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isEmpty() || titulo.length() > 150) {
            System.out.println("Erro: Título inválido.");
            return;
        }

        System.out.print("Digite o Tipo (CONSOLIDADO, CARTEIRA, PERFORMANCE): ");
        String tipo = scanner.nextLine().trim().toUpperCase();
        if (!tipo.equals("CONSOLIDADO") && !tipo.equals("CARTEIRA") && !tipo.equals("PERFORMANCE")) {
            System.out.println("Erro: Tipo inválido. Deve ser CONSOLIDADO, CARTEIRA ou PERFORMANCE.");
            return;
        }

        System.out.print("Digite o ID da Empresa: ");
        Long idEmpresa = null;
        try {
            idEmpresa = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID de Empresa inválido.");
            return;
        }

        System.out.print("Digite o ID do Usuário (Gerador do Relatório): ");
        Long idUsuario = null;
        try {
            idUsuario = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID de Usuário inválido.");
            return;
        }

        Relatorio relatorio = new Relatorio(null, titulo, LocalDateTime.now(), tipo, idEmpresa, idUsuario);
        try {
            dao.cadastrar(relatorio);
            System.out.println("Relatório cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar relatório: " + e.getMessage());
        }
    }

    private void alterar(Scanner scanner, RelatorioDAO dao) {
        System.out.print("Digite o ID do Relatório que deseja alterar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Relatorio r = dao.buscarPorId(id);
            System.out.println("Relatório encontrado: " + r.getTitulo() + " (" + r.getTipo() + ")");

            System.out.print("Digite o novo Título (vazio para manter): ");
            String titulo = scanner.nextLine().trim();
            if (!titulo.isEmpty()) {
                if(titulo.length() > 150) { System.out.println("Erro: Título longo demais."); return; }
                r.setTitulo(titulo);
            }

            System.out.print("Digite o novo Tipo (CONSOLIDADO, CARTEIRA, PERFORMANCE) ou vazio para manter: ");
            String tipo = scanner.nextLine().trim().toUpperCase();
            if (!tipo.isEmpty()) {
                if (!tipo.equals("CONSOLIDADO") && !tipo.equals("CARTEIRA") && !tipo.equals("PERFORMANCE")) {
                    System.out.println("Erro: Tipo inválido."); return;
                }
                r.setTipo(tipo);
            }

            dao.atualizar(r);
            System.out.println("Relatório alterado com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao alterar relatório: " + e.getMessage());
        }
    }

    private void excluir(Scanner scanner, RelatorioDAO dao) {
        System.out.print("Digite o ID do Relatório que deseja excluir: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            dao.remover(id);
            System.out.println("Relatório excluído com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao excluir relatório: " + e.getMessage());
        }
    }

    private void exibirTodos(RelatorioDAO dao) {
        try {
            List<Relatorio> lista = dao.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("Nenhum relatório cadastrado.");
            } else {
                System.out.println("\nLista de Relatórios:");
                for (Relatorio r : lista) {
                    System.out.println(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar relatórios: " + e.getMessage());
        }
    }

    private void exibirPorId(Scanner scanner, RelatorioDAO dao) {
        System.out.print("Digite o ID do Relatório: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Relatorio r = dao.buscarPorId(id);
            System.out.println(r);
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar relatório: " + e.getMessage());
        }
    }
}
