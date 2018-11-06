package br.com.gestao.financeira.pessoal.lancamento;

import static br.com.gestao.financeira.pessoal.lancamento.SerieLancamento.FIND_ALL_QUERY;

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

import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.lancamento.rules.DataSerieLancamentoInvalidaException;

@Entity
@Table(name = "lancamento_serie")
@NamedQueries({
		@NamedQuery(name = FIND_ALL_QUERY, query = "SELECT l FROM SerieLancamento l WHERE l.usuario.id=:usuario") })
public class SerieLancamento implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorLancamento";

	public static final String FIND_ALL_QUERY = "SerieLancamento.findAll";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_lancamento_serie", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Convert(converter = LancamentoFrequenciaConverter.class)
	@Column(name = "frequencia", nullable = false, unique = false)
	private LancamentoFrequencia frequencia;

	@Column(name = "data_limite", nullable = false, unique = false)
	@Temporal(TemporalType.DATE)
	private Date dataLimite;

	@Column(name = "data_inicio", nullable = false, unique = false)
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	public List<Lancamento> gerarLancamentos(Lancamento lancamento) {
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();

		LocalDate dataInicial = LocalDateConverter.fromDate(getDataInicio());
		LocalDate dataLimite = LocalDateConverter.fromDate(getDataLimite());

		int fator = 0;
		LocalDate proximaData = dataInicial;

		while (proximaData.isBefore(dataLimite) || proximaData.isEqual(dataLimite)) {
			Lancamento lancamentoCopia = (Lancamento) lancamento.clone();
			lancamentoCopia.setData(LocalDateConverter.toDate(proximaData));
			lancamentoCopia.setDataAnterior(null);
			lancamentos.add(lancamentoCopia);
			proximaData = getNextDate(dataInicial, ++fator);
		}

		return lancamentos;
	}

	private LocalDate getNextDate(LocalDate data, int fator) {
		if (getFrequencia().equals(LancamentoFrequencia.SEMANAL)) {
			data = data.plusDays(DayOfWeek.values().length * fator);
		} else if (getFrequencia().equals(LancamentoFrequencia.QUINZENAL)) {
			data = data.plusWeeks(2 * fator);
		} else if (getFrequencia().equals(LancamentoFrequencia.MENSAL)) {
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

	public LancamentoFrequencia getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(LancamentoFrequencia frequencia) {
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@PrePersist
	void beforePersist() {
		try {
			this.usuario = ((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
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