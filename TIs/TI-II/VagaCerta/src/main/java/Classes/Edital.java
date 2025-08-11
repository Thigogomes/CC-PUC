package Classes;

import java.sql.Date;

public class Edital {
    private int id;
    private String arquivo;
    private Date data_publicacao;

    public Edital() {}

    public Edital(int id, String arquivo, Date data_publicacao) {
        this.id = id;
        this.arquivo = arquivo;
        this.data_publicacao = data_publicacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public Date getData_publicacao() {
        return data_publicacao;
    }

    public void setData_publicacao(Date data_publicacao) {
        this.data_publicacao = data_publicacao;
    }
}