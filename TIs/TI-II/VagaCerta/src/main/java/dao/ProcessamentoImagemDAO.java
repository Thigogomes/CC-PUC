package dao;

import Classes.ProcessamentoImagem;

import java.sql.*;

public class ProcessamentoImagemDAO extends DAO {

    public ProcessamentoImagemDAO() {
        super(); // herda a conexão configurada em DAO.java
        // opcional: criar tabela se não existir
        String ddl = "CREATE TABLE IF NOT EXISTS processamentos_imagem (" +
                "id BIGSERIAL PRIMARY KEY," +
                "file_name VARCHAR(255) NOT NULL," +
                "titulo VARCHAR(1024)," +
                "processed_at TIMESTAMP NOT NULL" +
                ")";
        try (Statement st = conexao.createStatement()) {
        	st.executeUpdate(ddl);
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }

    public void save(ProcessamentoImagem p) throws SQLException {
        String sql = "INSERT INTO processamentos_imagem (file_name, titulo, processed_at) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getFileName());
            ps.setString(2, p.getTitulo());
            ps.setTimestamp(3, Timestamp.valueOf(p.getProcessedAt()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getLong(1));
                }
            }
        }
    }
}
