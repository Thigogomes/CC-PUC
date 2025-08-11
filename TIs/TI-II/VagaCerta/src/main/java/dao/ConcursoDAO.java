package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Classes.Concurso;

public class ConcursoDAO extends DAO {
    public ConcursoDAO() {
        super();
        conectar();
    }

    public boolean insert(Concurso concurso) {
        boolean status = false;
        String sql =
            "INSERT INTO concurso (" +
            "id_concurso, nome, escolaridade, localizacao, categoria, banca, descricao, " +
            "orgao, cargo, \"materiaisDeEstudo\", horario, status, data_inscricao, data_termino" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1,  concurso.getID());
            st.setString(2,  concurso.getNome());
            st.setString(3,  concurso.getEscolaridade());
            st.setString(4,  concurso.getLocalizacao());
            st.setString(5,  concurso.getCategoria());
            st.setString(6,  concurso.getBanca());
            st.setString(7,  concurso.getDescricao());
            st.setString(8,  concurso.getOrgao());
            st.setString(9,  concurso.getCargo());
            st.setString(10, concurso.getMateriaisDeEstudo());
            st.setString(11, concurso.getHorario());
            st.setString(12, concurso.getStatus());
            st.setDate(13, new java.sql.Date(concurso.getInicioIncricoes().getTime()));
            st.setDate(14, new java.sql.Date(concurso.getTerminoIncricoes().getTime()));
    
            st.executeUpdate();
            status = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean update(Concurso concurso) {
        String sql = 
            "UPDATE concurso SET " +
            "nome = ?, " +
            "escolaridade = ?, " +
            "localizacao = ?, " +
            "categoria = ?, " +
            "banca = ?, " +
            "descricao = ?, " +
            "orgao = ?, " +
            "cargo = ?, " +
            "\"materiaisDeEstudo\" = ?, " +
            "horario = ?, " +
            "status = ?, " +
            "data_inscricao = ?, " +
            "data_termino = ? " +
            "WHERE id_concurso = ?";
    
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, concurso.getNome());
            ps.setString(2, concurso.getEscolaridade());
            ps.setString(3, concurso.getLocalizacao());
            ps.setString(4, concurso.getCategoria());
            ps.setString(5, concurso.getBanca());
            ps.setString(6, concurso.getDescricao());
            ps.setString(7, concurso.getOrgao());
            ps.setString(8, concurso.getCargo());
            ps.setString(9, concurso.getMateriaisDeEstudo());
            ps.setString(10, concurso.getHorario());
            ps.setString(11, concurso.getStatus());
            ps.setDate(12, new java.sql.Date(concurso.getInicioIncricoes().getTime()));
            ps.setDate(13, new java.sql.Date(concurso.getTerminoIncricoes().getTime()));
            ps.setInt(14, concurso.getID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Método que estava faltando
    public Concurso getConcurso(int id) {
        Concurso concurso = null;
        try {
            String sql = "SELECT * FROM concurso WHERE id_concurso = ?";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                concurso = new Concurso();
                concurso.setID(rs.getInt("id_concurso"));
                concurso.setNome(rs.getString("nome"));
                concurso.setEscolaridade(rs.getString("escolaridade"));
                concurso.setLocalizacao(rs.getString("localizacao"));
                concurso.setCategoria(rs.getString("categoria"));
                concurso.setBanca(rs.getString("banca"));
                concurso.setDescricao(rs.getString("descricao"));
                concurso.setOrgao(rs.getString("orgao"));
                concurso.setCargo(rs.getString("cargo"));
                concurso.setMateriaisDeEstudo(rs.getString("materiaisDeEstudo"));
                concurso.setHorario(rs.getString("horario"));
                concurso.setStatus(rs.getString("status"));
                concurso.setInicioIncricoes(rs.getDate("data_inscricao"));
                concurso.setTerminoIncricoes(rs.getDate("data_termino"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("Erro ao buscar concurso por ID: " + e.getMessage());
        }

        return concurso;
    }

    public List<Concurso> getAllConcursos() {
        List<Concurso> concursos = new ArrayList<>();
        String sql = "SELECT * FROM concurso";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Concurso concurso = new Concurso(
                    rs.getInt("id_concurso"),
                    rs.getString("nome"),
                    rs.getString("escolaridade"),
                    rs.getString("localizacao"),
                    rs.getString("categoria"),
                    rs.getString("banca"),
                    rs.getString("descricao"),
                    rs.getString("orgao"),
                    rs.getString("cargo"),
                    rs.getString("materiaisDeEstudo"),
                    rs.getString("horario"),
                    rs.getString("status"),
                    rs.getDate("data_inscricao"),
                    rs.getDate("data_termino")
                );
                concursos.add(concurso);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return concursos;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM concurso WHERE id_concurso = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int geradorDeId() {
        List<Concurso> concursos = getAllConcursos();
        int maxId = 0;
        if (concursos != null) {
            for (Concurso c : concursos) {
                if (c.getID() > maxId) {
                    maxId = c.getID();
                }
            }
        }
        return maxId + 1;
    }
    
    /**
     * Lista concursos filtrando por:
     *  - nome (prefixo case‐insensitive)
     *  - data de inscrição (YYYY‑MM‑DD)
     *  - escolaridade (exato)
     *  - localizacao (prefixo case‐insensitive)
     *
     * Se qualquer parâmetro vier null ou vazio, ignora aquele filtro.
     */
    public List<Concurso> listar(String nome, String dataInscricao, String escolaridade, String localizacao) {
        List<Concurso> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM concurso WHERE 1=1");

        if (nome != null && !nome.trim().isEmpty()) {
            sql.append(" AND lower(nome) LIKE ?");
        }
        if (dataInscricao != null && !dataInscricao.trim().isEmpty()) {
            sql.append(" AND data_inscricao = ?");
        }
        if (escolaridade != null && !escolaridade.trim().isEmpty()) {
            sql.append(" AND escolaridade = ?");
        }
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            sql.append(" AND lower(localizacao) LIKE ?");
        }

        try (PreparedStatement ps = conexao.prepareStatement(sql.toString())) {
            int idx = 1;
            if (nome != null && !nome.trim().isEmpty()) {
                ps.setString(idx++, nome.trim().toLowerCase() + "%");
            }
            if (dataInscricao != null && !dataInscricao.trim().isEmpty()) {
                ps.setDate(idx++, java.sql.Date.valueOf(dataInscricao));
            }
            if (escolaridade != null && !escolaridade.trim().isEmpty()) {
                ps.setString(idx++, escolaridade);
            }
            if (localizacao != null && !localizacao.trim().isEmpty()) {
                ps.setString(idx++, localizacao.trim().toLowerCase() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Concurso(
                        rs.getInt("id_concurso"),
                        rs.getString("nome"),
                        rs.getString("escolaridade"),
                        rs.getString("localizacao"),
                        rs.getString("categoria"),
                        rs.getString("banca"),
                        rs.getString("descricao"),
                        rs.getString("orgao"),
                        rs.getString("cargo"),
                        rs.getString("materiaisDeEstudo"),
                        rs.getString("horario"),
                        rs.getString("status"),
                        rs.getDate("data_inscricao"),
                        rs.getDate("data_termino")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
