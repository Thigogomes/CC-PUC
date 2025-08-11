/*package App;

import static spark.Spark.*;

import Services.EditalServices;
import dao.*;

public class EditalApp {
	private static EditalServices editalService = new EditalServices();
	
	public static void main(String[] args) {
		staticFiles.externalLocation("src/main/resources/ti-1-ppl-cc-m-20242-g7-concursos-master/ti-1-ppl-cc-m-20242-g7-concursos-master/codigo/public");
		port(8086);
		post("/edital", (request, response) -> editalService.insert(request, response));
		//get("/concurso/delete/:id", (request, response) -> concursosService.delete(request, response));
		//get("/concurso/:id", (request, response) -> concursosService.get(request, response));
		//post("/concurso/update/:id", (request, response) -> concursosService.update(request, response));
	}

}
*/