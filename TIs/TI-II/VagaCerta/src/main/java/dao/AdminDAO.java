package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Classes.Admin;

public class AdminDAO extends DAO {

    /** Insere um novo Admin e retorna o ID gerado no objeto. */
    public boolean inserirAdmin(Admin admin) {
        String sql = "INSERT INTO public.admin (\"user\", password, nome) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, admin.getUser());
            ps.setString(2, admin.getPassword());
            ps.setString(3, admin.getNome());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        admin.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir Admin: " + e.getMessage(), e);
        }
        return false;
    }

    /** Atualiza um Admin existente (identificado por id). */
    public boolean atualizarAdmin(Admin admin) {
        String sql = "UPDATE public.admin SET \"user\" = ?, password = ?, nome = ? WHERE id = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, admin.getUser());
            ps.setString(2, admin.getPassword());
            ps.setString(3, admin.getNome());
            ps.setInt(4, admin.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Admin: " + e.getMessage(), e);
        }
    }

    /** Remove o Admin de ID informado. */
    public boolean deleteAdmin(int id) {
        String sql = "DELETE FROM public.admin WHERE id = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar Admin: " + e.getMessage(), e);
        }
    }

    /** Busca um Admin pelo seu ID. */
    public Admin getAdminById(int id) {
        String sql = "SELECT id, \"user\", password, nome FROM public.admin WHERE id = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                        rs.getInt("id"),
                        rs.getString("user"),
                        rs.getString("password"),
                        rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Admin por ID: " + e.getMessage(), e);
        }
        return null;
    }

    /** Lista todos os Admins. */
    public List<Admin> listAll() {
        String sql = "SELECT id, \"user\", password, nome FROM public.admin ORDER BY id";
        List<Admin> admins = new ArrayList<>();
        try (Statement st = conexao.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                admins.add(new Admin(
                    rs.getInt("id"),
                    rs.getString("user"),
                    rs.getString("password"),
                    rs.getString("nome")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Admins: " + e.getMessage(), e);
        }
        return admins;
    }
    
    /** Busca um Admin pelo user (login). */
    public Admin getByUser(String user) {
        String sql = "SELECT id, \"user\", password, nome FROM public.admin WHERE \"user\" = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                        rs.getInt("id"),
                        rs.getString("user"),
                        rs.getString("password"),
                        rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
