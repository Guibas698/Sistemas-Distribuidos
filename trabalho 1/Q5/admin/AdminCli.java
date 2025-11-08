// admin/AdminCli.java
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AdminCli {
    public static void main(String[] args) throws Exception {
        System.out.println("Admin CLI — conectando em localhost:7070");
        try (Socket s = new Socket("127.0.0.1", 7070)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));

            // login admin
            bw.write("{\"op\":\"LOGIN\",\"user\":\"admin\",\"pass\":\"admin123\"}\n"); bw.flush();
            System.out.println("LOGIN -> " + br.readLine());

            System.out.println("Comandos: open <sec> | close | add <nome> | remove <id> | announce <texto> | list | results | quit");
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.print("> ");
                if (!sc.hasNextLine()) break;
                String line = sc.nextLine().trim();
                if (line.equalsIgnoreCase("quit")) break;
                if (line.startsWith("open ")) {
                    int sec = Integer.parseInt(line.substring(5).trim());
                    bw.write("{\"op\":\"OPEN\",\"durationSec\":" + sec + "}\n"); bw.flush();
                    System.out.println(br.readLine());
                    continue;
                }
                if (line.equals("close")) {
                    bw.write("{\"op\":\"CLOSE\"}\n"); bw.flush();
                    System.out.println(br.readLine());
                    continue;
                }
                if (line.startsWith("add ")) {
                    String nome = line.substring(4).trim().replace("\"","'");
                    bw.write("{\"op\":\"ADD_CANDIDATE\",\"nome\":\"" + nome + "\"}\n"); bw.flush();
                    System.out.println(br.readLine());
                    continue;
                }
                if (line.startsWith("remove ")) {
                    int id = Integer.parseInt(line.substring(7).trim());
                    bw.write("{\"op\":\"REMOVE_CANDIDATE\",\"suplementoId\":" + id + "}\n"); bw.flush();
                    System.out.println(br.readLine());
                    continue;
                }
                if (line.startsWith("announce ")) {
                    String msg = line.substring(9).trim();
                    MulticastNotifier.sendInfo(msg);
                    System.out.println("{\"ok\":true}");
                    continue;
                }
                if (line.equals("list")) {
                    bw.write("{\"op\":\"LIST\"}\n"); bw.flush();
                    System.out.println(br.readLine());
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
