import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;


public class ClienteQ4 {
    public static void main(String[] args) {
        // aqui faz a tentativa de se conectar com o servidor
        try (Socket socket = new Socket("localhost", 9090)) {
            
            // aqui prepara os canais para enviar e receber os bytes.
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());

            Scanner teclado = new Scanner(System.in);
            System.out.println("--- Consulta de Estoque ---");
            System.out.println("101 - Whey Protein");
            System.out.println("202 - Creatina");
            System.out.print("Digite o ID do suplemento: ");
            int idParaEnviar = teclado.nextInt();

            // aqui empacota a requisição e envia
            saida.writeInt(idParaEnviar);
            System.out.println(">>> Pedido foi enviado para o servidor <<<");

            // aqui desempacota a resposta do servidor.
            int quantidadeRecebida = entrada.readInt();
            
            System.out.println("\n--- Resposta do Servidor ---");
            System.out.println("Quantidade no estoque: " + quantidadeRecebida);
            System.out.println("--------------------------");

            teclado.close();

        } catch (IOException e) {
            System.out.println("ERRO: Não foi possível conectar ao servidor.");
            
        }
    }
}