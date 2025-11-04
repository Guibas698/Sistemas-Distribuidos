import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReadBinaryToText {
    public static void main(String[] args) {
        String input = "suplementos.txt";
        String output = "suplementos_readable.json";
        try (FileInputStream fis = new FileInputStream(input)) {
            PojoEscolhidoInputStream pin = new PojoEscolhidoInputStream(fis);
            Suplemento[] arr = pin.lerDados();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter fw = new FileWriter(output)) {
                gson.toJson(arr, fw);
            }
            System.out.println("Arquivo convertido para JSON legível: " + output);
            for (Suplemento s : arr) {
                System.out.println("Nome: " + s.getNome());
                System.out.println("Marca: " + s.getMarca());
                System.out.println("Valor: " + s.getValor());
                System.out.println();
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler/converter: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
