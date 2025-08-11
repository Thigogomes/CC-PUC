package Services;

import static spark.Spark.*;
import dao.UsuarioDAO;
import Classes.Usuario;
import spark.Request;
import spark.Response;

public class UsuarioService {
	private UsuarioDAO dao = new UsuarioDAO();
	
	public UsuarioService() {
        path("/", () -> {
            // CREATE via form HTML (url-encoded)
            post("usuario", this::insert);

            // READ ALL / READ ONE
            get("usuario/:cpf", this::getByCpf);

            // UPDATE via form HTML (override _method=PUT)
            put("usuario/:cpf", this::update);

            // DELETE via form HTML (override _method=DELETE)
            delete("usuario/:cpf", this::deleteUsuario);
        });
    }

	private Object insert(Request req, Response res) {
        // lê campos do form
		String cpf = req.queryParams("login");
        String nome = req.queryParams("nome");
        String email = req.queryParams("email");
        String escolaridade = req.queryParams("escolaridade");
        String senha = req.queryParams("senha");

        // gera objeto e persiste
        Usuario u = new Usuario(cpf, nome, escolaridade, senha, email);
        boolean ok = dao.inserirUsuario(u);

        // após criar, redireciona de volta à página
        if (ok) {
        	res.redirect("/login.html");
        } else {
        	res.status(500);
            return "Erro ao cadastrar usuario.";
        }
        return "";
    }
	
    public Object getByCpf(Request req, Response res) {
        try {
            String cpf = req.params("cpf");
            Usuario u = dao.getUsuarioByCpf(cpf);
            if (u == null) {
                res.status(404);
                return "Usuario não encontrado.";
            }
            res.type("application/json");
            return new com.google.gson.Gson().toJson(u);
        } catch (NumberFormatException ex) {
            res.status(400);
            return "CPF inválido.";
        }
    }
    
    public Object update(Request req, Response res) {
        String cpf = req.params("cpf");
		Usuario u = dao.getUsuarioByCpf(cpf);
		if (u == null) {
		    res.status(404);
		    return "Usuario não encontrado.";
		}
		
		u.setNome(req.queryParams("nome"));
		u.setEscolaridade(req.queryParams("escolaridade"));
		u.setSenha(req.queryParams("senha"));
		u.setEmail(req.queryParams("email"));

		boolean ok = dao.atualizarUsuario(u);
		if (ok) {
		    res.status(200);
		    return "Usuario atualizado.";
		} else {
		    res.status(500);
		    return "Erro ao atualizar usuario.";
		}
    }
    
    public Object deleteUsuario(Request req, Response res) {
        String cpf = req.params("cpf");
        boolean ok = dao.delete(cpf);
        if (ok) {
            res.redirect("/login.html");
        } else {
            res.status(404);
            return "Erro ao deletar usuario";
        }
        return "";
    
    }
}
