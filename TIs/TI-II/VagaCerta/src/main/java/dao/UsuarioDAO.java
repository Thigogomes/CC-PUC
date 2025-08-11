package dao;

import java.sql.*;
import java.util.*;

import Classes.Usuario;

public class UsuarioDAO extends DAO {
	    
    public boolean inserirUsuario(Usuario usuario) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate(
                "INSERT INTO usuario (cpf, nome, escolaridade, senha, email) VALUES ('" +
                usuario.getCpf() + "', '" +
                usuario.getNome() + "', '" +
                usuario.getEscolaridade() + "', '" +
                usuario.getSenha() + "', '" +
                usuario.getEmail() + "');"
            );
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public boolean atualizarUsuario(Usuario usuario) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql =
                "UPDATE usuario SET " +
                "nome = '" + usuario.getNome() + "', " +
                "escolaridade = '" + usuario.getEscolaridade() + "', " +
                "senha = '" + usuario.getSenha() + "', " +
                "email = '" + usuario.getEmail() + "'" +
                " WHERE cpf = '" + usuario.getCpf() + "'";
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public boolean delete(String cpf) {
    	String sql = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1,cpf);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Usuario getUsuarioByCpf(String cpf) {
        Usuario usuario = null;
        try {
            String sql = "SELECT * FROM usuario WHERE cpf = ?";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuario(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("escolaridade"),
                    rs.getString("senha"),
                    rs.getString("email")
                );
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário por CPF: " + e.getMessage());
        }
        return usuario;
    }

    public Usuario[] getUsuarios() {
        Usuario[] usuarios = null;
        try {
            Statement st = conexao.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            );
            ResultSet rs = st.executeQuery("SELECT * FROM usuario");
            if (rs.next()) {
                rs.last();
                usuarios = new Usuario[rs.getRow()];
                rs.beforeFirst();
                for (int i = 0; rs.next(); i++) {
                    usuarios[i] = new Usuario(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("escolaridade"),
                        rs.getString("senha"),
                        rs.getString("email")
                    );
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return usuarios;
    }
}
