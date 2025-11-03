// Arquivo: ClienteQ5.java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

public class ClienteQ5 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 9091)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Conectado ao sistema de estoque como USUÁRIO.");
            
            String jsonRequest = "{\"operacao\": \"listar_suplementos\"}";
            
            System.out.println("Enviando pedido para listar suplementos...");
            out.println(jsonRequest); 
            
            String jsonResponse = in.readLine();
            
            System.out.println("\n--- ESTOQUE ATUAL (Resposta JSON do Servidor) ---");
            System.out.println(jsonResponse);
            System.out.println("--------------------------------------------------\n");

        } catch (IOException e) {
            System.out.println("ERRO: Não foi possível conectar ao ServidorQ5.");
        }
    }
}