package Services;

import static spark.Spark.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import dao.LivroDAO;
import Classes.Livro;
import spark.Request;
import spark.Response;

public class LivroService {
    private LivroDAO dao = new LivroDAO();
    private Gson gson = new Gson();

    public LivroService() {
        path("/", () -> {
            // CREATE via form HTML (url-encoded)
            post("livros", this::createLivro);

            // READ ALL / READ ONE → retornam JSON ou redirecionam se desejar
            get("livros", this::getAllLivros);
            get("livros/:id", this::getLivroById);

            // Rota de override: trata formulários com _method=PUT ou DELETE
            post("livros/:id", (req, res) -> {
                String method = req.queryParams("_method");
                if (method != null) {
                    if ("DELETE".equalsIgnoreCase(method)) {
                        // Chama o método de delete
                        return deleteLivro(req, res);
                    } else if ("PUT".equalsIgnoreCase(method)) {
                        return updateLivro(req, res);
                    }
                }
                res.status(405);
                return "Método não suportado";
            });

            // EXIBIR DETALHES: rota para redirecionar ao HTML
            get("livros/exibir/:id", this::exibirLivro);

            // UPDATE via form HTML direto para PUT (se usar JS ou cliente REST)
            put("livros/:id", this::updateLivro);

            // DELETE via cliente REST ou fetch
            delete("livros/:id", this::deleteLivro);
        });
    }

    // POST /livros
    private Object createLivro(Request req, Response res) {
        String titulo = req.queryParams("titulo");
        String autor = req.queryParams("autor");
        String versaoParam = req.queryParams("versao");
        int versao = 0;
        try {
            versao = Integer.parseInt(versaoParam);
        } catch (NumberFormatException e) {
            res.status(400);
            return "Versão inválida.";
        }
        String materia = req.queryParams("materia");
        String link = req.queryParams("link");

        Livro l = new Livro(0, titulo, autor, versao, materia, link);
        boolean ok = dao.inserir(l);

        if (ok) {
            res.redirect("/biblioteca.html");
        } else {
            res.status(500);
            return "Erro ao cadastrar livro";
        }
        return "";
    }

    // GET /livros
    public Object getAllLivros(Request req, Response res) {
        String search = req.queryParams("search");
        String materia = req.queryParams("materia");
        List<Livro> lista = dao.listar(search, materia);
        res.type("application/json");
        return gson.toJson(lista);
    }
    
    // GET /livros/:id → retorna JSON do objeto ou 404
    public Object getLivroById(Request req, Response res) {
        int id;
        try {
            id = Integer.parseInt(req.params("id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "ID inválido";
        }
        Livro l = dao.get(id);
        if (l == null) {
            res.status(404);
            return "Livro não encontrado";
        }
        res.type("application/json");
        return gson.toJson(l);
    }

    // GET /livros/exibir/:id → redireciona para a página HTML de detalhes
    private Object exibirLivro(Request req, Response res) {
        int id;
        try {
            id = Integer.parseInt(req.params("id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "ID inválido.";
        }
        Livro l = dao.get(id);
        if (l != null) {
            // Monta URL para detalhes_livros.html com query params
            StringBuilder sb = new StringBuilder();
            sb.append("/detalhes_livros.html?");
            sb.append("id=").append(l.getId());
            sb.append("&titulo=").append(encode(l.getTitulo()));
            sb.append("&autor=").append(encode(l.getAutor()));
            sb.append("&versao=").append(l.getVersao());
            sb.append("&materia=").append(encode(l.getMateria()));
            sb.append("&link=").append(encode(l.getLink()));
            res.redirect(sb.toString());
        } else {
            res.status(404);
            return "Livro não encontrado.";
        }
        return null;
    }

    // PUT /livros/:id
    public Object updateLivro(Request req, Response res) {
        int id;
        try {
            id = Integer.parseInt(req.params("id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "ID inválido";
        }
        Livro existing = dao.get(id);
        if (existing == null) {
            res.status(404);
            return "Livro não encontrado";
        }

        String titulo   = req.queryParams("titulo");
        String autor    = req.queryParams("autor");
        String versaoS  = req.queryParams("versao");
        String materia  = req.queryParams("materia");
        String link     = req.queryParams("link");

        if (titulo  != null && !titulo.isEmpty())  existing.setTitulo(titulo);
        if (autor   != null && !autor.isEmpty())   existing.setAutor(autor);
        if (versaoS != null && !versaoS.isEmpty()) {
            try {
                existing.setVersao(Integer.parseInt(versaoS));
            } catch (NumberFormatException e) {
                res.status(400);
                return "Versão inválida.";
            }
        }
        if (materia != null && !materia.isEmpty()) existing.setMateria(materia);
        if (link    != null && !link.isEmpty())    existing.setLink(link);

        boolean ok = dao.update(existing);
        if (ok) {
            res.redirect("/biblioteca.html");
        } else {
            res.status(500);
            return "Erro ao atualizar livro";
        }
        return "";
    }

    // DELETE /livros/:id
    public Object deleteLivro(Request req, Response res) {
        int id;
        try {
            id = Integer.parseInt(req.params("id"));
        } catch (NumberFormatException e) {
            res.status(400);
            return "ID inválido";
        }
        boolean ok = dao.delete(id);
        if (ok) {
            res.redirect("/biblioteca.html");
        } else {
            res.status(404);
            return "Erro ao deletar livro";
        }
        return "";
    }

    // URL-encode de parâmetros
    private String encode(String valor) {
        if (valor == null) return "";
        try {
            return URLEncoder.encode(valor, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return valor;
        }
    }
}
