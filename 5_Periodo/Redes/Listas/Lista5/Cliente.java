import java.io.*;
import java.net.*;

class Cliente {
    private static String ipServidor = "192.168.255.10";
    private static int portaServidor = 6790;

    public static String lerString() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        return in.readLine();
    }

    public static void main(String argv[]) throws Exception {
        Socket socket = new Socket(ipServidor, portaServidor);

        DataOutputStream saída = new DataOutputStream(socket.getOutputStream());
        saída.writeBytes(lerString() + '\n');

        BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("FROM SERVER: " + entrada.readLine());

        socket.close();
    }
}