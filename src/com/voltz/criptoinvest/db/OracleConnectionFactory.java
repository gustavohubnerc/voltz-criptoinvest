package com.voltz.criptoinvest.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por centralizar a criação de conexões com o banco Oracle da FIAP*
 * Esta classe será utilizada pelas demais camadas (DAO / Repositórios) para obter
 * uma conexão já configurada com o banco de dados.
 */
public class OracleConnectionFactory {

    // Dados de conexão fornecidos pela FIAP
    private static final String USER = "rm560403";
    private static final String PASSWORD = "060900";
    private static final String HOST = "ORACLE.FIAP.COM.BR";
    private static final String PORT = "1521";
    private static final String SID = "ORCL";

    // URL padrão de conexão Oracle (driver JDBC thin)
    // Formato: jdbc:oracle:thin:@host:porta:SID
    private static final String URL =
            "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SID;

    // Driver JDBC da Oracle (necessário estar no classpath do projeto)
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

    /**
     * Retorna uma nova conexão com o banco Oracle da FIAP.
     *
     * @return Connection já aberta
     * @throws SQLException caso ocorra algum problema na conexão
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Garante que o driver da Oracle seja carregado
            Class.forName(ORACLE_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC da Oracle não encontrado no classpath.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Metodo utilitário simples para testar a conexão com o banco.
     * Pode ser chamado a partir da classe Main.
     */
    public static void testarConexao() {
        System.out.println("Testando conexão com o banco Oracle...");
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Conexão estabelecida com sucesso!");
                System.out.println("   Auto-commit: " + conn.getAutoCommit());
            } else {
                System.out.println("✗ Não foi possível estabelecer a conexão.");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro ao conectar ao banco Oracle: " + e.getMessage());
        }
    }
}

