import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SuplementoTcpClient {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 9092;
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);

        Suplemento[] suplementos = {
            new Suplemento("Whey Protein", "Marca X", 250.50),
            new Suplemento("Creatina", "Marca Y", 120.75),
            new Suplemento("Vitamina C", "Marca Z", 45.0)
        };

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Conectado ao servidor " + host + ":" + port);
            OutputStream out = socket.getOutputStream();
            PojoEscolhidoOutputStream writer = new PojoEscolhidoOutputStream(suplementos, suplementos.length, out);
            writer.escreverDados();
            System.out.println("Dados enviados. Fechando conexão.");
        }
    }
}
