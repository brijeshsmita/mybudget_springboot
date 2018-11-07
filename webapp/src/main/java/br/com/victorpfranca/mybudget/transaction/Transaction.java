package br.com.victorpfranca.mybudget.transaction;

import static br.com.victorpfranca.mybudget.transaction.Transaction.FIND_LANCAMENTO_CARTAO_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.FIND_LANCAMENTO_FATURA_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.FIND_LANCAMENTO_INICIAL_CARTAO_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.FIND_LANCAMENTO_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.REMOVE_BY_SERIE_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.REMOVE_LANCAMENTOS_CARTAO_CREDITO_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.REMOVE_LANCAMENTOS_CONTA_CORRENTE_QUERY;
import static br.com.victorpfranca.mybudget.transaction.Transaction.REMOVE_LANCAMENTOS_FATURA_CARTAO_ITEM_QUERY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.InOutConverter;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Table(name = "lancamento")
@NamedQueries({

		@NamedQuery(name = FIND_LANCAMENTO_QUERY, query = "SELECT l FROM Transaction l WHERE l.usuario.id=:user AND (:serie is null OR serie = :serie) AND (:category is null OR category = :category) ORDER BY data, id"),
		@NamedQuery(name = FIND_LANCAMENTO_FATURA_QUERY, query = "SELECT l FROM Transaction l WHERE cartaoCreditoFatura = :cartaoCreditoFatura AND data >= :data ORDER BY data ASC"),

		@NamedQuery(name = FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, query = "SELECT l FROM CheckingAccountTransaction l WHERE l.usuario.id=:user AND (:ano is null OR ano = :ano) AND (:mes is null OR mes = :mes) AND (:account is null OR account = :account) AND (:category is null OR category = :category) AND (:saldoInicial is null OR saldoInicial = :saldoInicial) AND (:cartaoCreditoFatura is null OR cartaoCreditoFatura = :cartaoCreditoFatura) AND (:faturaCartao is null OR faturaCartao = :faturaCartao) AND (:status is null OR status= :status) ORDER BY data, id"),

		@NamedQuery(name = FIND_LANCAMENTO_CARTAO_QUERY, query = "SELECT l FROM CreditCardTransaction l WHERE l.usuario.id=:user AND (:serie is null OR serie = :serie) AND (:account is null OR account = :account) AND (:saldoInicial is null OR saldoInicial = :saldoInicial) ORDER BY data, id ASC"),

		@NamedQuery(name = FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY, query = "SELECT l FROM CreditCardInvoiceTransactionItem l WHERE l.usuario.id=:user AND (:lancamentoCartao is null OR lancamentoCartao = :lancamentoCartao) AND (:ano is null OR ano = :ano) AND (:mes is null OR mes = :mes) AND (:account is null OR account = :account) AND (:category is null OR category = :category) ORDER BY data, lancamentoCartao.data, id"),

		@NamedQuery(name = FIND_LANCAMENTO_INICIAL_CARTAO_QUERY, query = "SELECT l FROM CreditCardTransaction l WHERE l.usuario.id=:user AND (:account is null OR account = :account) AND saldoInicial = true ORDER BY data, id ASC"),

		@NamedQuery(name = REMOVE_BY_SERIE_QUERY, query = "DELETE FROM Transaction l WHERE serie = :serie"),
		@NamedQuery(name = REMOVE_LANCAMENTOS_CONTA_CORRENTE_QUERY, query = "DELETE FROM CheckingAccountTransaction l WHERE (:account is null OR account = :account) AND (:saldoInicial is null OR saldoInicial = :saldoInicial)"),
		@NamedQuery(name = REMOVE_LANCAMENTOS_CARTAO_CREDITO_QUERY, query = "DELETE FROM CreditCardTransaction l WHERE account = :account AND (:saldoInicial is null OR saldoInicial = :saldoInicial)"),
		@NamedQuery(name = REMOVE_LANCAMENTOS_FATURA_CARTAO_ITEM_QUERY, query = "DELETE FROM CreditCardInvoiceTransactionItem l WHERE account = :account AND (:saldoInicial is null OR saldoInicial = :saldoInicial)") })
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorLancamento";

	public static final String FIND_LANCAMENTO_QUERY = "Transaction.findLancamento";
	public static final String FIND_LANCAMENTO_FATURA_QUERY = "Transaction.findLancamentoFatura";
	public static final String FIND_LANCAMENTO_CONTA_CORRENTE_QUERY = "Transaction.findLancamentoCorrente";
	public static final String FIND_LANCAMENTO_CARTAO_QUERY = "Transaction.findLancamentoCartao";
	public static final String FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY = "Transaction.findLancamentoFaturaItem";

	public static final String FIND_LANCAMENTO_INICIAL_CARTAO_QUERY = "Transaction.findLancamentoInicialCartao";

	public static final String REMOVE_BY_SERIE_QUERY = "Transaction.removeBySerie";
	public static final String REMOVE_LANCAMENTOS_CONTA_CORRENTE_QUERY = "Transaction.removeLancamentosContaCorrente";
	public static final String REMOVE_LANCAMENTOS_CARTAO_CREDITO_QUERY = "Transaction.removeLancamentosCartaoCredito";
	public static final String REMOVE_LANCAMENTOS_FATURA_CARTAO_ITEM_QUERY = "Transaction.removeLancamentoFaturaCartaoItem";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_lancamento", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@Column(name = "comentario", nullable = false, unique = false)
	protected String comentario;

	@NotNull
	@Column(name = "ano", nullable = false, unique = false)
	protected Integer ano;

	@NotNull
	@Column(name = "mes", nullable = false, unique = false)
	protected Integer mes;

	@NotNull(message = "Qual é a data deste lançamento?")
	@Column(name = "data_lancamento", nullable = false, unique = false)
	@Temporal(TemporalType.DATE)
	protected Date data;

	@Transient
	protected Date dataAnterior;

	@NotNull(message = "Este é um lançamento agendado ou confirmado?")
	@Convert(converter = TransactionStatusConverter.class)
	@Column(name = "status", nullable = false, unique = false)
	protected TransactionStatus status;

	@NotNull(message = "Este é um lançamento de Receita ou de Despesa?")
	@Convert(converter = InOutConverter.class)
	@Column(name = "in_out", nullable = false, unique = false)
	protected InOut inOut;

	@NotNull(message = "Qual é o valor deste lançamento?")
	@Column(name = "valor", nullable = false, unique = false)
	protected BigDecimal valor;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "conta_id")
	protected Account account;

	@Transient
	protected Account contaAnterior;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "lancamento_serie_id")
	protected TransactionSerie serie;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "categoria_id")
	protected Category category;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(name = "ajuste", nullable = false, unique = false)
	private boolean ajuste;

	@Column(name = "saldo_inicial", nullable = false, unique = false)
	protected boolean saldoInicial;

	@Transient
	protected BigDecimal valorAnterior;

	@Transient
	protected BigDecimal saldo;

	public boolean isConfirmado() {
		return getStatus().equals(TransactionStatus.CONFIRMADO);
	}

	public void setConfirmado(boolean confirmado) {
		setStatus(confirmado ? TransactionStatus.CONFIRMADO : TransactionStatus.NAO_CONFIRMADO);
	}

	public void setData(Date data) {
		if (data != null) {
			LocalDate date = LocalDateConverter.fromDate(data);

			setMes(date.getMonthValue());
			setAno(date.getYear());
		}

		this.data = data;
	}

	public BigDecimal somarSaldo(BigDecimal saldo) {
		if (getInOut().equals(InOut.S))
			setSaldo((getValor()).negate().add(saldo));
		else
			setSaldo((getValor()).add(saldo));
		return this.saldo;
	}

	public TransactionVO getVO() {
		TransactionVO transactionVO = new TransactionVO();
		transactionVO.setCategory(getCategory());
		transactionVO.setComentario(getComentario());
		transactionVO.setAccount(getAccount());
		transactionVO.setContaAnterior(contaAnterior);
		transactionVO.setData(getData());
		transactionVO.setDataAnterior(getDataAnterior());
		transactionVO.setId(getId());
		transactionVO.setInOut(getInOut());
		transactionVO.setStatus(getStatus());
		transactionVO.setValor(getValor());
		transactionVO.setValorAnterior(getValorAnterior());
		transactionVO.setAjuste(isAjuste());

		if (getSerie() != null) {
			transactionVO.setSerie(getSerie());
			transactionVO.setDataInicio(getSerie().getDataInicio());
			transactionVO.setDataLimite(getSerie().getDataLimite());
			transactionVO.setFrequencia(getSerie().getFrequencia());
		}

		transactionVO.setUsuario(user);
		return transactionVO;
	}

	@Override
	public Object clone() {

		Transaction transaction = new Transaction();

		transaction.setData(data);
		transaction.setDataAnterior(dataAnterior);
		transaction.setAno(ano);
		transaction.setMes(mes);
		transaction.setCategory(category);
		transaction.setComentario(comentario);
		transaction.setAccount(account);
		transaction.setContaAnterior(contaAnterior);
		transaction.setInOut(inOut);
		transaction.setSerie(serie);
		transaction.setStatus(status);
		transaction.setValor(valor);
		transaction.setValorAnterior(valorAnterior);
		transaction.setUser(user);

		return transaction;
	}

	public Transaction getLancamentoAnterior() {
		Transaction lancamentoAntigo = (Transaction) clone();
		lancamentoAntigo.setAccount(getContaAnterior());
		lancamentoAntigo.setValor(getValorAnterior());
		lancamentoAntigo.setDataAnterior(getDataAnterior());
		return lancamentoAntigo;
	}

	@PrePersist
	void beforePersist() {
		try {
			setUser(((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado());
		} catch (NamingException e) {
			throw new RuntimeException(e.getExplanation());
		}
	}

	public boolean contaFoiAlterada() {
		return getContaAnterior() != null && !getAccount().equals(getContaAnterior());
	}

	public void validar() throws TransactionMonthUpdatedException, NullableAccountException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
		validarData();
		validarConta();
		validarInOut();
		validarValor();
		validarCategoriaNaoNula();
	}

	protected boolean isPermiteCategoriaNula() {
		return isAjuste() || isSaldoInicial();
	}

	protected void validarCategoriaNaoNula() throws IncompatibleCategoriesException {
		if (!isPermiteCategoriaNula() && getCategory() == null) {
			throw new IncompatibleCategoriesException("crud.lancamento.error.categoria.null");
		}
	}

	protected void validarValor() throws InvalidTransactionValueException {
		if (getValor().compareTo(BigDecimal.ZERO) <= 0)
			throw new InvalidTransactionValueException("crud_lancamento_validator_valor_lancamento");
	}

	protected void validarData() throws TransactionMonthUpdatedException {
		if (getDataAnterior() != null) {
			int mesAnterior = LocalDateConverter.fromDate(getDataAnterior()).getMonthValue();
			int mesAtual = LocalDateConverter.fromDate(getData()).getMonthValue();
			if (mesAnterior != mesAtual) {
				throw new TransactionMonthUpdatedException("crud_lancamento_validator_mes_lancamento");
			}
		}
	}

	protected void validarConta() throws NullableAccountException, AccountTypeException {
		if (getAccount() == null) {
			throw new NullableAccountException("crud_lancamento_validator_conta");
		}
		if (getContaAnterior() != null && !getAccount().getClass().equals(getContaAnterior().getClass())) {
			throw new AccountTypeException("crud_lancamento_validator_tipo_conta");
		}
	}

	protected void validarInOut() throws IncompatibleCategoriesException {
		if (getCategory() != null && !getCategory().getInOut().equals(getInOut())) {
			throw new IncompatibleCategoriesException("crud.lancamento.error.tipo.categoria");
		}
	}

}