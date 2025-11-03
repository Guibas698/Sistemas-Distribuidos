
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServidorQ5 {

    public static List<Suplemento> estoque = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        
        estoque.add(new Suplemento("Whey Protein", 50));
        estoque.add(new Suplemento("Creatina", 100));
        
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

    
    public static class Suplemento {
        private String nome;
        private int quantidade;

        public Suplemento(String nome, int quantidade) {
            this.nome = nome;
            this.quantidade = quantidade;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        @Override
        public String toString() {
            return "Suplemento{" +
                    "nome='" + nome + '\'' +
                    ", quantidade=" + quantidade +
                    '}';
        }
    }
}