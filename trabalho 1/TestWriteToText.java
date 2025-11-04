import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestWriteToText {
    public static void main(String[] args) throws IOException {
        Suplemento[] suplementos = {
            new Suplemento("Whey Protein", "Marca X", 250.50),
            new Suplemento("Creatina", "Marca Y", 120.75)
        };

        String filename = "suplementos.txt";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Escrever JSON legível
        try (FileWriter fw = new FileWriter(filename)) {
            gson.toJson(suplementos, fw);
            System.out.println("Escreveu JSON legível em: " + filename);
        }

        // Ler e desserializar
        try (FileReader fr = new FileReader(filename)) {
            Suplemento[] lidos = gson.fromJson(fr, Suplemento[].class);
            System.out.println("Dados lidos do arquivo texto:");
            for (Suplemento s : lidos) {
                System.out.println("Nome: " + s.getNome());
                System.out.println("Marca: " + s.getMarca());
                System.out.println("Valor: " + s.getValor());
                System.out.println();
            }
        }
    }
}
