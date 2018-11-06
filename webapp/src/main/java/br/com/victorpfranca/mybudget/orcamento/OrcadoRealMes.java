package br.com.victorpfranca.mybudget.orcamento;

import static br.com.victorpfranca.mybudget.orcamento.OrcadoRealMes.FIND_ALL;
import static br.com.victorpfranca.mybudget.orcamento.OrcadoRealMes.FIND_BY_DESPESA_MONTH;
import static br.com.victorpfranca.mybudget.orcamento.OrcadoRealMes.FIND_BY_DESPESA_UNTIL_MONTH;
import static br.com.victorpfranca.mybudget.orcamento.OrcadoRealMes.FIND_BY_RECEITA_MONTH;
import static br.com.victorpfranca.mybudget.orcamento.OrcadoRealMes.FIND_BY_RECEITA_UNTIL_MONTH;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.InOutConverter;
import br.com.victorpfranca.mybudget.accesscontroll.User;

@Entity
@Table(name = "vw_orcado_real_mes")
@NamedQueries({ @NamedQuery(name = FIND_ALL, query = "SELECT o FROM OrcadoRealMes o WHERE o.usuario.id = :user"),
		@NamedQuery(name = FIND_BY_DESPESA_MONTH, query = "SELECT o FROM OrcadoRealMes o where o.usuario.id = :user and ano = :ano and mes = :mes and inOut = '1'"),
		@NamedQuery(name = FIND_BY_RECEITA_MONTH, query = "SELECT o FROM OrcadoRealMes o where o.usuario.id = :user and ano = :ano and mes = :mes and inOut = '0'"),

		@NamedQuery(name = FIND_BY_DESPESA_UNTIL_MONTH, query = "SELECT o FROM OrcadoRealMes o where o.usuario.id = :user AND CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) <= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) and inOut = '1' ORDER BY ano DESC, mes DESC"),
		@NamedQuery(name = FIND_BY_RECEITA_UNTIL_MONTH, query = "SELECT o FROM OrcadoRealMes o where o.usuario.id = :user AND CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) <= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) and inOut = '0' ORDER BY ano DESC, mes DESC") })

public class OrcadoRealMes implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "OrcadoRealMes.findAll";
	public static final String FIND_BY_DESPESA_MONTH = "OrcadoRealMes.findByDespesaMonth";
	public static final String FIND_BY_RECEITA_MONTH = "OrcadoRealMes.findByReceitaMonth";

	public static final String FIND_BY_DESPESA_UNTIL_MONTH = "OrcadoRealMes.findByDespesaUntilMonth";
	public static final String FIND_BY_RECEITA_UNTIL_MONTH = "OrcadoRealMes.findByReceitaUntilMonth";

	@Id
	@NotNull
	@Convert(converter = InOutConverter.class)
	@Column(name = "in_out", nullable = false, unique = false)
	private InOut inOut;

	@Id
	@NotNull
	@Column(name = "ano", nullable = false, unique = false)
	private Integer ano;

	@Id
	@NotNull
	@Column(name = "mes", nullable = false, unique = false)
	private Integer mes;

	@NotNull
	@Column(name = "saldo", nullable = false, unique = false)
	private BigDecimal saldo;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public InOut getInOut() {
		return inOut;
	}

	public void setInOut(InOut inOut) {
		this.inOut = inOut;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public int compareDate(int ano, int mes) {
		LocalDate date = LocalDate.of(this.ano, this.mes, 1);
		return date.compareTo(LocalDate.of(ano, mes, 1));
	}

}