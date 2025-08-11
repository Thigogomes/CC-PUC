package Classes;

public class Admin {
    private int id;
    private String user;
    private String password;
    private String nome;

    public Admin() {
        this.id = 0;
        this.user = "";
        this.password = "";
        this.nome = "";
    }

    public Admin(int id, String user, String password, String nome) {
        this.id = id;
        this.user = user;
        this.password = password;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Admin [id=" + id + ", user=" + user + ", nome=" + nome + ", password=" + password + "]";
    }
}
