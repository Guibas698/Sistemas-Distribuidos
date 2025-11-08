// common/Suplemento.java
public class Suplemento {
    private final int id;
    private final String nome;

    public Suplemento(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    public int getId() { return id; }
    public String getNome() { return nome; }
    @Override public String toString() { return id + ":" + nome; }
}
