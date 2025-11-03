// Arquivo: ClienteAdminQ5.java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

public class ClienteAdminQ5 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 9091)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Conectado como ADMINISTRADOR.");
            
            String jsonRequest = "{\"operacao\": \"adicionar_suplemento\", " +
                                 "\"novoSuplemento\": {\"nome\": \"Vitamina C\", \"estoque\": 200}" +
                                 "}";
            
            System.out.println("Enviando pedido para adicionar Vitamina C...");
            out.println(jsonRequest);
            
            String jsonResponse = in.readLine();
            
            System.out.println("\n--- RESPOSTA DO SERVIDOR ---");
            System.out.println(jsonResponse);
            System.out.println("----------------------------\n");

        } catch (IOException e) {
            System.out.println("ERRO: Não foi possível conectar ao ServidorQ5.");
        }
    }
}