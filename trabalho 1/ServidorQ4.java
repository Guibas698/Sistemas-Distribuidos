import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;



public class ServidorQ4 {
    public static void main(String[] args) {
        // aqui simula um banco de dados simples.
        int estoqueWhey = 50;
        int estoqueCreatina = 100;

        try (ServerSocket servidorSocket = new ServerSocket(9090)) {
            System.out.println("Servidor iniciado. Aguardando cliente");

            // aqui o servidor fica bloqueado até o cliente se conectar.
            Socket clienteSocket = servidorSocket.accept();
            System.out.println(">>> Cliente se conectou <<< " + clienteSocket.getInetAddress().getHostAddress());

            // aqui prepara os canais para enviar os bytes.
            DataInputStream entrada = new DataInputStream(clienteSocket.getInputStream());
            DataOutputStream saida = new DataOutputStream(clienteSocket.getOutputStream());

            // aqui é desempacotando a requisição do cliente.
            int idSuplementoRecebido = entrada.readInt();
            System.out.println("Servidor recebeu a consulta para o ID: " + idSuplementoRecebido);

            // aqui processa a requisição.
            int quantidadeEmEstoque = 0;
            if (idSuplementoRecebido == 101) {
                quantidadeEmEstoque = estoqueWhey;
            } else if (idSuplementoRecebido == 202) {
                quantidadeEmEstoque = estoqueCreatina;
            } else {
                System.out.println("ID desconhecido.");
            }

            // aqui o servidor empacota a resposta e envia.
            saida.writeInt(quantidadeEmEstoque);
            System.out.println("Servidor enviou a quantidade no estoque : " + quantidadeEmEstoque);
            
            // Fecha a conexão com este cliente.
            clienteSocket.close();
            System.out.println(">>> Conexão com o cliente encerrada <<<");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}