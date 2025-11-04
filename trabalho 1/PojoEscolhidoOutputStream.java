import java.io.*;
import java.nio.charset.StandardCharsets;

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
        // Delegar para o destino para suportar escrita por byte quando necessário
        destino.write(b);
    }

    public void escreverDados() throws IOException {
        for (Suplemento suplemento : suplementos) {
            // Converte os atributos para bytes
            byte[] nomeBytes = suplemento.getNome().getBytes(StandardCharsets.UTF_8);
            byte[] marcaBytes = suplemento.getMarca().getBytes(StandardCharsets.UTF_8);
            byte[] valorBytes = doubleToBytes(suplemento.getValor());

            // Converte os bytes de volta para String para exibição
            System.out.println("Nome: " + new String(nomeBytes));
            System.out.println("Marca: " + new String(marcaBytes));
            System.out.println("Valor: " + suplemento.getValor());
            
            // Escreve os bytes para o OutputStream (destino)
            // comprimento do nome e marca são gravados em um único byte (0..255)
            destino.write(nomeBytes.length & 0xFF);
            destino.write(nomeBytes);
            destino.write(marcaBytes.length & 0xFF);
            destino.write(marcaBytes);
            // Não gravamos o comprimento do valor (double) — sempre 8 bytes — para manter o protocolo simples
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
