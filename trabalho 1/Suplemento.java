
public class Suplemento {
    // Campos que cobrem os usos diferentes no repositório
    private int id; // opcional, usado por algumas classes
    private String nome;
    private String categoria; // usada por algumas classes
    private String marca; // usada por outras classes
    private double valor; // usado por outras classes (preço)
    private int votos;

    // Construtor original (id, nome, categoria)
    public Suplemento(int id, String nome, String categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.marca = null;
        this.valor = 0.0;
        this.votos = 0;
    }

    // Construtor compatível com PojoEscolhido* (nome, marca, valor)
    public Suplemento(String nome, String marca, double valor) {
        this.id = 0;
        this.nome = nome;
        this.categoria = null;
        this.marca = marca;
        this.valor = valor;
        this.votos = 0;
    }

    // Getters compatíveis com diferentes convenções no código
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public int getVotos() { return votos; }

    // For backward/forward compatibility
    public String getMarca() { return (marca != null) ? marca : categoria; }
    public double getValor() { return valor; }

    public void addcionarVoto() { this.votos++; }

    @Override
    public String toString() {
        if (marca != null) {
            return nome + " - " + marca + " (valor=" + valor + ")";
        } else {
            return id + " - " + nome + " (" + categoria + ")";
        }
    }
}
