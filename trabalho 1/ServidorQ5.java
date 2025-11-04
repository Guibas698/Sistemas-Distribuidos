
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServidorQ5 {

    public static List<Suplemento> estoque = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        
    // Inicializa com alguns suplementos (nome, marca, valor)
    estoque.add(new Suplemento("Whey Protein", "Marca X", 50.0));
    estoque.add(new Suplemento("Creatina", "Marca Y", 100.0));
        
        try (ServerSocket servidorSocket = new ServerSocket(9091)) { 
            System.out.println("Servidor Q5 (JSON + Multi-Thread) iniciado na porta 9091...");

            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                System.out.println(">>> Novo cliente conectado: " + clienteSocket.getInetAddress().getHostAddress());
                
                ClienteHandler garcom = new ClienteHandler(clienteSocket);
                new Thread(garcom).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    // Usamos a classe Suplemento definida em Suplemento.java (nome, marca, valor)
}