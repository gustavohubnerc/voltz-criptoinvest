package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.InvestimentoDAO;
import com.voltz.criptoinvest.model.Investimento;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class InvestimentoConsoleView {

    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        InvestimentoDAO dao = new InvestimentoDAO();

        while (opcao != 0) {
            System.out.println("\n--- Módulo de Investimento ---");
            System.out.println("1. Incluir Investimento");
            System.out.println("2. Alterar Investimento");
            System.out.println("3. Excluir Investimento");
            System.out.println("4. Exibir todos os Investimentos");
            System.out.println("5. Exibir Investimento por ID");
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

    private void incluir(Scanner scanner, InvestimentoDAO dao) {
        System.out.print("Digite o Ativo (ex: BTC, ETH): ");
        String ativo = scanner.nextLine().trim();
        if (ativo.isEmpty() || ativo.length() > 10) {
            System.out.println("Erro: Ativo inválido.");
            return;
        }

        System.out.print("Digite a Quantidade: ");
        double quantidade = 0.0;
        try {
            quantidade = Double.parseDouble(scanner.nextLine());
            if (quantidade < 0) {
                System.out.println("Erro: Quantidade não pode ser negativa.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
            return;
        }

        System.out.print("Digite o Preço Médio (em R$): ");
        double precoMedio = 0.0;
        try {
            precoMedio = Double.parseDouble(scanner.nextLine());
            if (precoMedio < 0) {
                System.out.println("Erro: Preço médio não pode ser negativo.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
            return;
        }

        System.out.print("Digite o ID da Carteira vinculada: ");
        Long idCarteira = null;
        try {
            idCarteira = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID de Carteira inválido.");
            return;
        }

        Investimento investimento = new Investimento(null, ativo, quantidade, precoMedio, idCarteira);
        try {
            dao.cadastrar(investimento);
            System.out.println("Investimento cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar investimento: " + e.getMessage());
        }
    }

    private void alterar(Scanner scanner, InvestimentoDAO dao) {
        System.out.print("Digite o ID do Investimento que deseja alterar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Investimento inv = dao.buscarPorId(id);
            System.out.println("Investimento encontrado: " + inv.getAtivo() + " - Qtd: " + inv.getQuantidade());

            System.out.print("Digite o novo Ativo (deixe vazio para manter): ");
            String ativo = scanner.nextLine().trim();
            if (!ativo.isEmpty()) {
                if(ativo.length() > 10) { System.out.println("Erro: Ativo longo demais."); return; }
                inv.setAtivo(ativo);
            }

            System.out.print("Digite a nova Quantidade (deixe vazio para manter): ");
            String qtdStr = scanner.nextLine().trim();
            if (!qtdStr.isEmpty()) {
                double quantidade = Double.parseDouble(qtdStr);
                if (quantidade < 0) { System.out.println("Erro: Quantidade não pode ser negativa."); return; }
                inv.setQuantidade(quantidade);
            }

            System.out.print("Digite o novo Preço Médio (deixe vazio para manter): ");
            String precoStr = scanner.nextLine().trim();
            if (!precoStr.isEmpty()) {
                double precoMedio = Double.parseDouble(precoStr);
                if (precoMedio < 0) { System.out.println("Erro: Preço médio não pode ser negativo."); return; }
                inv.setPrecoMedio(precoMedio);
            }

            System.out.print("Digite o novo ID da Carteira (deixe vazio para manter): ");
            String idCarteiraStr = scanner.nextLine().trim();
            if (!idCarteiraStr.isEmpty()) {
                Long idCarteira = Long.parseLong(idCarteiraStr);
                inv.setCarteiraId(idCarteira);
            }

            dao.atualizar(inv);
            System.out.println("Investimento alterado com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao alterar investimento: " + e.getMessage());
        }
    }

    private void excluir(Scanner scanner, InvestimentoDAO dao) {
        System.out.print("Digite o ID do Investimento que deseja excluir: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            dao.remover(id);
            System.out.println("Investimento excluído com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao excluir investimento: " + e.getMessage());
        }
    }

    private void exibirTodos(InvestimentoDAO dao) {
        try {
            List<Investimento> lista = dao.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("Nenhum investimento cadastrado.");
            } else {
                System.out.println("\nLista de Investimentos:");
                for (Investimento inv : lista) {
                    System.out.println(inv);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar investimentos: " + e.getMessage());
        }
    }

    private void exibirPorId(Scanner scanner, InvestimentoDAO dao) {
        System.out.print("Digite o ID do Investimento: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Investimento inv = dao.buscarPorId(id);
            System.out.println(inv);
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar investimento: " + e.getMessage());
        }
    }
}
