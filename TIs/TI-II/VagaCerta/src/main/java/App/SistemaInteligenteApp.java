package App;

import static spark.Spark.*;

import SI.ExtractTextFromImage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.util.Base64;

public class SistemaInteligenteApp {

    public static void main(String[] args) {

        // Cria pasta uploads se não existir
        File uploadDir = new File("uploads");
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        //port(8086); // Define a porta do servidor
        //staticFiles.externalLocation("src/main/resources/ti-1-ppl-cc-m-20242-g7-concursos-master/ti-1-ppl-cc-m-20242-g7-concursos-master/codigo/public");

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
        
    }
}

