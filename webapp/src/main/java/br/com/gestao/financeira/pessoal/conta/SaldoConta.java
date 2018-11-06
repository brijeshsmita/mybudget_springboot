package br.com.gestao.financeira.pessoal.conta;

import static br.com.gestao.financeira.pessoal.conta.SaldoConta.FIND_ALL_QUERY;
import static br.com.gestao.financeira.pessoal.conta.SaldoConta.FIND_FROM_ANO_MES_GROUPED_QUERY;
import static br.com.gestao.financeira.pessoal.conta.SaldoConta.FIND_FROM_ANO_MES_QUERY;
import static br.com.gestao.financeira.pessoal.conta.SaldoConta.FIND_UNTIL_ANO_MES_GROUPED_QUERY;
import static br.com.gestao.financeira.pessoal.conta.SaldoConta.FIND_UNTIL_ANO_MES_QUERY;
import static br.com.gestao.financeira.pessoal.conta.SaldoConta.REMOVE_SALDOS_INICIAIS_QUERY;

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

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoFaturaCartaoItem;

@Entity
@Table(name = "conta_saldo")
@NamedQueries({ @NamedQuery(name = FIND_ALL_QUERY, query = "SELECT s FROM SaldoConta s WHERE s.usuario.id = :usuario"),

		@NamedQuery(name = FIND_UNTIL_ANO_MES_GROUPED_QUERY, query = "SELECT new br.com.gestao.financeira.pessoal.conta.SaldoConta(s.ano, s.mes, SUM(valor)) FROM SaldoConta s where (:usuario is null OR usuario = :usuario) and (:conta is null OR conta = :conta) and CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) <= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) group by ano, mes order by ano DESC, mes DESC"),
		@NamedQuery(name = FIND_FROM_ANO_MES_GROUPED_QUERY, query = "SELECT new br.com.gestao.financeira.pessoal.conta.SaldoConta(s.ano, s.mes, SUM(valor)) FROM SaldoConta s where (:usuario is null OR usuario = :usuario) and (:conta is null OR conta = :conta) and CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) >= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) group by ano, mes order by ano ASC, mes ASC"),

		@NamedQuery(name = FIND_UNTIL_ANO_MES_QUERY, query = "SELECT s FROM SaldoConta s where (:usuario is null OR usuario = :usuario) and (:conta is null OR conta = :conta) and CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) <= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) order by ano DESC, mes DESC"),
		@NamedQuery(name = FIND_FROM_ANO_MES_QUERY, query = "SELECT s FROM SaldoConta s where (:usuario is null OR usuario = :usuario) and (:conta is null OR conta = :conta) and CONCAT(to_char(ano, 'FM9999'),to_char(mes, 'FM09')) >= CONCAT(to_char(:ano, 'FM9999'),to_char(:mes, 'FM09')) order by ano ASC, mes ASC"),
		@NamedQuery(name = REMOVE_SALDOS_INICIAIS_QUERY, query = "DELETE FROM SaldoConta l WHERE conta = :conta") })
public class SaldoConta implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorSaldoConta";

	public static final String FIND_ALL_QUERY = "SaldoConta.findAll";

	public static final String FIND_UNTIL_ANO_MES_GROUPED_QUERY = "SaldoConta.findUntilAnoMesGrouped";
	public static final String FIND_FROM_ANO_MES_GROUPED_QUERY = "SaldoConta.findFromAnoMesGrouped";

	public static final String FIND_UNTIL_ANO_MES_QUERY = "SaldoConta.findUntilAnoMes";
	public static final String FIND_FROM_ANO_MES_QUERY = "SaldoConta.findFromAnoMes";

	public static final String REMOVE_SALDOS_INICIAIS_QUERY = "SaldoConta.removeSaldosIniciais";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_conta_saldo", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "conta_id")
	private Conta conta;

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
	private Usuario usuario;

	public SaldoConta() {
		this.valor = BigDecimal.ZERO;
	}

	public SaldoConta(Conta conta, Integer ano, Integer mes, BigDecimal valor) {
		this.conta = conta;
		this.usuario = conta.getUsuario();
		this.ano = ano;
		this.mes = mes;
		this.valor = valor;
	}

	public SaldoConta(int ano, int mes, BigDecimal valor) {
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

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public SaldoConta withConta(Conta conta) {
		setConta(conta);
		return this;
	}

	public SaldoConta withAno(int ano) {
		setAno(ano);
		return this;
	}

	public SaldoConta withMes(int mes) {
		setMes(mes);
		return this;
	}

	public SaldoConta withValor(BigDecimal valor) {
		setValor(valor);
		return this;
	}

	@PrePersist
	void beforePersist() {
		try {
			if (usuario == null)
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