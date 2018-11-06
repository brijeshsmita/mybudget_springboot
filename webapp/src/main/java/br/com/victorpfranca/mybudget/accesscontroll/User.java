package br.com.victorpfranca.mybudget.accesscontroll;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import br.com.victorpfranca.mybudget.LocalDateConverter;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String GENERATOR = "GeneratorUsuario";
	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_usuario", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Size(min = 1, max = 70)
	@Column(name = "password")
	private String senha;

	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "first_name")
	private String firstName;

	@Size(min = 0, max = 20)
	@Column(name = "last_name")
	private String lastName;

	@NotNull
	@Size(min = 1, max = 70)
	@Column(name = "email")
	private String email;

	@Column(name = "ativo")
	private Boolean ativo;

	@Column(name = "data_cadastro", nullable = false, unique = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	@Column(name = "data_ultimo_acesso", nullable = true, unique = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimoAcesso;

	@Column(name = "quantidade_acessos", nullable = false, unique = false)
	private BigDecimal quantidadeAcessos;
	
	@Column(name = "pre_cadastro")
	private Boolean preCadastro;

	@PrePersist
	void beforePersist() {
	    setEmail(StringUtils.lowerCase(getEmail()));
	}
	@PreUpdate
	void beforeUpdate() {
	    setEmail(StringUtils.lowerCase(getEmail()));
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