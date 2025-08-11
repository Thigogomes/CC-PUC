package Services;

import static spark.Spark.*;

import Classes.ProcessamentoImagem;
import dao.ProcessamentoImagemDAO;
import SI.ExtractTextFromImage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Base64;

public class SistemaInteligenteService {

    private final ProcessamentoImagemDAO dao = new ProcessamentoImagemDAO();

    public SistemaInteligenteService() {
        // registra a rota /upload
    	post("/upload", (req, res) -> {
    	    res.type("application/json");

    	    JsonObject json = JsonParser.parseString(req.body()).getAsJsonObject();
    	    String base64Image = json.get("image").getAsString();

    	    try {
    	        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
    	        String fileName = "foto_" + System.currentTimeMillis() + ".png";

    	        // **1. Cria o diretório uploads se não existir**
    	        File uploadDir = new File("uploads");
    	        if (!uploadDir.exists()) {
    	            uploadDir.mkdirs();
    	        }

    	        // **2. Escreve o arquivo dentro de uploads/**
    	        File outputFile = new File(uploadDir, fileName);
    	        try (OutputStream out = new FileOutputStream(outputFile)) {
    	            out.write(imageBytes);
    	        }

    	        // **3. Extrai o texto (título)**
    	        String titulo = ExtractTextFromImage.analisarImagem(imageBytes);
    	        
    	        System.out.println(">> [DEBUG] Texto extraído pelo OCR: '" + titulo + "'");
    	        
    	        // **4. Persiste no banco, retorna JSON incluindo o título**
    	        ProcessamentoImagem pi = new ProcessamentoImagem(
    	            fileName,
    	            titulo,
    	            LocalDateTime.now()
    	        );
    	        dao.save(pi);

    	        JsonObject resp = new JsonObject();
    	        resp.addProperty("status", "ok");
    	        resp.addProperty("file", fileName);
    	        resp.addProperty("titulo", titulo != null ? titulo : "");
    	        return resp.toString();

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
