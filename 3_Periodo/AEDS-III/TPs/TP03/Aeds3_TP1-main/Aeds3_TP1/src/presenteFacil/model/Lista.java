package src.presenteFacil.model;

import java.io.*;
import java.time.LocalDate;

public class Lista implements Registro {
    private int id;
    private String codigo; 
    private String nome;
    private String descricao;
    private LocalDate dataCriacao;
    private LocalDate dataLimite;
    private int idUsuario; //Chave Estrangeira
    private boolean ativa;

    public Lista(){
        this.id = -1;
        this.codigo = "";
        this.nome = "";
        this.descricao = "";
        this.dataCriacao = LocalDate.now();
        this.dataLimite = LocalDate.now();
        this.idUsuario = -1;
        this.ativa = true;
    }

    public Lista(String codigo, String nome, String descricao, LocalDate dataCriacao, LocalDate dataLimite, int idUsuario){
        this.id = -1;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataLimite = dataLimite;
        this.idUsuario = idUsuario;
        this.ativa = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public boolean isAtiva() {
        return ativa;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public byte[] toByteArray() throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.codigo);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeInt((int)this.dataCriacao.toEpochDay());
        dos.writeInt((int)this.dataLimite.toEpochDay());
        dos.writeInt(this.idUsuario);
        dos.writeBoolean(this.ativa);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.codigo = dis.readUTF();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.dataCriacao = LocalDate.ofEpochDay(dis.readInt());
        this.dataLimite = LocalDate.ofEpochDay(dis.readInt());
        this.idUsuario = dis.readInt();
        this.ativa = dis.readBoolean();
    }

    @Override
    public String toString() {
        return 
            "\nID................: " + this.id +
            "\nCódigo............: " + this.codigo +
            "\nNome..............: " + this.nome+ 
            "\nDescrição.........: " + this.descricao +
            "\nData de criação...: " + this.dataCriacao +
            "\nData de limite....: " + this.dataLimite +
            "\nID Usuario........: " + this.idUsuario +
            "\nAtiva.............: " + this.ativa
        ;
    }
}