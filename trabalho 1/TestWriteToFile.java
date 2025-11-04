import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestWriteToFile {
    public static void main(String[] args) throws IOException {
        Suplemento[] suplementos = {
            new Suplemento("Whey Protein", "Marca X", 250.50),
            new Suplemento("Creatina", "Marca Y", 120.75)
        };

    String filename = "suplementos.txt";

        // Escrever para arquivo
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            PojoEscolhidoOutputStream out = new PojoEscolhidoOutputStream(suplementos, suplementos.length, fos);
            System.out.println("Escrevendo dados em: " + filename);
            out.escreverDados();
            System.out.println("Escrita concluída.");
        }

        // Ler de arquivo e validar
        try (FileInputStream fis = new FileInputStream(filename)) {
            PojoEscolhidoInputStream in = new PojoEscolhidoInputStream(fis, suplementos.length);
            System.out.println("Lendo dados de: " + filename);
            in.lerDados();
            System.out.println("Leitura concluída.");
        }
    }
}
