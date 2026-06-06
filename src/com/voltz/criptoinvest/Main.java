package com.voltz.criptoinvest;

import com.voltz.criptoinvest.view.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        EmpresaConsoleView empresaView = new EmpresaConsoleView();
        UsuarioConsoleView usuarioView = new UsuarioConsoleView();
        CarteiraConsoleView carteiraView = new CarteiraConsoleView();
        InvestimentoConsoleView investimentoView = new InvestimentoConsoleView();
        TransacaoConsoleView transacaoView = new TransacaoConsoleView();
        RelatorioConsoleView relatorioView = new RelatorioConsoleView();

        while (opcao != 0) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║   SISTEMA DE GESTÃO DE CRIPTOINVESTIMENTOS       ║");
            System.out.println("║   Menu Principal (Fase 7)                        ║");
            System.out.println("╚══════════════════════════════════════════════════╝\n");
            
            System.out.println("1. Gerenciar Empresas");
            System.out.println("2. Gerenciar Usuários");
            System.out.println("3. Gerenciar Carteiras");
            System.out.println("4. Gerenciar Investimentos");
            System.out.println("5. Gerenciar Transações");
            System.out.println("6. Gerenciar Relatórios");
            System.out.println("0. Sair");
            System.out.print("Escolha um módulo: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1:
                    empresaView.exibirMenu(scanner);
                    break;
                case 2:
                    usuarioView.exibirMenu(scanner);
                    break;
                case 3:
                    carteiraView.exibirMenu(scanner);
                    break;
                case 4:
                    investimentoView.exibirMenu(scanner);
                    break;
                case 5:
                    transacaoView.exibirMenu(scanner);
                    break;
                case 6:
                    relatorioView.exibirMenu(scanner);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        
        scanner.close();
    }
}