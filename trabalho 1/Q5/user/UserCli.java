// user/UserCli.java (rev: handle vote errors & format list)
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UserCli {
    private static String extract(String json, String field) {
        int i = json.indexOf("\"" + field + "\"");
        if (i < 0) return "";
        int c = json.indexOf(":", i);
        int q1 = json.indexOf("\"", c + 1);
        int q2 = json.indexOf("\"", q1 + 1);
        if (q1 < 0 || q2 < 0) return "";
        return json.substring(q1 + 1, q2);
    }
    private static String extractErr(String json) {
        if (!json.contains("\"ok\":false")) return "";
        if (json.contains("\"err\":\"deadline\"")) return "Votação ainda NÃO está aberta. Peça ao admin para abrir (open <segundos>).";
        if (json.contains("\"err\":\"duplicado\"")) return "Você já votou nesta votação.";
        if (json.contains("\"err\":\"not_found\"")) return "ID de suplemento inexistente.";
        if (json.contains("\"err\":\"login\"")) return "Faça login primeiro.";
        if (json.contains("\"err\":\"admin\"")) return "Operação restrita a administradores.";
        return "Erro ao processar a operação.";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("User CLI — conectando em localhost:7070");
        Thread t = new Thread(new MulticastListener());
        t.setDaemon(true);
        t.start();

        try (Socket s = new Socket("127.0.0.1", 7070)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));

            Scanner sc = new Scanner(System.in);
            System.out.print("Usuário: ");
            String user = sc.nextLine().trim();
            //a senha é 123
            System.out.print("Senha: ");
            String pass = sc.nextLine().trim();
            bw.write("{\"op\":\"LOGIN\",\"user\":\"" + user.replace("\"","'") + "\",\"pass\":\"" + pass.replace("\"","'") + "\"}\n"); bw.flush();
            String loginResp = br.readLine();
            System.out.println("LOGIN -> " + loginResp);

            System.out.println("Comandos: list | vote <id> | results | quit");
            while (true) {
                System.out.print("> ");
                if (!sc.hasNextLine()) break;
                String line = sc.nextLine().trim();
                if (line.equalsIgnoreCase("quit")) break;

                if (line.equals("list")) {
                    bw.write("{\"op\":\"LIST\"}\n"); bw.flush();
                    String resp = br.readLine();
                    // Tenta imprimir formatado
                    if (resp.startsWith("{\"ok\":true,\"candidatos\":[")) {
                        System.out.println("CANDIDATOS:");
                        String arr = resp.substring(resp.indexOf('[')+1, resp.lastIndexOf(']'));
                        if (arr.trim().isEmpty()) { System.out.println(" (lista vazia)"); continue; }
                        String[] itens = arr.split("\\},\\{");
                        for (String it : itens) {
                            String e = it;
                            if (!e.startsWith("{")) e = "{" + e;
                            if (!e.endsWith("}")) e = e + "}";
                            String id = e.replaceAll(".*\"id\":(\\d+).*", "$1");
                            String nome = e.replaceAll(".*\"nome\":\"([^\"]+)\".*", "$1");
                            System.out.println("  " + id + " - " + nome);
                        }
                    } else {
                        System.out.println(resp);
                    }
                    continue;
                }

                if (line.startsWith("vote")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length < 2) {
                        System.out.println("Uso: vote <id>");
                        continue;
                    }
                    int id = Integer.parseInt(parts[1]);
                    bw.write("{\"op\":\"VOTE\",\"suplementoId\":" + id + "}\n"); bw.flush();
                    String resp = br.readLine();
                    if (resp.contains("\"ok\":true")) {
                        System.out.println("Voto confirmado no candidato #" + id + "!");
                    } else {
                        System.out.println("Falha ao votar: " + extractErr(resp));
                    }
                    continue;
                }

                if (line.equals("results")) {
                    bw.write("{\"op\":\"RESULTS\"}\n"); bw.flush();
                    System.out.println(br.readLine());
                    continue;
                }

                System.out.println("Comando inválido.");
            }
        }
    }
}
