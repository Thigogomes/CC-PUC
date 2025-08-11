package Services;

import static spark.Spark.*;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import Classes.Admin;
import dao.AdminDAO;
import spark.Request;
import spark.Response;

public class AdminService {
    private AdminDAO dao = new AdminDAO();
    private Gson gson = new Gson();

    public AdminService() {
        path("/", () -> {
            // CREATE
            post("admin", this::createAdmin);
            post("admin/login", this::loginAdmin, gson::toJson);

            // READ ALL
            get("admin", this::getAllAdmins, gson::toJson);

            // READ ONE
            get("admin/:id", this::getAdminById, gson::toJson);
            

            // UPDATE
            put("admin/:id", this::updateAdmin);

            // DELETE
            delete("admin/:id", this::deleteAdmin);
        });
    }

    private Object createAdmin(Request req, Response res) {
        String user     = req.queryParams("user");
        String password = req.queryParams("password");
        String nome     = req.queryParams("nome");

        Admin admin = new Admin();
        admin.setUser(user);
        admin.setPassword(password);
        admin.setNome(nome);

        boolean ok = dao.inserirAdmin(admin);
        if (ok) {
            res.status(201);
            return gson.toJson(admin);
        } else {
            res.status(500);
            return "Erro ao criar Admin.";
        }
    }

    private Object getAllAdmins(Request req, Response res) {
        List<Admin> list = dao.listAll();
        res.type("application/json");
        return list;
    }

    private Object getAdminById(Request req, Response res) {
        int id = Integer.parseInt(req.params("id"));
        Admin admin = dao.getAdminById(id);
        if (admin != null) {
            res.type("application/json");
            return admin;
        } else {
            res.status(404);
            return "Admin não encontrado.";
        }
    }

    private Object updateAdmin(Request req, Response res) {
        int id = Integer.parseInt(req.params("id"));
        Admin admin = dao.getAdminById(id);
        if (admin == null) {
            res.status(404);
            return "Admin não encontrado.";
        }

        String user     = req.queryParams("user");
        String password = req.queryParams("password");
        String nome     = req.queryParams("nome");

        if (user != null && !user.isEmpty()) admin.setUser(user);
        if (password != null && !password.isEmpty()) admin.setPassword(password);
        if (nome != null && !nome.isEmpty()) admin.setNome(nome);

        boolean ok = dao.atualizarAdmin(admin);
        if (ok) {
            res.status(200);
            return gson.toJson(admin);
        } else {
            res.status(500);
            return "Erro ao atualizar Admin.";
        }
    }

    private Object deleteAdmin(Request req, Response res) {
        int id = Integer.parseInt(req.params("id"));
        boolean ok = dao.deleteAdmin(id);
        if (ok) {
            res.status(200);
            return "Admin excluído com sucesso.";
        } else {
            res.status(404);
            return "Admin não encontrado.";
        }
    }
    
    private Object loginAdmin(Request req, Response res) {
        String user     = req.queryParams("user");
        String password = req.queryParams("password");

        if (user == null || password == null) {
            res.status(400);
            return Map.of("error","Parametros faltando");
        }

        Admin admin = dao.getByUser(user);
        if (admin == null) {
            res.status(404);
            return Map.of("error","Admin não encontrado");
        }
        if (!admin.getPassword().equals(password)) {
            res.status(401);
            return Map.of("error","Senha inválida");
        }
        res.status(200);
        return Map.of("message","Login OK");
    }
}
