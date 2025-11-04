
public class Suplemento {
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