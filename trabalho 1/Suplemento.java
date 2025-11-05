
public class Suplemento {
    private int id;
    private String nome;
    private String categoria;
    private int votos;
    
    public Suplemento(int id, String nome, String categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.votos = 0;
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public int getVotos() { return votos; }
    
    public void addcionarVoto() {
        this.votos++;
    }
    
    @Override
    public String toString() {
        return id + " - " + nome + " (" + categoria + ")";
    }
}
