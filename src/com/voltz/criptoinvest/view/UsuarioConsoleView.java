package com.voltz.criptoinvest.view;

import com.voltz.criptoinvest.dao.UsuarioDAO;
import com.voltz.criptoinvest.model.Usuario;
import com.voltz.criptoinvest.exception.EntidadeNaoEncontradaException;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UsuarioConsoleView {

    public void exibirMenu(Scanner scanner) {
        int opcao = -1;
        UsuarioDAO dao = new UsuarioDAO();

        while (opcao != 0) {
            System.out.println("\n--- Módulo de Usuário ---");
            System.out.println("1. Incluir Usuário");
            System.out.println("2. Alterar Usuário");
            System.out.println("3. Excluir Usuário");
            System.out.println("4. Exibir todos os Usuários");
            System.out.println("5. Exibir Usuário por ID");
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

    private void incluir(Scanner scanner, UsuarioDAO dao) {
        System.out.print("Digite o Nome do Usuário: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty() || nome.length() > 100) {
            System.out.println("Erro: Nome não pode ser vazio e deve ter no máximo 100 caracteres.");
            return;
        }

        System.out.print("Digite o Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty() || email.length() > 100) {
            System.out.println("Erro: Email inválido.");
            return;
        }

        System.out.print("Digite a Senha: ");
        String senha = scanner.nextLine().trim();
        if (senha.isEmpty() || senha.length() > 255) {
            System.out.println("Erro: Senha inválida.");
            return;
        }

        System.out.print("Digite o Papel (ex: admin, gestor, operador): ");
        String papel = scanner.nextLine().trim();
        if (papel.isEmpty() || papel.length() > 30) {
            System.out.println("Erro: Papel inválido.");
            return;
        }

        System.out.print("Digite o ID da Empresa vinculada (ou enter para deixar vazio): ");
        String idEmpresaStr = scanner.nextLine().trim();
        Long idEmpresa = null;
        if (!idEmpresaStr.isEmpty()) {
            try {
                idEmpresa = Long.parseLong(idEmpresaStr);
            } catch (NumberFormatException e) {
                System.out.println("Erro: ID de Empresa inválido.");
                return;
            }
        }

        Usuario usuario = new Usuario(null, nome, email, senha, papel, idEmpresa);
        try {
            dao.cadastrar(usuario);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    private void alterar(Scanner scanner, UsuarioDAO dao) {
        System.out.print("Digite o ID do Usuário que deseja alterar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Usuario usuario = dao.buscarPorId(id);
            System.out.println("Usuário encontrado: " + usuario.getNome() + " - " + usuario.getEmail());

            System.out.print("Digite o novo Nome (deixe vazio para manter): ");
            String nome = scanner.nextLine().trim();
            if (!nome.isEmpty()) {
                if(nome.length() > 100) { System.out.println("Erro: Nome longo demais."); return; }
                usuario.setNome(nome);
            }

            System.out.print("Digite o novo Email (deixe vazio para manter): ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                if(email.length() > 100) { System.out.println("Erro: Email longo demais."); return; }
                usuario.setEmail(email);
            }

            System.out.print("Digite a nova Senha (deixe vazio para manter): ");
            String senha = scanner.nextLine().trim();
            if (!senha.isEmpty()) {
                if(senha.length() > 255) { System.out.println("Erro: Senha longa demais."); return; }
                usuario.setSenhaHash(senha);
            }

            System.out.print("Digite o novo Papel (deixe vazio para manter): ");
            String papel = scanner.nextLine().trim();
            if (!papel.isEmpty()) {
                if(papel.length() > 30) { System.out.println("Erro: Papel longo demais."); return; }
                usuario.setPapel(papel);
            }

            System.out.print("Digite o novo ID da Empresa vinculada (deixe vazio para manter, ou '0' para remover): ");
            String idEmpresaStr = scanner.nextLine().trim();
            if (!idEmpresaStr.isEmpty()) {
                if (idEmpresaStr.equals("0")) {
                    usuario.setEmpresaId(null);
                } else {
                    Long idEmpresa = Long.parseLong(idEmpresaStr);
                    usuario.setEmpresaId(idEmpresa);
                }
            }

            dao.atualizar(usuario);
            System.out.println("Usuário alterado com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao alterar usuário: " + e.getMessage());
        }
    }

    private void excluir(Scanner scanner, UsuarioDAO dao) {
        System.out.print("Digite o ID do Usuário que deseja excluir: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            dao.remover(id);
            System.out.println("Usuário excluído com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
        }
    }

    private void exibirTodos(UsuarioDAO dao) {
        try {
            List<Usuario> lista = dao.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado.");
            } else {
                System.out.println("\nLista de Usuários:");
                for (Usuario u : lista) {
                    System.out.println(u);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }
    }

    private void exibirPorId(Scanner scanner, UsuarioDAO dao) {
        System.out.print("Digite o ID do Usuário: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            Usuario u = dao.buscarPorId(id);
            System.out.println(u);
        } catch (NumberFormatException e) {
            System.out.println("Erro: ID inválido.");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }
    }
}
