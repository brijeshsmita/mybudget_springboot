package br.com.victorpfranca.mybudget.transaction;

import static br.com.victorpfranca.mybudget.transaction.TransactionSerie.FIND_ALL_QUERY;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.transaction.rules.DataSerieLancamentoInvalidaException;

@Entity
@Table(name = "lancamento_serie")
@NamedQueries({
		@NamedQuery(name = FIND_ALL_QUERY, query = "SELECT l FROM TransactionSerie l WHERE l.usuario.id=:user") })
public class TransactionSerie implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorLancamento";

	public static final String FIND_ALL_QUERY = "TransactionSerie.findAll";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_lancamento_serie", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Convert(converter = FrequencyTransactionConverter.class)
	@Column(name = "frequencia", nullable = false, unique = false)
	private TransactionFrequence frequencia;

	@Column(name = "data_limite", nullable = false, unique = false)
	@Temporal(TemporalType.DATE)
	private Date dataLimite;

	@Column(name = "data_inicio", nullable = false, unique = false)
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public List<Transaction> gerarLancamentos(Transaction transaction) {
		List<Transaction> transactions = new ArrayList<Transaction>();

		LocalDate dataInicial = LocalDateConverter.fromDate(getDataInicio());
		LocalDate dataLimite = LocalDateConverter.fromDate(getDataLimite());

		int fator = 0;
		LocalDate proximaData = dataInicial;

		while (proximaData.isBefore(dataLimite) || proximaData.isEqual(dataLimite)) {
			Transaction lancamentoCopia = (Transaction) transaction.clone();
			lancamentoCopia.setData(LocalDateConverter.toDate(proximaData));
			lancamentoCopia.setDataAnterior(null);
			transactions.add(lancamentoCopia);
			proximaData = getNextDate(dataInicial, ++fator);
		}

		return transactions;
	}

	private LocalDate getNextDate(LocalDate data, int fator) {
		if (getFrequencia().equals(TransactionFrequence.SEMANAL)) {
			data = data.plusDays(DayOfWeek.values().length * fator);
		} else if (getFrequencia().equals(TransactionFrequence.QUINZENAL)) {
			data = data.plusWeeks(2 * fator);
		} else if (getFrequencia().equals(TransactionFrequence.MENSAL)) {
			data = data.plusMonths(1 * fator);
		}
		return data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TransactionFrequence getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(TransactionFrequence frequencia) {
		this.frequencia = frequencia;
	}

	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	@PrePersist
	void beforePersist() {
		try {
			this.user = ((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado();
		} catch (NamingException e) {
			throw new RuntimeException(e.getExplanation());
		}
	}

	public void validarDatas() throws DataSerieLancamentoInvalidaException {
		if (dataInicio.compareTo(dataLimite) > 0)
			throw new DataSerieLancamentoInvalidaException("crud.lancamento.serie.error.datas");
	}

}