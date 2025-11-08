// server/MulticastNotifier.java
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MulticastNotifier {
    public static final String GROUP = "230.0.0.1";
    public static final int PORT = 4446;

    public static void sendInfo(String text) {
        String json = "{\"type\":\"INFO\",\"text\":\"" + text.replace("\"","'") + "\"}";
        byte[] buf = json.getBytes(StandardCharsets.UTF_8);
        try (DatagramSocket ds = new DatagramSocket()) {
            DatagramPacket pkt = new DatagramPacket(buf, buf.length, InetAddress.getByName(GROUP), PORT);
            ds.send(pkt);
        } catch (Exception e) {
            System.err.println("Falha ao enviar nota UDP: " + e.getMessage());
        }
    }
}
