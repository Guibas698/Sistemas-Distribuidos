import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.*;

public class ServidorQ5 {
    // Listas compartilhadas
    public static List<Suplemento> suplementos = Collections.synchronizedList(new ArrayList<>());
    public static Map<String, Usuario> usuarios = Collections.synchronizedMap(new HashMap<>());
    public static Map<String, Integer> votos = Collections.synchronizedMap(new HashMap<>());
    public static boolean votacaoAtiva = true;
    
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SERVIDOR DE VOTAÇÃO ===");
        
        //INICIALIZAR DADOS
        inicializarDados();
        
        // TEMPORIZADOR PARA ENCERRAR VOTAÇÃO (60 segundos)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                votacaoAtiva = false;
                System.out.println(" VOTAÇÃO ENCERRADA!");
                mostrarResultados();
            }
        }, 60000); // 60 segundos
        
        //INICIAR SERVIDOR SOCKET
        try {
            ServerSocket servidorSocket = new ServerSocket(9091);
            System.out.println("Servidor iniciado na porta 9091");
            System.out.println(" Suplementos para votação:");
            
            for (Suplemento sup : suplementos) {
                System.out.println("   " + sup.toString());
                votos.put(sup.getNome(), 0); // Inicializar contador de votos
            }
            
            // Aceitar conexões do cliente
            while (true) {
                System.out.println(" Aguardando conexões...");
                Socket clienteSocket = servidorSocket.accept();
                System.out.println(" Cliente conectado: " + clienteSocket.getInetAddress().getHostAddress());
                
                // Criar thread para cada cliente
                ThreadCliente handler = new ThreadCliente(clienteSocket);
                new Thread(handler).start();
            }
            
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }
    
    private static void inicializarDados() {
        // Cadastrar usuários
        usuarios.put("admin", new Usuario("admin", "admin123", true));
        usuarios.put("joao", new Usuario("joao", "senha123", false));
        usuarios.put("maria", new Usuario("maria", "senha123", false));
        
        // Cadastrar suplementos para votação
        suplementos.add(new Suplemento(1, "Whey Protein", "Proteína"));
        suplementos.add(new Suplemento(2, "Creatina", "Força"));
        suplementos.add(new Suplemento(3, "Pré-Treino", "Energia"));
        suplementos.add(new Suplemento(4, "BCAA", "Recuperação"));
    }
    
    private static void mostrarResultados() {
        System.out.println("\n RESULTADOS FINAIS:");
        for (Map.Entry<String, Integer> entry : votos.entrySet()) {
            System.out.println("   " + entry.getKey() + ": " + entry.getValue() + " votos");
        }
    }
}

// Classe representando um usuário
class Usuario {
    private String username;
    private String password;
    private boolean admin;

    public Usuario(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return username + (admin ? " (admin)" : "");
    }
}

// A classe `Suplemento` agora é definida em Suplemento.java (unificada)

// Handler mínimo de cliente para compilar e fechar conexões
class ThreadCliente implements Runnable {
    private final Socket socket;

    public ThreadCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Implementação mínima: fechar conexão ao terminar.
            socket.close();
        } catch (IOException e) {
            System.out.println("Erro na conexão com cliente: " + e.getMessage());
        }
    }
}
