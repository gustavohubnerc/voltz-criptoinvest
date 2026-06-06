package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.TransacaoDAO;
import com.voltz.criptoinvest.model.Transacao;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TransacaoConsoleView {

    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        TransacaoDAO dao = new TransacaoDAO();

        while (opcao != 0) {
            System.out.println("\n--- Módulo de Transação ---");
            System.out.println("1. Incluir Transação");
            System.out.println("2. Alterar Transação");
            System.out.println("3. Excluir Transação");
            System.out.println("4. Exibir todas as Transações");
            System.out.println("5. Exibir Transação por ID");
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

    private void incluir(Scanner scanner, TransacaoDAO dao) {
        System.out.print("Digite o Tipo (COMPRA, VENDA, DEPOSITO, SAQUE): ");
        String tipo = scanner.nextLine().trim().toUpperCase();
        if (!tipo.equals("COMPRA") && !tipo.equals("VENDA") && !tipo.equals("DEPOSITO") && !tipo.equals("SAQUE")) {
            System.out.println("Erro: Tipo inválido.");
            return;
        }

        System.out.print("Digite o Ativo (ex: BTC, BRL): ");
        String ativo = scanner.nextLine().trim();
        if (ativo.isEmpty() || ativo.length() > 10) {
            System.out.println("Erro: Ativo inválido.");
            return;
        }

        System.out.print("Digite a Quantidade: ");
        double quantidade = 0.0;
        try {
            quantidade = Double.parseDouble(scanner.nextLine());
            if (quantidade < 0) { System.out.println("Erro: Quantidade não pode ser negativa."); return; }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
            return;
        }

        System.out.print("Digite o Preço: ");
        double preco = 0.0;
        try {
            preco = Double.parseDouble(scanner.nextLine());
            if (preco < 0) { System.out.println("Erro: Preço não pode ser negativo."); return; }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
            return;
        }

        System.out.print("Digite o Status (PENDENTE, CONFIRMADA, CANCELADA, FALHA) ou enter para CONFIRMADA: ");
        String status = scanner.nextLine().trim().toUpperCase();
        if (status.isEmpty()) {
            status = "CONFIRMADA";
        } else if (!status.equals("PENDENTE") && !status.equals("CONFIRMADA") && !status.equals("CANCELADA") && !status.equals("FALHA")) {
            System.out.println("Erro: Status inválido.");
            return;
        }

        System.out.print("Digite o ID da Carteira: ");
        Long idCarteira = null;
        try {
            idCarteira = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID de Carteira inválido.");
            return;
        }

        System.out.print("Digite o ID do Usuário: ");
        Long idUsuario = null;
        try {
            idUsuario = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID do Usuário inválido.");
            return;
        }

        System.out.print("Digite o ID do Investimento relacionado (ou enter para deixar vazio): ");
        String idInvStr = scanner.nextLine().trim();
        Long idInvestimento = null;
        if (!idInvStr.isEmpty()) {
            try {
                idInvestimento = Long.parseLong(idInvStr);
            } catch (NumberFormatException e) {
                System.out.println("Erro: ID de Investimento inválido.");
                return;
            }
        }

        Transacao t = new Transacao(null, tipo, ativo, quantidade, preco, LocalDateTime.now(), status, idCarteira, idUsuario, idInvestimento);
        try {
            dao.cadastrar(t);
            System.out.println("Transação cadastrada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar transação: " + e.getMessage());
        }
    }

    private void alterar(Scanner scanner, TransacaoDAO dao) {
        System.out.print("Digite o ID da Transação que deseja alterar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Transacao t = dao.buscarPorId(id);
            System.out.println("Transação encontrada: " + t.getTipo() + " de " + t.getAtivo() + " (" + t.getStatus() + ")");

            System.out.print("Digite o novo Tipo (COMPRA, VENDA, DEPOSITO, SAQUE) ou vazio para manter: ");
            String tipo = scanner.nextLine().trim().toUpperCase();
            if (!tipo.isEmpty()) {
                if (!tipo.equals("COMPRA") && !tipo.equals("VENDA") && !tipo.equals("DEPOSITO") && !tipo.equals("SAQUE")) {
                    System.out.println("Erro: Tipo inválido."); return;
                }
                t.setTipo(tipo);
            }

            System.out.print("Digite o novo Ativo (vazio para manter): ");
            String ativo = scanner.nextLine().trim();
            if (!ativo.isEmpty()) {
                if (ativo.length() > 10) { System.out.println("Erro: Ativo muito longo."); return; }
                t.setAtivo(ativo);
            }

            System.out.print("Digite a nova Quantidade (vazio para manter): ");
            String qtdStr = scanner.nextLine().trim();
            if (!qtdStr.isEmpty()) {
                double quantidade = Double.parseDouble(qtdStr);
                if(quantidade < 0) { System.out.println("Erro: Quantidade inválida."); return; }
                t.setQuantidade(quantidade);
            }

            System.out.print("Digite o novo Preço (vazio para manter): ");
            String precoStr = scanner.nextLine().trim();
            if (!precoStr.isEmpty()) {
                double preco = Double.parseDouble(precoStr);
                if(preco < 0) { System.out.println("Erro: Preço inválido."); return; }
                t.setPreco(preco);
            }

            System.out.print("Digite o novo Status (PENDENTE, CONFIRMADA, CANCELADA, FALHA) ou vazio para manter: ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (!status.isEmpty()) {
                if (!status.equals("PENDENTE") && !status.equals("CONFIRMADA") && !status.equals("CANCELADA") && !status.equals("FALHA")) {
                    System.out.println("Erro: Status inválido."); return;
                }
                t.setStatus(status);
            }

            dao.atualizar(t);
            System.out.println("Transação alterada com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao alterar transação: " + e.getMessage());
        }
    }

    private void excluir(Scanner scanner, TransacaoDAO dao) {
        System.out.print("Digite o ID da Transação que deseja excluir: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            dao.remover(id);
            System.out.println("Transação excluída com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao excluir transação: " + e.getMessage());
        }
    }

    private void exibirTodos(TransacaoDAO dao) {
        try {
            List<Transacao> lista = dao.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("Nenhuma transação cadastrada.");
            } else {
                System.out.println("\nLista de Transações:");
                for (Transacao t : lista) {
                    System.out.println(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar transações: " + e.getMessage());
        }
    }

    private void exibirPorId(Scanner scanner, TransacaoDAO dao) {
        System.out.print("Digite o ID da Transação: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Transacao t = dao.buscarPorId(id);
            System.out.println(t);
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar transação: " + e.getMessage());
        }
    }
}
