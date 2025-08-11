/*package App;



import static spark.Spark.*;

import Services.ConcursoService;
import dao.*;

public class ConcursoApp {
    public static void main(String[] args) {
        staticFiles.externalLocation("src/main/resources/ti-1-ppl-cc-m-20242-g7-concursos-master/" + "ti-1-ppl-cc-m-20242-g7-concursos-master/codigo/public");
        DAO dao = new DAO();

        port(8085);

        // Habilita POST “especiais” para atualizar e deletar via formulário HTML
        // ao invés de usar antes((req,res)->req.requestMethod(...))
        // definimos endpoints POST para aqueles casos:
        post("/concursos/:id", (req, res) -> {
            String method = req.queryParams("_method");
            if ("PUT".equalsIgnoreCase(method)) {
                return new ConcursoService().update(req, res);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                return new ConcursoService().delete(req, res);
            } else {
                halt(405, "Método não permitido");
                return null;
            }
        });

        dao.conectar();

        new ConcursoService();
        System.out.println("ConcursoService iniciado!");
    }
}
*/