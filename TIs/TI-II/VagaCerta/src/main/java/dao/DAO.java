package dao;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DAO {
    protected static Connection conexao;

	public DAO() { }
    // Lista de tabelas que o sistema precisa ter
    private static final List<String> REQUIRED_TABLES = Arrays.asList(
        "edital", "concurso", "materia", "livro", "cronograma",
        "usuario", "vincular", "inscrever", "ler",
        "simular", "questao", "alternativa"
    );

    public boolean conectar() {
        String driverName  = "org.postgresql.Driver";
        String serverName  = "vaga-certa.postgres.database.azure.com";
        String database    = "concurso_vagacerta";
        int    porta       = 5432;
        String username    = "adm_vagacerta";
        String password    = "pucminas25@";
        

        // URL base apontando para o postgres "mãe"
        String urlBase = String.format(
            "jdbc:postgresql://%s:%d/postgres?sslmode=require",
            serverName, porta
        );

        try {
            Class.forName(driverName);

            // 1) conecta ao postgres padrão e verifica existência do DB
            try (Connection connInit = DriverManager.getConnection(urlBase, username, password);
                 PreparedStatement ps = connInit.prepareStatement(
                     "SELECT 1 FROM pg_database WHERE datname = ?"
                 )
            ) {
                ps.setString(1, database);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.err.println("Banco '" + database + "' NÃO existe.");
                        return false;
                    }
                }
            }

            // 2) reconecta agora ao seu database alvo
            String urlDb = String.format(
                "jdbc:postgresql://%s:%d/%s?sslmode=require",
                serverName, porta, database
            );
            conexao = DriverManager.getConnection(urlDb, username, password);

            // 3) checa existência das tabelas obrigatórias
            String checkTableSql =
                "SELECT 1 " +
                "FROM information_schema.tables " +
                "WHERE table_schema = 'public' AND table_name = ?";

            try (PreparedStatement ps = conexao.prepareStatement(checkTableSql)) {
                for (String table : REQUIRED_TABLES) {
                    ps.setString(1, table);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.err.println("Tabela obrigatória ausente: " + table);
                            conexao.close();
                            return false;
                        }
                    }
                }
            }

            System.out.println("Conexão estabelecida e todas as tabelas existem.");
            return true;

        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL não encontrado: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("Erro de SQL: " + e.getMessage());
            return false;
        }
    }

    public boolean close() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
            return false;
        }
    }
}
