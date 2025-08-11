package Classes;

public class Usuario {
    private String cpf;
    private String nome;
    private String escolaridade;
    private String senha;
    private String email;
    
    public Usuario() {
        this.cpf = "";
        this.nome     = "";
        this.escolaridade    = "";
        this.senha   = "";
        this.email  = "";
     }
    
    public Usuario(String cpf, String nome, String escolaridade, String senha, String email) {
        this.cpf = cpf;
        this.nome     = nome;
        this.escolaridade    = escolaridade;
        this.senha   = senha;
        this.email  = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Usuario [cpf=" + cpf 
             + ", nome="   + nome 
             + ", escolaridade="  + escolaridade 
             + ", senha=" + senha 
             + ", email="+ email 
             + "]";
    }
}
