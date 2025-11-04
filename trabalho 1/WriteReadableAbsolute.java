import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WriteReadableAbsolute {
    public static void main(String[] args) {
        String filename = "C:\\Users\\games\\OneDrive\\Documentos\\sistemas distruibuidos\\trabalhos sd\\trabalho 1\\suplementos.txt";
        Suplemento[] suplementos = {
            new Suplemento("Whey Protein", "Marca X", 250.50),
            new Suplemento("Creatina", "Marca Y", 120.75)
        };
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(filename)) {
            gson.toJson(suplementos, fw);
            System.out.println("Sobrescreveu (texto) : " + filename);
        } catch (IOException e) {
            System.err.println("Erro ao escrever: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
