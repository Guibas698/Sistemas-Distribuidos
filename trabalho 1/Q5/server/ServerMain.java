// server/ServerMain.java
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class ServerMain {
    public static final int TCP_PORT = 7070;

    public static void main(String[] args) throws Exception {
        VotingService service = new VotingService();
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket ss = new ServerSocket(TCP_PORT)) {
            System.out.println("Servidor Q5 ouvindo TCP " + TCP_PORT);
            while (true) {
                Socket s = ss.accept();
                pool.submit(() -> handleClient(s, service));
            }
        }
    }

    static void handleClient(Socket s, VotingService svc) {
        try (s;
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {

            VotingService.Role role = null;
            String user = null;
            String line;
            while ((line = br.readLine()) != null) {
                String resp = process(line, svc, role, user);
                // atualização de contexto após LOGIN
                if (line.contains("\"op\":\"LOGIN\"")) {
                    String u = extract(line, "user");
                    String p = extract(line, "pass");
                    VotingService.Role r = svc.login(u, p);
                    if (r == null) { resp = "{\"ok\":false,\"err\":\"login\"}"; }
                    else { resp = "{\"ok\":true,\"role\":\"" + r.name() + "\"}"; role = r; user = u; }
                }
                bw.write(resp); bw.write("\n"); bw.flush();
            }
        } catch (IOException e) {
            System.err.println("Conexão encerrada: " + e.getMessage());
        }
    }

    static String process(String json, VotingService svc, VotingService.Role role, String user) {
        try {
            if (json.contains("\"op\":\"LIST\"")) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\"ok\":true,\"candidatos\":[");
                boolean first = true;
                for (String c : svc.listAsJson()) {
                    if (!first) sb.append(",");
                    sb.append(c);
                    first = false;
                }
                sb.append("]}");
                return sb.toString();
            }
            if (json.contains("\"op\":\"VOTE\"")) {
                if (user == null) return "{\"ok\":false,\"err\":\"login\"}";
                int id = Integer.parseInt(extractNumber(json, "suplementoId"));
                return svc.vote(user, id);
            }
            if (json.contains("\"op\":\"RESULTS\"")) {
                return svc.resultsJson();
            }
            if (json.contains("\"op\":\"OPEN\"")) {
                if (role != VotingService.Role.ADMIN) return "{\"ok\":false,\"err\":\"admin\"}";
                int sec = Integer.parseInt(extractNumber(json, "durationSec"));
                svc.openForSeconds(sec);
                MulticastNotifier.sendInfo("Votação aberta por " + sec + "s");
                return "{\"ok\":true}";
            }
            if (json.contains("\"op\":\"CLOSE\"")) {
                if (role != VotingService.Role.ADMIN) return "{\"ok\":false,\"err\":\"admin\"}";
                svc.close();
                MulticastNotifier.sendInfo("Votação encerrada");
                return "{\"ok\":true}";
            }
            if (json.contains("\"op\":\"ADD_CANDIDATE\"")) {
                if (role != VotingService.Role.ADMIN) return "{\"ok\":false,\"err\":\"admin\"}";
                String nome = extract(json, "nome");
                int id = svc.addCandidate(nome);
                MulticastNotifier.sendInfo("Novo candidato: " + nome + " (id " + id + ")");
                return "{\"ok\":true,\"id\":" + id + "}";
            }
            if (json.contains("\"op\":\"REMOVE_CANDIDATE\"")) {
                if (role != VotingService.Role.ADMIN) return "{\"ok\":false,\"err\":\"admin\"}";
                int id = Integer.parseInt(extractNumber(json, "suplementoId"));
                boolean ok = svc.removeCandidate(id);
                if (ok) MulticastNotifier.sendInfo("Candidato removido: id " + id);
                return ok ? "{\"ok\":true}" : "{\"ok\":false,\"err\":\"not_found\"}";
            }
            if (json.contains("\"op\":\"LOGIN\"")) {
                // LOGIN é tratado em handleClient para atualizar 'role' e 'user' corretamente
                return "{\"ok\":false,\"err\":\"internal\"}";
            }
            return "{\"ok\":false,\"err\":\"op\"}";
        } catch (Exception e) {
            return "{\"ok\":false,\"err\":\"bad_request\"}";
        }
    }

    static String extract(String json, String field) {
        int i = json.indexOf("\"" + field + "\"");
        if (i < 0) return "";
        int c = json.indexOf(":", i);
        int q1 = json.indexOf("\"", c + 1);
        int q2 = json.indexOf("\"", q1 + 1);
        return json.substring(q1 + 1, q2);
    }
    static String extractNumber(String json, String field) {
        int i = json.indexOf("\"" + field + "\"");
        if (i < 0) return "0";
        int c = json.indexOf(":", i);
        int end = json.indexOf(",", c + 1);
        if (end < 0) end = json.indexOf("}", c + 1);
        return json.substring(c + 1, end).replaceAll("[^0-9-]", "");
    }
}
