package App;

import static spark.Spark.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import SI.ExtractTextFromImage;
import Services.*;
import dao.DAO;

public class Main {
    public static void main(String[] args) {
        // Porta
        port(8086);

        // Serve HTML/CSS/JS de src/main/resources/public
        staticFiles.externalLocation("src/main/resources/ti-1-ppl-cc-m-20242-g7-concursos-master/" + "ti-1-ppl-cc-m-20242-g7-concursos-master/codigo/public");

        
        DAO dao = new DAO();
        if (!dao.conectar()) {
            System.err.println("Falha ao conectar ao BD");
            System.exit(1);
        }

        // Registra rotas de Concurso
        post("/concursos/:id", (req, res) -> {
            String method = req.queryParams("_method");
            if ("PUT".equalsIgnoreCase(method)) {
                return new ConcursoService().updateConcurso(req, res);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                return new ConcursoService().delete(req, res);
            } else {
                halt(405, "Método não permitido");
                return null;
            }
        });
        new ConcursoService();
        System.out.println("ConcursoService iniciado!");

        // Registra rotas de Livro
        post("/livros/:id", (req, res) -> {
            String method = req.queryParams("_method");
            if ("PUT".equalsIgnoreCase(method)) {
                return new LivroService().updateLivro(req, res);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                return new LivroService().deleteLivro(req, res);
            } else {
                halt(405, "Método não permitido");
                return null;
            }
        });
        
        /*
        //Conecta o Sistema Inteligente
        post("/upload", (req, res) -> {
            res.type("application/json");

            JsonObject json = JsonParser.parseString(req.body()).getAsJsonObject();
            String base64Image = json.get("image").getAsString();

            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                
                if(imageBytes != null) {
                	String tituloDoLivro = ExtractTextFromImage.analisarImagem(imageBytes);
                	System.out.println("\n******************************************************\n");
                	System.out.println("\nTitulo: " + tituloDoLivro);
                	System.out.println("\n\n******************************************************\n");
                }
                
                String fileName = "foto_" + System.currentTimeMillis() + ".png";

                try (OutputStream out = new FileOutputStream("uploads/" + fileName)) {
                    out.write(imageBytes);
                }

                JsonObject response = new JsonObject();
                response.addProperty("status", "ok");
                response.addProperty("file", fileName);
                return response.toString();

            } catch (Exception e) {
                res.status(500);
                JsonObject error = new JsonObject();
                error.addProperty("status", "error");
                error.addProperty("message", e.getMessage());
                return error.toString();
            }
        });
        
        new SistemaInteligenteApp();
        System.out.println("Sistema Inteligente iniciado!");
        */
        new LivroService();
        System.out.println("LivroService iniciado!");
        
        new UsuarioService();
        System.out.println("UsuarioService iniciado!");
        
        new AdminService();
        System.out.println("AdminService iniciado!");
        
        new SistemaInteligenteService();
        System.out.println("SistemaInteligenteService iniciado!");
        
        // Aqui, a aplicação fica escutando todas as rotas configuradas
        System.out.println("Main rodando na porta 8086 com todas as rotas.");

    }
}