package SI;

import com.azure.ai.vision.imageanalysis.*;
import com.azure.ai.vision.imageanalysis.models.*;
import com.azure.core.credential.KeyCredential;

import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths; 
import com.azure.core.util.BinaryData; 
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.util.Base64;

public class ExtractTextFromImage {
    public static String analisarImagem(byte[] imageDataBytes) throws Exception {

    	String endpoint = "https://aiservicesvagacerta.cognitiveservices.azure.com/";
    	String key = "Dn9hXIBPQWuLVXCZouOmN3d8tuAR2kGmvmZC7ArCQeZ3neVXuXVgJQQJ99BFACYeBjFXJ3w3AAAEACOGLH8P";

        // Cria o cliente de análise de imagem
        ImageAnalysisClient client = new ImageAnalysisClientBuilder()
            .endpoint(endpoint)
            .credential(new KeyCredential(key))
            .buildClient();

        // URL da imagem que será analisada
        //String imageUrl = "https://learn.microsoft.com/azure/ai-services/computer-vision/media/quickstarts/presentation.png";
        String imagePath = "src/main/resources/ti-1-ppl-cc-m-20242-g7-concursos-master/ti-1-ppl-cc-m-20242-g7-concursos-master/docs/images/capa-adm3.jpg";
        
        //byte[] imageDataBytes = Files.readAllBytes(Paths.get(imagePath));
        BinaryData imageData = BinaryData.fromBytes(imageDataBytes);
        
        // Faz análise com OCR (leitura de texto)
        /*ImageAnalysisResult result = client.analyzeFromUrl(
            imagePath,
            Arrays.asList(VisualFeatures.READ),
            new ImageAnalysisOptions());*/
        
        ImageAnalysisResult result = client.analyze(
                imageData,                                  
                Arrays.asList(VisualFeatures.READ),
                new ImageAnalysisOptions());  

        // Imprime o texto detectado
        
        /*System.out.println("Texto detectado:");
        for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
            System.out.println("Linha: " + line.getText());
        }*/
        
        // Converte para String o titulo do livro
        StringBuilder textoDetectado = new StringBuilder();

        for (DetectedTextLine line : result.getRead().getBlocks().get(0).getLines()) {
            textoDetectado.append(line.getText()).append(" ");
        }
        
        String tituloDoLivro = textoDetectado.toString();
        //System.out.println("\nTitulo: " + tituloDoLivro);
        
        return tituloDoLivro;
    }
}
