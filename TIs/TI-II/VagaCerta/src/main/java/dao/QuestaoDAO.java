/*package dao;

import java.sql.*;
import java.util.*;
import Classes.Questao;

public class QuestaoDAO {
	public QuestaoDAO(){
		super();
	    conectar();
	}

    public static boolean cadastrar(String enunciado, String a, String b, String c, String d, String resposta, int concursoId) {
        String alternativas = "A) " + a + " B) " + b + " C) " + c + " D) " + d;
        try (Connection conn = Conexao.getConnection()) {
            String sql = "INSERT INTO questao (concurso_id, enunciado, alternativas, resposta_certa) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, concursoId);
            stmt.setString(2, enunciado);
            stmt.setString(3, alternativas);
            stmt.setString(4, resposta);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> listar() {
        List<String> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM questao")) {
            while (rs.next()) {
                lista.add("ID: " + rs.getInt("id") + " | " + rs.getString("enunciado") + " | Resposta: " + rs.getString("resposta_certa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static Map<String, String> buscarPorId(int id) {
        Map<String, String> dados = new HashMap<>();
        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT * FROM questao WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dados.put("id", String.valueOf(rs.getInt("id")));
                dados.put("concurso_id", String.valueOf(rs.getInt("concurso_id")));
                dados.put("enunciado", rs.getString("enunciado"));
                dados.put("alternativas", rs.getString("alternativas"));
                dados.put("resposta", rs.getString("resposta_certa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dados;
    }

    public static boolean atualizar(int id, String enunciado, String alternativas, String resposta, int concursoId) {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "UPDATE questao SET concurso_id=?, enunciado=?, alternativas=?, resposta_certa=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, concursoId);
            stmt.setString(2, enunciado);
            stmt.setString(3, alternativas);
            stmt.setString(4, resposta);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean excluir(int id) {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "DELETE FROM questao WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
*/