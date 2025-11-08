// server/VotingService.java
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VotingService {
    public enum Role { USER, ADMIN }

    private final Map<Integer, String> candidatos = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> votos = new ConcurrentHashMap<>();
    private final Set<String> votantes = ConcurrentHashMap.newKeySet();

    private volatile Instant deadline = Instant.now().minusSeconds(1); // inicia fechada

    public VotingService() {
        // candidatos padrão: WheyProtein, Creatina, Vitaminas, PreTreino
        addCandidate("WheyProtein");
        addCandidate("Creatina");
        addCandidate("Vitaminas");
        addCandidate("PreTreino");
    }

    // ---- auth simples ----
    public Role login(String user, String pass) {
        if ("admin".equals(user) && "admin123".equals(pass)) return Role.ADMIN;
        if (pass != null && pass.equals("123")) return Role.USER;
        return null;
    }

    // ---- janela de votação ----
    public synchronized void openForSeconds(int sec) {
        deadline = Instant.now().plusSeconds(Math.max(1, sec));
    }
    public synchronized void close() {
        deadline = Instant.now().minusSeconds(1);
    }
    public boolean isOpen() {
        return Instant.now().isBefore(deadline);
    }
    public long secondsLeft() {
        long s = Duration.between(Instant.now(), deadline).getSeconds();
        return Math.max(0, s);
    }

    // ---- candidatos ----
    public synchronized int addCandidate(String nome) {
        int nextId = candidatos.keySet().stream().mapToInt(i->i).max().orElse(0) + 1;
        candidatos.put(nextId, nome);
        votos.put(nextId, 0);
        return nextId;
    }
    public synchronized boolean removeCandidate(int id) {
        if (!candidatos.containsKey(id)) return false;
        candidatos.remove(id);
        votos.remove(id);
        return true;
    }
    public List<String> listAsJson() {
        List<String> list = new ArrayList<>();
        for (var e: candidatos.entrySet()) {
            list.add("{\"id\":" + e.getKey() + ",\"nome\":\"" + e.getValue() + "\"}");
        }
        return list;
    }

    // ---- voto ----
    public synchronized String vote(String user, int id) {
        if (!isOpen()) return "{\"ok\":false,\"err\":\"deadline\"}";
        if (!candidatos.containsKey(id)) return "{\"ok\":false,\"err\":\"not_found\"}";
        String key = user == null ? "" : user.toLowerCase();
        if (votantes.contains(key)) return "{\"ok\":false,\"err\":\"duplicado\"}";
        votos.computeIfPresent(id, (k, v) -> v + 1);
        votantes.add(key);
        return "{\"ok\":true}";
    }

    // ---- resultados ----
    public synchronized String resultsJson() {
        int total = votos.values().stream().mapToInt(Integer::intValue).sum();
        int vencedor = votos.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(-1);
        StringBuilder p = new StringBuilder("{");
        boolean first = true;
        for (var e: votos.entrySet()) {
            double pct = total == 0 ? 0.0 : (100.0 * e.getValue() / total);
            if (!first) p.append(",");
            p.append("\"").append(e.getKey()).append("\":")
                    .append(String.format(java.util.Locale.US, "%.2f", pct));
            first = false;
        }
        p.append("}");
        return "{\"ok\":true,\"total\":" + total + ",\"percents\":" + p + ",\"winnerId\":" + vencedor + "}";
    }
}
