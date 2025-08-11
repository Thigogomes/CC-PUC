package Services;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import dao.EditalDAO;
import Classes.Edital;

public class EditalServices {
    private EditalDAO editalDAO = new EditalDAO();
    private SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    public static void registerRoutes() {
        EditalServices service = new EditalServices();
        post("/editais", service::insert);
        get("/editais", service::getAll);
        get("/editais/:id", service::getById);
        put("/editais/:id", service::update);
        Spark.delete("/editais/:id", service::delete);
    }

    public Object insert(Request req, Response res) {
        try {
            String arquivo = req.queryParams("arquivo");
            String dataPubliStr = req.queryParams("data_publicacao");

            Date utilDate = formato.parse(dataPubliStr);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Edital e = new Edital(0, arquivo, sqlDate);
            boolean ok = editalDAO.insert(e);
            if (ok) {
                res.status(201);
                return "Edital (" + arquivo + ") inserido!";
            } else {
                res.status(500);
                return "Erro ao inserir edital.";
            }
        } catch (ParseException pe) {
            res.status(400);
            return "Formato data inválido.";
        }
    }

    public Object getAll(Request req, Response res) {
        List<Edital> lista = editalDAO.getAllEditais();
        if (lista == null || lista.isEmpty()) {
            res.status(404);
            return "Nenhum edital encontrado.";
        }
        res.type("application/json");
        return new com.google.gson.Gson().toJson(lista);
    }

    public Object getById(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params("id"));
            Edital e = editalDAO.getEdital(id);
            if (e == null) {
                res.status(404);
                return "Edital não encontrado.";
            }
            res.type("application/json");
            return new com.google.gson.Gson().toJson(e);
        } catch (NumberFormatException ex) {
            res.status(400);
            return "ID inválido.";
        }
    }

    public Object update(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params("id"));
            Edital e = editalDAO.getEdital(id);
            if (e == null) {
                res.status(404);
                return "Edital não encontrado.";
            }

            String arquivo = req.queryParams("arquivo");
            String dataPubliStr = req.queryParams("data_publicacao");

            if (arquivo != null && !arquivo.isEmpty()) e.setArquivo(arquivo);
            if (dataPubliStr != null && !dataPubliStr.isEmpty()) {
                Date d = formato.parse(dataPubliStr);
                e.setData_publicacao(new java.sql.Date(d.getTime()));
            }

            boolean ok = editalDAO.update(e);
            if (ok) {
                res.status(200);
                return "Edital atualizado.";
            } else {
                res.status(500);
                return "Erro ao atualizar edital.";
            }
        } catch (ParseException pe) {
            res.status(400);
            return "Formato data inválido.";
        } catch (NumberFormatException ne) {
            res.status(400);
            return "ID inválido.";
        }
    }

    public Object delete(Request req, Response res) {
        try {
            int id = Integer.parseInt(req.params("id"));
            boolean ok = editalDAO.delete(id);
            if (ok) {
                res.status(200);
                return "Edital deletado.";
            } else {
                res.status(404);
                return "Erro ao deletar edital.";
            }
        } catch (NumberFormatException ne) {
            res.status(400);
            return "ID inválido.";
        }
    }
}