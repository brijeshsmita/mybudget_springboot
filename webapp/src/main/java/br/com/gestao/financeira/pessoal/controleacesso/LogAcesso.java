package br.com.gestao.financeira.pessoal.controleacesso;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "log_acesso")
public class LogAcesso implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String GENERATOR = "GeneratorLogAcesso";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_log_acesso", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	@Column(name = "data", nullable = false, unique = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	public LogAcesso() {
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
