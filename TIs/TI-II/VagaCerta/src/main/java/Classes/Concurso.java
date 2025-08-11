package Classes;

import java.util.*;

public class Concurso {
	private int ID;
	private String nome;
	private String escolaridade;
	private String localizacao;
	private String categoria;
	private String banca;
	private String descricao;
	private String orgao;
	private String cargo;
	private int editalID;
	private String materiaisDeEstudo;
	private String horario;
	private String status;
	private Date inicioIncricoes;
	private Date terminoIncricoes;
	
	public Concurso() {
		this.ID = -1;
		this.nome = "";
		this.escolaridade = "";
		this.localizacao = "";
		this.categoria = "";
		this.banca = "";
		this.descricao = "";
		this.orgao = "";
		this.cargo = "";
		this.editalID = -1;
		this.materiaisDeEstudo = "";
		this.horario = "";
		this.status = "";
		this.inicioIncricoes = null;
		this.terminoIncricoes = null;
	}
	
	public Concurso(int ID, String nome, String escolaridade, String localizacao, String categoria, String banca, String descricao, String orgao, String cargo, String materiaisDeEstudo, String horario, String status, Date inicioIncricoes, Date terminoIncricoes) {
		this.ID = ID;
		this.nome = nome;
		this.escolaridade = escolaridade;
		this.localizacao = localizacao;
		this.categoria = categoria;
		this.banca = banca;
		this.descricao = descricao;
		this.orgao = orgao;
		this.cargo = cargo;
		//this.editalID = editalID;
		this.materiaisDeEstudo = materiaisDeEstudo;
		this.horario = horario;
		this.status = status;
		this.inicioIncricoes = inicioIncricoes;
		this.terminoIncricoes = terminoIncricoes;
	}
	
	public int getID() {
	    return ID;
	}

	public void setID(int ID) {
	    this.ID = ID;
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

	public String getLocalizacao() {
	    return localizacao;
	}

	public void setLocalizacao(String localizacao) {
	    this.localizacao = localizacao;
	}

	public String getCategoria() {
	    return categoria;
	}

	public void setCategoria(String categoria) {
	    this.categoria = categoria;
	}

	public String getBanca() {
	    return banca;
	}

	public void setBanca(String banca) {
	    this.banca = banca;
	}

	public String getDescricao() {
	    return descricao;
	}

	public void setDescricao(String descricao) {
	    this.descricao = descricao;
	}

	public String getOrgao() {
	    return orgao;
	}

	public void setOrgao(String orgao) {
	    this.orgao = orgao;
	}

	public String getCargo() {
	    return cargo;
	}

	public void setCargo(String cargo) {
	    this.cargo = cargo;
	}

	// public int getEditalID() {
	//     return editalID;
	// }

	public void setEditalID(int editalID) {
	    this.editalID = editalID;
	}

	public String getMateriaisDeEstudo() {
	    return materiaisDeEstudo;
	}

	public void setMateriaisDeEstudo(String materiaisDeEstudo) {
	    this.materiaisDeEstudo = materiaisDeEstudo;
	}

	public String getHorario() {
	    return horario;
	}

	public void setHorario(String horario) {
	    this.horario = horario;
	}

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}

	public Date getInicioIncricoes() {
	    return inicioIncricoes;
	}

	public void setInicioIncricoes(Date inicioIncricoes) {
	    this.inicioIncricoes = inicioIncricoes;
	}

	public Date getTerminoIncricoes() {
	    return terminoIncricoes;
	}

	public void setTerminoIncricoes(Date terminoIncricoes) {
	    this.terminoIncricoes = terminoIncricoes;
	}
	
	@Override
	public String toString() {
	    return "Concurso {" +
	           "ID=" + ID +
	           ", nome='" + nome + '\'' +
	           ", escolaridade='" + escolaridade + '\'' +
	           ", localizacao='" + localizacao + '\'' +
	           ", categoria='" + categoria + '\'' +
	           ", banca='" + banca + '\'' +
	           ", descricao='" + descricao + '\'' +
	           ", orgao='" + orgao + '\'' +
	           ", cargo='" + cargo + '\'' +
	           ", linkEdital='" + editalID + '\'' +
	           ", materiaisDeEstudo='" + materiaisDeEstudo+ '\'' +
	           ", horario='" + horario + '\'' +
	           ", status=" + (status.length() > 0 ? "Ativo" : "Inativo") +
	           ", inicioIncricoes=" + inicioIncricoes +
	           ", terminoIncricoes=" + terminoIncricoes +
	           '}';
	}
}