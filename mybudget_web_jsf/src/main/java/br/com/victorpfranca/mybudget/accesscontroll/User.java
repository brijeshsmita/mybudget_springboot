package br.com.victorpfranca.mybudget.accesscontroll;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import br.com.victorpfranca.mybudget.LocalDateConverter;

/**
 * The persistent class for the user database table.
 * 
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String senha;

	private String firstName;

	private String lastName;

	private String email;

	private Boolean ativo;

	private Date dataCadastro;

	private Date dataUltimoAcesso;

	private BigDecimal quantidadeAcessos;

	private Boolean preCadastro;

	public User(String firstName, String lastName, String email, Boolean ativo, Date dataCadastro,
			Date dataUltimoAcesso, BigDecimal quantidadeAcessos, Boolean preCadastro) {
		
		this.firstName = firstName;
		this.lastName  = lastName;
		this.email = email;
		this.ativo = ativo;
		this.dataCadastro = dataCadastro;
		this.dataUltimoAcesso = dataUltimoAcesso;
		this.quantidadeAcessos = quantidadeAcessos;
		this.preCadastro = preCadastro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public Boolean getPreCadastro() {
		return preCadastro;
	}

	public void setPreCadastro(Boolean preCadastro) {
		this.preCadastro = preCadastro;
	}

	public LocalDate getDataCadastroLocalDate() {
		return LocalDateConverter.fromDate(dataCadastro);
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public BigDecimal getQuantidadeAcessos() {
		return quantidadeAcessos;
	}

	public void setQuantidadeAcessos(BigDecimal quantidadeAcessos) {
		this.quantidadeAcessos = quantidadeAcessos;
	}

	public void addContadorAcesso() {
		setQuantidadeAcessos(quantidadeAcessos.add(BigDecimal.ONE));
	}
}