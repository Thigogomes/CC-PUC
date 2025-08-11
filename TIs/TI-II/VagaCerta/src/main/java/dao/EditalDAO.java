package dao;

import java.sql.*;
import java.util.*;

import Classes.Edital;

public class EditalDAO extends DAO {
    public EditalDAO(){
        super();
        conectar();
    }

    public boolean insert(Edital edital) {
        boolean status = false;
        try {
            String sql = "INSERT INTO edital(id_edital, arquivo, data_publi) VALUES (?, ?, ?);";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, edital.getId());
            st.setString(2, edital.getArquivo());
            st.setDate(3, edital.getData_publicacao());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    public Edital getEdital(int id) {
        Edital edital = null;
        String sql = "SELECT * FROM edital WHERE id_edital = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    edital = new Edital(
                        rs.getInt("id_edital"),
                        rs.getString("arquivo"),
                        rs.getDate("data_publi")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return edital;
    }

    public List<Edital> getAllEditais() {
        List<Edital> editais = new ArrayList<>();
        String sql = "SELECT * FROM edital";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Edital edital = new Edital(
                    rs.getInt("id_edital"),
                    rs.getString("arquivo"),
                    rs.getDate("data_publi")
                );
                editais.add(edital);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return editais;
    }

    public boolean update(Edital edital) {
        boolean status = false;
        try {
            String sql = "UPDATE edital SET arquivo = ?, data_publi = ? WHERE id_edital = ?;";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, edital.getArquivo());
            st.setDate(2, edital.getData_publicacao());
            st.setInt(3, edital.getId());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try {
            String sql = "DELETE FROM edital WHERE id_edital = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return status;
    }
}