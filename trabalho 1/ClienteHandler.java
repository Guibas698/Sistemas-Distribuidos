// Arquivo: ClienteHandler.java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClienteHandler implements Runnable {
    
    private Socket clienteSocket;
    private Gson gson = new Gson();

    public ClienteHandler(Socket socket) {
        this.clienteSocket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true);
        ) {
            String jsonRequest;
            while ((jsonRequest = entrada.readLine()) != null) {
                
                System.out.println(">>> [Thread] Servidor recebeu JSON: " + jsonRequest);

                JsonObject requestObj = JsonParser.parseString(jsonRequest).getAsJsonObject();
                String operacao = requestObj.get("operacao").getAsString();

                if (operacao.equals("listar_suplementos")) {
                    String jsonResponse = gson.toJson(ServidorQ5.estoque);
                    saida.println(jsonResponse);

                } else if (operacao.equals("adicionar_suplemento")) {
                    JsonObject supJson = requestObj.getAsJsonObject("novoSuplemento");
                    Suplemento novoSuplemento = gson.fromJson(supJson, Suplemento.class);
                    ServidorQ5.estoque.add(novoSuplemento);
                    saida.println("{\"status\":\"Suplemento adicionado com sucesso!\"}");
                }
            }
        } catch (IOException e) {
            System.out.println(">>> [Thread] Cliente desconectado.");
        }
    }
}