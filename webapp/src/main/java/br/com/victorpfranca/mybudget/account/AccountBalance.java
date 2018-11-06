package br.com.victorpfranca.mybudget.account;

import static br.com.victorpfranca.mybudget.account.AccountBalance.FIND_ALL_QUERY;
import static br.com.victorpfranca.mybudget.account.AccountBalance.FIND_FROM_ANO_MES_GROUPED_QUERY;
import static br.com.victorpfranca.mybudget.account.AccountBalance.FIND_FROM_ANO_MES_QUERY;
import static br.com.victorpfranca.mybudget.account.AccountBalance.FIND_UNTIL_ANO_MES_GROUPED_QUERY;
import static br.com.victorpfranca.mybudget.account.AccountBalance.FIND_UNTIL_ANO_MES_QUERY;
import static br.com.victorpfranca.mybudget.account.AccountBalance.REMOVE_SALDOS_INICIAIS_QUERY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoFaturaCartaoItem;

@Entity
@Table(name = "conta_saldo")
@NamedQueries({ @NamedQuery(name = FIND_ALL_QUERY, query = "SELECT s FROM AccountBalance s WHERE s.usuario.id = :user"),

		@NamedQuery(name = FIND_UNTIL_ANO_MES_GROUPED_QUERY, query = "SELECT new br.com.victorpfranca.mybudget.account.AccountBalance(s.ano, s.mes, SUM(valor)) FROM AccountBalance s where (:user is null OR user = :user) and (:account is null OR account = :account) and CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) <= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) group by ano, mes order by ano DESC, mes DESC"),
		@NamedQuery(name = FIND_FROM_ANO_MES_GROUPED_QUERY, query = "SELECT new br.com.victorpfranca.mybudget.account.AccountBalance(s.ano, s.mes, SUM(valor)) FROM AccountBalance s where (:user is null OR user = :user) and (:account is null OR account = :account) and CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) >= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) group by ano, mes order by ano ASC, mes ASC"),

		@NamedQuery(name = FIND_UNTIL_ANO_MES_QUERY, query = "SELECT s FROM AccountBalance s where (:user is null OR user = :user) and (:account is null OR account = :account) and CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) <= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) order by ano DESC, mes DESC"),
		@NamedQuery(name = FIND_FROM_ANO_MES_QUERY, query = "SELECT s FROM AccountBalance s where (:user is null OR user = :user) and (:account is null OR account = :account) and CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) >= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) order by ano ASC, mes ASC"),
		@NamedQuery(name = REMOVE_SALDOS_INICIAIS_QUERY, query = "DELETE FROM AccountBalance l WHERE account = :account") })
public class AccountBalance implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorSaldoConta";

	public static final String FIND_ALL_QUERY = "AccountBalance.findAll";

	public static final String FIND_UNTIL_ANO_MES_GROUPED_QUERY = "AccountBalance.findUntilAnoMesGrouped";
	public static final String FIND_FROM_ANO_MES_GROUPED_QUERY = "AccountBalance.findFromAnoMesGrouped";

	public static final String FIND_UNTIL_ANO_MES_QUERY = "AccountBalance.findUntilAnoMes";
	public static final String FIND_FROM_ANO_MES_QUERY = "AccountBalance.findFromAnoMes";

	public static final String REMOVE_SALDOS_INICIAIS_QUERY = "AccountBalance.removeSaldosIniciais";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_conta_saldo", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "conta_id")
	private Account account;

	@Column(name = "valor", nullable = false, unique = false)
	private BigDecimal valor;

	@NotNull
	@Column(name = "ano", nullable = false, unique = false)
	private Integer ano;

	@NotNull
	@Column(name = "mes", nullable = false, unique = false)
	private Integer mes;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public AccountBalance() {
		this.valor = BigDecimal.ZERO;
	}

	public AccountBalance(Account account, Integer ano, Integer mes, BigDecimal valor) {
		this.account = account;
		this.user = account.getUsuario();
		this.ano = ano;
		this.mes = mes;
		this.valor = valor;
	}

	public AccountBalance(int ano, int mes, BigDecimal valor) {
		this.ano = ano;
		this.mes = mes;
		this.valor = valor;
	}

	public void add(Lancamento lancamento) {
		BigDecimal valorAtualLancamentoComSinal = lancamento.getValor();

		BigDecimal valorAnteriorLancamentoComSinal = lancamento.getValorAnterior() != null
				? lancamento.getValorAnterior()
				: BigDecimal.ZERO;

		if (lancamento.getInOut().equals(InOut.S)) {
			valorAtualLancamentoComSinal = valorAtualLancamentoComSinal.negate();
			valorAnteriorLancamentoComSinal = valorAnteriorLancamentoComSinal.negate();
		}

		setValor(getValor().subtract(valorAnteriorLancamentoComSinal).add(valorAtualLancamentoComSinal));
	}

	public void remove(Lancamento lancamento) {
		BigDecimal valorAtualLancamentoComSinal = lancamento.getValor();
		if (lancamento.getInOut().equals(InOut.S) || (lancamento instanceof LancamentoFaturaCartaoItem)) {
			valorAtualLancamentoComSinal = valorAtualLancamentoComSinal.negate();
		}
		setValor(getValor().subtract(valorAtualLancamentoComSinal));
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		return mes;
	}

	public LocalDate getLocalDate() {
		return LocalDate.of(this.ano, this.mes, 1);
	}
	
	public Date getDate() {
		return LocalDateConverter.toDate(getLocalDate());
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	public AccountBalance withConta(Account account) {
		setAccount(account);
		return this;
	}

	public AccountBalance withAno(int ano) {
		setAno(ano);
		return this;
	}

	public AccountBalance withMes(int mes) {
		setMes(mes);
		return this;
	}

	public AccountBalance withValor(BigDecimal valor) {
		setValor(valor);
		return this;
	}

	@PrePersist
	void beforePersist() {
		try {
			if (user == null)
				setUsuario(((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
						.recuperarUsuarioLogado());
		} catch (NamingException e) {
			throw new RuntimeException(e.getExplanation());
		}
	}

	public int compareDate(int ano, int mes) {
		LocalDate date = LocalDate.of(this.ano, this.mes, 1);
		return date.compareTo(LocalDate.of(ano, mes, 1));
	}

}