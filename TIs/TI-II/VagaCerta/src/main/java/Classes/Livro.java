package Classes;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private int versao;
    private String materia;
    private String link;

    public Livro() {}

    public Livro(int id, String titulo, String autor, int versao, String materia, String link) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.versao = versao;
        this.materia = materia;
        this.link = link;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Livro{" +
               "id=" + id +
               ", titulo='" + titulo + '\'' +
               ", autor='" + autor + '\'' +
               ", versao=" + versao +
               ", materia='" + materia + '\'' +
               ", link='" + link + '\'' +
               '}';
    }
}
