import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SuplementoTcpServer {
    private int port;

    public SuplementoTcpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("SuplementoTcpServer iniciado na porta " + port);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getRemoteSocketAddress());
                new Thread(() -> handleClient(client)).start();
            }
        }
    }

    private void handleClient(Socket client) {
        try (InputStream in = client.getInputStream()) {
            DataInputStream dis = new DataInputStream(in);
            while (true) {
                int nomeLen;
                try {
                    nomeLen = dis.readUnsignedByte();
                } catch (EOFException eof) {
                    break; // cliente fechou stream
                }
                byte[] nomeBytes = new byte[nomeLen];
                dis.readFully(nomeBytes);

                int marcaLen = dis.readUnsignedByte();
                byte[] marcaBytes = new byte[marcaLen];
                dis.readFully(marcaBytes);

                byte[] valorBytes = new byte[8];
                dis.readFully(valorBytes);

                String nome = new String(nomeBytes, StandardCharsets.UTF_8);
                String marca = new String(marcaBytes, StandardCharsets.UTF_8);
                double valor = bytesToDouble(valorBytes);

                System.out.println("[RECEBIDO] Nome: " + nome + ", Marca: " + marca + ", Valor: " + valor);
            }
        } catch (IOException e) {
            System.out.println("Erro no cliente: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {}
            System.out.println("Conexão com cliente encerrada.");
        }
    }

    private double bytesToDouble(byte[] bytes) {
        long longBits = 0;
        for (int i = 0; i < 8; i++) {
            longBits |= (long) (bytes[i] & 0xFF) << (56 - (i * 8));
        }
        return Double.longBitsToDouble(longBits);
    }

    public static void main(String[] args) throws IOException {
        int port = 9092;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        SuplementoTcpServer server = new SuplementoTcpServer(port);
        server.start();
    }
}
