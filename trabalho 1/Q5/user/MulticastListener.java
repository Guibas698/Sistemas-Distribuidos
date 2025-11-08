// user/MulticastListener.java
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MulticastListener implements Runnable {
    public static final String GROUP = "230.0.0.1";
    public static final int PORT = 4446;
    private volatile boolean running = true;

    public void stop() { running = false; }

    @Override
    public void run() {
        try (MulticastSocket ms = new MulticastSocket(PORT)) {
            ms.joinGroup(InetAddress.getByName(GROUP));
            byte[] buf = new byte[2048];
            while (running) {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                ms.receive(p);
                String msg = new String(p.getData(), 0, p.getLength(), StandardCharsets.UTF_8);
                System.out.println("[NOTA] " + msg);
            }
        } catch (Exception e) {
            System.err.println("Falha multicast: " + e.getMessage());
        }
    }
}
