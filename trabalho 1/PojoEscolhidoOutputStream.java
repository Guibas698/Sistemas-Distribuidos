import java.io.*;

class Suplemento {
    private String nome;
    private String marca;
    private double valor;

    
    public Suplemento(String nome, String marca, double valor) {
        this.nome = nome;
        this.marca = marca;
        this.valor = valor;
    }

    
    public String getNome() {
        return nome;
    }

    public String getMarca() {
        return marca;
    }

    public double getValor() {
        return valor;
    }
}

public class PojoEscolhidoOutputStream extends OutputStream {
    private Suplemento[] suplementos;
    private int numeroDeObjetos;
    private OutputStream destino;

    public PojoEscolhidoOutputStream(Suplemento[] suplementos, int numeroDeObjetos, OutputStream destino) {
        this.suplementos = suplementos;
        this.numeroDeObjetos = numeroDeObjetos;
        this.destino = destino;
    }

    @Override
    public void write(int b) throws IOException {
        // Não utilizado aqui, mas necessário para implementação de OutputStream.
    }

    public void escreverDados() throws IOException {
        for (Suplemento suplemento : suplementos) {
            // Converte os atributos para bytes
            byte[] nomeBytes = suplemento.getNome().getBytes();
            byte[] marcaBytes = suplemento.getMarca().getBytes();
            byte[] valorBytes = doubleToBytes(suplemento.getValor());

            // Converte os bytes de volta para String para exibição
            System.out.println("Nome: " + new String(nomeBytes));
            System.out.println("Marca: " + new String(marcaBytes));
            System.out.println("Valor: " + suplemento.getValor());
            
            // Escreve os bytes para o OutputStream (destino)
            destino.write(nomeBytes.length);
            destino.write(nomeBytes);
            destino.write(marcaBytes.length);
            destino.write(marcaBytes);
            destino.write(valorBytes.length);
            destino.write(valorBytes);
        }
    }

    // Helper method to convert double to byte array
    private byte[] doubleToBytes(double value) {
        long longBits = Double.doubleToLongBits(value);
        return new byte[] {
            (byte) (longBits >> 56),
            (byte) (longBits >> 48),
            (byte) (longBits >> 40),
            (byte) (longBits >> 32),
            (byte) (longBits >> 24),
            (byte) (longBits >> 16),
            (byte) (longBits >> 8),
            (byte) longBits
        };
    }
}

class Main {
    public static void main(String[] args) throws IOException {
        // Criação de objetos Suplemento
        Suplemento[] suplementos = {
            new Suplemento("Whey Protein", "Marca X", 120.40),
            new Suplemento("Creatina", "Marca Y", 60.50)
        };

        // Criação do PojoEscolhidoOutputStream
        PojoEscolhidoOutputStream outputStream = new PojoEscolhidoOutputStream(suplementos, suplementos.length, System.out);

        // Escrever dados para o destino (neste caso, a saída padrão)
        outputStream.escreverDados();
    }
}
