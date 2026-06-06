package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.CarteiraDAO;
import com.voltz.criptoinvest.model.Carteira;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CarteiraConsoleView {

    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        CarteiraDAO dao = new CarteiraDAO();

        while (opcao != 0) {
            System.out.println("\n--- Módulo de Carteira ---");
            System.out.println("1. Incluir Carteira");
            System.out.println("2. Alterar Carteira");
            System.out.println("3. Excluir Carteira");
            System.out.println("4. Exibir todas as Carteiras");
            System.out.println("5. Exibir Carteira por ID");
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

    private void incluir(Scanner scanner, CarteiraDAO dao) {
        System.out.print("Digite o Endereço da Carteira: ");
        String endereco = scanner.nextLine().trim();
        if (endereco.isEmpty() || endereco.length() > 100) {
            System.out.println("Erro: Endereço inválido.");
            return;
        }

        System.out.print("Digite o Custodiante: ");
        String custodiante = scanner.nextLine().trim();
        if (custodiante.isEmpty() || custodiante.length() > 100) {
            System.out.println("Erro: Custodiante inválido.");
            return;
        }

        System.out.print("Digite o Saldo Inicial (ex: 1000.50): ");
        double saldo = 0.0;
        try {
            saldo = Double.parseDouble(scanner.nextLine());
            if (saldo < 0) {
                System.out.println("Erro: Saldo não pode ser negativo (regra de negócio).");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
            return;
        }

        System.out.print("Digite o ID da Empresa dona da carteira: ");
        Long idEmpresa = null;
        try {
            idEmpresa = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID de Empresa inválido.");
            return;
        }

        Carteira carteira = new Carteira(null, endereco, custodiante, saldo, idEmpresa);
        try {
            dao.cadastrar(carteira);
            System.out.println("Carteira cadastrada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar carteira: " + e.getMessage());
        }
    }

    private void alterar(Scanner scanner, CarteiraDAO dao) {
        System.out.print("Digite o ID da Carteira que deseja alterar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Carteira carteira = dao.buscarPorId(id);
            System.out.println("Carteira encontrada: " + carteira.getEndereco() + " - " + carteira.getCustodiante());

            System.out.print("Digite o novo Endereço (deixe vazio para manter): ");
            String endereco = scanner.nextLine().trim();
            if (!endereco.isEmpty()) {
                if(endereco.length() > 100) { System.out.println("Erro: Endereço longo demais."); return; }
                carteira.setEndereco(endereco);
            }

            System.out.print("Digite o novo Custodiante (deixe vazio para manter): ");
            String custodiante = scanner.nextLine().trim();
            if (!custodiante.isEmpty()) {
                if(custodiante.length() > 100) { System.out.println("Erro: Custodiante longo demais."); return; }
                carteira.setCustodiante(custodiante);
            }

            System.out.print("Digite o novo Saldo (deixe vazio para manter): ");
            String saldoStr = scanner.nextLine().trim();
            if (!saldoStr.isEmpty()) {
                double saldo = Double.parseDouble(saldoStr);
                if (saldo < 0) {
                    System.out.println("Erro: Saldo não pode ser negativo.");
                    return;
                }
                carteira.setSaldo(saldo);
            }

            System.out.print("Digite o novo ID da Empresa (deixe vazio para manter): ");
            String idEmpresaStr = scanner.nextLine().trim();
            if (!idEmpresaStr.isEmpty()) {
                Long idEmpresa = Long.parseLong(idEmpresaStr);
                carteira.setEmpresaId(idEmpresa);
            }

            dao.atualizar(carteira);
            System.out.println("Carteira alterada com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao alterar carteira: " + e.getMessage());
        }
    }

    private void excluir(Scanner scanner, CarteiraDAO dao) {
        System.out.print("Digite o ID da Carteira que deseja excluir: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            dao.remover(id);
            System.out.println("Carteira excluída com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao excluir carteira: " + e.getMessage());
        }
    }

    private void exibirTodos(CarteiraDAO dao) {
        try {
            List<Carteira> lista = dao.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("Nenhuma carteira cadastrada.");
            } else {
                System.out.println("\nLista de Carteiras:");
                for (Carteira c : lista) {
                    System.out.println(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar carteiras: " + e.getMessage());
        }
    }

    private void exibirPorId(Scanner scanner, CarteiraDAO dao) {
        System.out.print("Digite o ID da Carteira: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Carteira c = dao.buscarPorId(id);
            System.out.println(c);
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar carteira: " + e.getMessage());
        }
    }
}
