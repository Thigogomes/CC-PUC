import java.io.*;
import java.net.*;

class Servidor {
    private static int portaServidor = 6790;

    public static void main(String argv[]) throws Exception {

        try (ServerSocket socket = new ServerSocket(portaServidor)) {

            while (true) {
                Socket conexao = socket.accept();

                System.out.println("Aguardando datagrama do cliente...");
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

                String str = entrada.readLine();
                System.out.println("Received: " + str);

                str = str.toUpperCase() + '\n';

                DataOutputStream saída = new DataOutputStream(conexao.getOutputStream());

                saída.writeBytes(str);
            }
        }
    }
}