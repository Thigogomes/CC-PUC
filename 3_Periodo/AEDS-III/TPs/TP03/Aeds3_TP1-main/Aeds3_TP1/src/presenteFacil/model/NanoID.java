package src.presenteFacil.model;

import java.security.SecureRandom;
    
public class NanoID {

    private static String alfabeto = "useandom-26T198340PX75pxJACKVERYMINDBUSHWOLF_GQZbfghjklqvwyzrict";
    private static SecureRandom random = new SecureRandom();

    public static String nanoId(){
        return criarNanoId(alfabeto);
    }
    
    public static String criarNanoId(String alfabeto){

        StringBuilder id = new StringBuilder(10);
        
        for(int i = 0; i < 10; i++){
            int index = random.nextInt(alfabeto.length());
            id.append(alfabeto.charAt(index));
        }
        
        return id.toString();
    }
}
