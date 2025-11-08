import java.io.*;
import java.io.*;

public class PojoEscolhidoInputStream extends InputStream {
    private InputStream origem;
    private Suplemento[] suplementos;
    private int numeroDeObjetos;
    private int indexAtual;

    public PojoEscolhidoInputStream(InputStream origem, int numeroDeObjetos) {
        this.origem = origem;
        this.numeroDeObjetos = numeroDeObjetos;
        this.suplementos = new Suplemento[numeroDeObjetos];
        this.indexAtual = 0;
    }

    @Override
    public int read() throws IOException {
        return origem.read();  // Para leitura byte a byte, mas não será usado diretamente aqui
    }

    public void lerDados() throws IOException {
        for (int i = 0; i < numeroDeObjetos; i++) {
            // Lê o nome
            int nomeLength = origem.read();  // Lê o comprimento do nome (1 byte)
            byte[] nomeBytes = new byte[nomeLength];
            origem.read(nomeBytes);  // Lê o nome

            // Lê a marca
            int marcaLength = origem.read();  // Lê o comprimento da marca (1 byte)
            byte[] marcaBytes = new byte[marcaLength];
            origem.read(marcaBytes);  // Lê a marca

            // Lê o valor (double)
            byte[] valorBytes = new byte[8];  // 8 bytes para armazenar um valor double
            origem.read(valorBytes);  // Lê o valor

            // Reconstruir o objeto Suplemento
            String nome = new String(nomeBytes);
            String marca = new String(marcaBytes);
            double valor = bytesToDouble(valorBytes);

            // Armazena no array
            suplementos[i] = new Suplemento(nome, marca, valor);
        }

        // Exibir dados lidos
        for (Suplemento suplemento : suplementos) {
            System.out.println("Nome: " + suplemento.getNome());
            System.out.println("Marca: " + suplemento.getMarca());
            System.out.println("Valor: " + suplemento.getValor());
        }
    }

    // Helper method to convert byte array to double
    private double bytesToDouble(byte[] bytes) {
        long longBits = 0;
        for (int i = 0; i < 8; i++) {
            longBits |= (long) (bytes[i] & 0xFF) << (56 - (i * 8));
        }
        return Double.longBitsToDouble(longBits);
    }
}

// Usaremos a classe `Suplemento` definida em Suplemento.java

class Main {
    public static void main(String[] args) throws IOException {
        // Suplementos a serem testados
        Suplemento[] suplementos = {
            new Suplemento("Whey Protein", "Marca X", 250.50),
            new Suplemento("Creatina", "Marca Y", 120.75)
        };

        // Criando o OutputStream (escrever os dados)
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        PojoEscolhidoOutputStream outputStream = new PojoEscolhidoOutputStream(suplementos, suplementos.length, byteArrayOut);
        outputStream.escreverDados();

        // Criando o InputStream (ler os dados)
        ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(byteArrayOut.toByteArray());
        PojoEscolhidoInputStream inputStream = new PojoEscolhidoInputStream(byteArrayIn, suplementos.length);

        // Lê e exibe os dados lidos
        inputStream.lerDados();
    }
}

