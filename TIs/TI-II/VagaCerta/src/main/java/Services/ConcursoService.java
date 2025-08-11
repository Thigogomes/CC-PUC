package Services;

import static spark.Spark.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;


import dao.ConcursoDAO;
import Classes.Concurso;
import spark.Request;
import spark.Response;

public class ConcursoService {
    private ConcursoDAO concursoDAO = new ConcursoDAO();
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public ConcursoService() {
        path("/", () -> {
            post("concursos", this::createConcurso);
            get("concursos", this::getAllConcursos);
            get("concursos/:id", this::getConcursoById);
            get("concursos/exibir/:id", this::exibir);
            put("concursos/:id", this::updateConcurso);
        });
    }

    public Object createConcurso(Request req, Response res) {
        Concurso concurso = new Concurso();
        concurso.setID(concursoDAO.geradorDeId());
        concurso.setNome(req.queryParams("nome"));
        concurso.setEscolaridade(req.queryParams("escolaridade"));
        concurso.setLocalizacao(req.queryParams("localizacao"));
        concurso.setCategoria(req.queryParams("categoria"));
        concurso.setBanca(req.queryParams("banca"));
        concurso.setDescricao(req.queryParams("descricao"));
        concurso.setOrgao(req.queryParams("orgao"));
        concurso.setCargo(req.queryParams("cargo"));
        concurso.setMateriaisDeEstudo(req.queryParams("materiaisDeEstudo"));
        concurso.setHorario(req.queryParams("horario"));
        concurso.setStatus(req.queryParams("status"));

        try {
            Date inicio = formato.parse(req.queryParams("inicioIncricoes"));
            Date termino = formato.parse(req.queryParams("terminoIncricoes"));
            concurso.setInicioIncricoes(inicio);
            concurso.setTerminoIncricoes(termino);
        } catch (ParseException e) {
            e.printStackTrace();
            res.status(400);
            return "Erro ao converter datas.";
        }

        boolean sucesso = concursoDAO.insert(concurso);
        res.status(sucesso ? 201 : 500);
        return sucesso ? "Concurso criado com sucesso!" : "Erro ao criar concurso.";
    }

    public Object getAllConcursos(Request req, Response res) {
        // lê possíveis query params
        String nome        = req.queryParams("nome");
        String data        = req.queryParams("data");
        String escolaridade= req.queryParams("nivelEnsino");
        String localizacao = req.queryParams("localizacao");

        List<Concurso> lista = concursoDAO.listar(nome, data, escolaridade, localizacao);
        res.type("application/json");
        return new Gson().toJson(lista);
    }


    public Object getConcursoById(Request req, Response res) {
        int id = Integer.parseInt(req.params("id"));
        Concurso concurso = concursoDAO.getConcurso(id);

        if (concurso != null) {
            res.type("application/json");
            return new Gson().toJson(concurso);
        } else {
            res.status(404);
            return "Concurso não encontrado.";
        }
    }


    public Object updateConcurso(Request req, Response res) {
        int id = Integer.parseInt(req.params("id"));
        Concurso concurso = new Concurso();
        concurso.setID(id);
        concurso.setNome(req.queryParams("nome"));
        concurso.setEscolaridade(req.queryParams("escolaridade"));
        concurso.setLocalizacao(req.queryParams("localizacao"));
        concurso.setCategoria(req.queryParams("categoria"));
        concurso.setBanca(req.queryParams("banca"));
        concurso.setDescricao(req.queryParams("descricao"));
        concurso.setOrgao(req.queryParams("orgao"));
        concurso.setCargo(req.queryParams("cargo"));
        concurso.setMateriaisDeEstudo(req.queryParams("materiaisDeEstudo"));
        concurso.setHorario(req.queryParams("horario"));
        concurso.setStatus(req.queryParams("status"));

        try {
            Date inicio = formato.parse(req.queryParams("inicioIncricoes"));
            Date termino = formato.parse(req.queryParams("terminoIncricoes"));
            concurso.setInicioIncricoes(inicio);
            concurso.setTerminoIncricoes(termino);
        } catch (ParseException e) {
            e.printStackTrace();
            res.status(400);
            return "Erro ao converter datas.";
        }

        boolean sucesso = concursoDAO.update(concurso);
        res.status(sucesso ? 200 : 500);
        return sucesso ? "Concurso atualizado com sucesso!" : "Erro ao atualizar concurso.";
    }

    public Object delete(Request req, Response res) {
        int id = Integer.parseInt(req.params("id"));
        boolean sucesso = concursoDAO.delete(id);
        res.status(sucesso ? 200 : 404);
        return sucesso ? "Concurso excluído com sucesso!" : "Concurso não encontrado.";
    }

    // ✅ Novo método: redireciona para a página detalhamento com os dados como parâmetros
    public Object exibir(Request req, Response res) {
        int id = Integer.parseInt(req.params("id"));
        Concurso concurso = concursoDAO.getConcurso(id);

        if (concurso != null) {
            String url = String.format("/detalhes.html?" +
                    "id=%d&nome=%s&escolaridade=%s&localizacao=%s&categoria=%s&banca=%s&descricao=%s&orgao=%s" +
                    "&cargo=%s&materiaisDeEstudo=%s&horario=%s&status=%s&inicioIncricoes=%s&terminoIncricoes=%s",
                concurso.getID(),
                encode(concurso.getNome()),
                encode(concurso.getEscolaridade()),
                encode(concurso.getLocalizacao()),
                encode(concurso.getCategoria()),
                encode(concurso.getBanca()),
                encode(concurso.getDescricao()),
                encode(concurso.getOrgao()),
                encode(concurso.getCargo()),
                encode(concurso.getMateriaisDeEstudo()),
                encode(concurso.getHorario()),
                encode(concurso.getStatus()),
                concurso.getInicioIncricoes().toString(),
                concurso.getTerminoIncricoes().toString()
            );
            res.redirect(url);
        } else {
            res.status(404);
            return "Concurso não encontrado.";
        }
        return null;
    }

    private String encode(String valor) {
        try {
            return java.net.URLEncoder.encode(valor, "UTF-8");
        } catch (Exception e) {
            return valor;
        }
    }
}
