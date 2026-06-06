package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.EmpresaDAO;
import com.voltz.criptoinvest.model.Empresa;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EmpresaConsoleView {

    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        EmpresaDAO dao = null;
        try {
            dao = new EmpresaDAO();
            while (opcao != 0) {
                System.out.println("\n--- Módulo de Empresa ---");
                System.out.println("1. Incluir Empresa");
                System.out.println("2. Alterar Empresa");
                System.out.println("3. Excluir Empresa");
                System.out.println("4. Exibir todas as Empresas");
                System.out.println("5. Exibir Empresa por ID");
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
        } catch (SQLException e) {
            System.err.println("Erro de banco de dados no módulo Empresa: " + e.getMessage());
        } finally {
            if (dao != null) {
                try {
                    dao.fecharConexao();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    private void incluir(Scanner scanner, EmpresaDAO dao) {
        System.out.print("Digite o CNPJ da Empresa (máx 18 caracteres): ");
        String cnpj = scanner.nextLine().trim();
        if (cnpj.isEmpty() || cnpj.length() > 18) {
            System.out.println("Erro: CNPJ não pode ser vazio e deve ter no máximo 18 caracteres.");
            return;
        }

        System.out.print("Digite a Razão Social: ");
        String razaoSocial = scanner.nextLine().trim();
        if (razaoSocial.isEmpty() || razaoSocial.length() > 100) {
            System.out.println("Erro: Razão Social não pode ser vazia e deve ter no máximo 100 caracteres.");
            return;
        }

        Empresa empresa = new Empresa(cnpj, razaoSocial);
        try {
            dao.cadastrarEmpresa(empresa);
            System.out.println("Empresa cadastrada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar empresa: " + e.getMessage());
        }
    }

    private void alterar(Scanner scanner, EmpresaDAO dao) {
        System.out.print("Digite o ID da Empresa que deseja alterar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Empresa empresaExistente = dao.pesquisarEmpresa(id);
            System.out.println("Empresa encontrada: " + empresaExistente.getRazaoSocial() + " - CNPJ: " + empresaExistente.getCnpj());

            System.out.print("Digite o novo CNPJ (deixe vazio para manter): ");
            String cnpj = scanner.nextLine().trim();
            if (!cnpj.isEmpty()) {
                if(cnpj.length() > 18) {
                    System.out.println("Erro: CNPJ longo demais.");
                    return;
                }
                empresaExistente.setCnpj(cnpj);
            }

            System.out.print("Digite a nova Razão Social (deixe vazio para manter): ");
            String razaoSocial = scanner.nextLine().trim();
            if (!razaoSocial.isEmpty()) {
                if(razaoSocial.length() > 100) {
                    System.out.println("Erro: Razão Social longa demais.");
                    return;
                }
                empresaExistente.setRazaoSocial(razaoSocial);
            }

            dao.atualizarEmpresa(empresaExistente);
            System.out.println("Empresa alterada com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao alterar empresa: " + e.getMessage());
        }
    }

    private void excluir(Scanner scanner, EmpresaDAO dao) {
        System.out.print("Digite o ID da Empresa que deseja excluir: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            dao.removerEmpresa(id);
            System.out.println("Empresa excluída com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao excluir empresa: " + e.getMessage());
        }
    }

    private void exibirTodos(EmpresaDAO dao) {
        try {
            List<Empresa> lista = dao.listarEmpresas();
            if (lista.isEmpty()) {
                System.out.println("Nenhuma empresa cadastrada.");
            } else {
                System.out.println("\nLista de Empresas:");
                for (Empresa e : lista) {
                    System.out.println("ID: " + e.getId() + " | CNPJ: " + e.getCnpj() + " | Razão Social: " + e.getRazaoSocial());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar empresas: " + e.getMessage());
        }
    }

    private void exibirPorId(Scanner scanner, EmpresaDAO dao) {
        System.out.print("Digite o ID da Empresa: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Empresa e = dao.pesquisarEmpresa(id);
            System.out.println("ID: " + e.getId() + " | CNPJ: " + e.getCnpj() + " | Razão Social: " + e.getRazaoSocial());
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresa: " + e.getMessage());
        }
    }
}
