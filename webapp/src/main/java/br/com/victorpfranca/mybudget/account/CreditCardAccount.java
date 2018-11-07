package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;

@Entity
@DiscriminatorValue("1")
public class CreditCardAccount extends Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Com qual account esta fatura será paga?")
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = false, name = "conta_pagamento_fatura_id")
	private Account contaPagamentoFatura;

	@NotNull(message = "Qual é o dia de fechamento da fatura deste cartão?")
	@Column(name = "cartao_dia_fechamento", unique = false)
	private Integer cartaoDiaFechamento;

	@NotNull(message = "Qual é o dia de pagamento da fatura deste cartão?")
	@Column(name = "cartao_dia_pagamento", unique = false)
	private Integer cartaoDiaPagamento;

	@Transient
	private Integer cartaoDiaFechamentoAnterior;

	@Transient
	private Integer cartaoDiaPagamentoAnterior;

	@Transient
	protected List<Transaction> transactions;
	
	public CreditCardAccount() {
		this.transactions = new ArrayList<Transaction>();
	}

	public CreditCardAccount(String nome) {
		super(nome);
		this.transactions = new ArrayList<Transaction>();
	}

	@PostLoad
	public void carregarValoresAnteriores() {
		setCartaoDiaFechamentoAnterior(cartaoDiaFechamento);
		setCartaoDiaPagamentoAnterior(cartaoDiaPagamento);
	}

	public Date getDataFechamentoProximo(Date dataReferencia) {
		LocalDate dataReferenciaLocalDate = LocalDateConverter.fromDate(dataReferencia);

		LocalDate dataFechamentoMesReferencia = null;
		int diasNoMes = dataReferenciaLocalDate.lengthOfMonth();
		if (getCartaoDiaFechamento() <= diasNoMes) {
			dataFechamentoMesReferencia = dataReferenciaLocalDate.withDayOfMonth(getCartaoDiaFechamento());
		} else {
			dataFechamentoMesReferencia = dataReferenciaLocalDate.plusMonths(1).withDayOfMonth(1);
		}

		if (dataFechamentoMesReferencia.compareTo(dataReferenciaLocalDate) < 0) {
			return Date
					.from(dataFechamentoMesReferencia.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		return Date.from(dataFechamentoMesReferencia.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public LocalDate getDataPagamentoProximo(Date dataReferencia) {
		Date dataFechamentoAtual = getDataFechamentoProximo(dataReferencia);

		Calendar cal = Calendar.getInstance();
		cal.setTime(dataFechamentoAtual);
		cal.set(Calendar.DAY_OF_MONTH, getCartaoDiaPagamento());
		if (cal.getTime().before(dataFechamentoAtual)) {
			cal.add(Calendar.MONTH, 1);
		}
		return cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public List<Transaction> carregarFaturas(CreditCardTransaction lancamento, List<Transaction> faturasExistentes)
			throws NullableAccountException {

		return carregarFaturasAPartirDe(lancamento, faturasExistentes,
				LocalDateConverter.toDate(getDataPagamentoProximo(lancamento.getData())));
	}

	public List<Transaction> carregarFaturasAPartirDe(CreditCardTransaction lancamento,
			List<Transaction> faturasExistentes, Date dateProximoPagamento) throws NullableAccountException {
		LocalDate localDateProximoPagamento = LocalDateConverter.fromDate(dateProximoPagamento);

		int qtdParcelas = lancamento.getQtdParcelas();
		BigDecimal valorParcela = lancamento.getValor().divide(BigDecimal.valueOf(qtdParcelas), 2,
				RoundingMode.HALF_UP);
		if (lancamento.getInOut().equals(InOut.E))
			valorParcela = valorParcela.negate();

		if (!faturasExistentes.isEmpty()
				&& faturasExistentes.get(0).getData().after(LocalDateConverter.toDate(localDateProximoPagamento))) {
			faturasExistentes.add(0,
					CheckingAccountTransaction.buildFaturaCartao(this,
							Date.from(localDateProximoPagamento.atStartOfDay(ZoneId.systemDefault()).toInstant()),
							BigDecimal.ZERO));
		}

		List<Transaction> faturas = new ArrayList<Transaction>();
		for (int i = 0; i < qtdParcelas; i++) {
			Transaction fatura = null;
			if (faturasExistentes.size() - 1 >= i) {
				fatura = faturasExistentes.get(i);
				fatura.setValorAnterior(fatura.getValor());
				fatura.setValor(fatura.getValor().add(valorParcela));
			} else {
				fatura = CheckingAccountTransaction.buildFaturaCartao(this,
						Date.from(localDateProximoPagamento.atStartOfDay(ZoneId.systemDefault()).toInstant()),
						valorParcela);
			}
			faturas.add(fatura);
			localDateProximoPagamento = localDateProximoPagamento.plusMonths(1);
		}
		return faturas;
	}

	public Integer getCartaoDiaFechamento() {
		return cartaoDiaFechamento;
	}

	public void setCartaoDiaFechamento(Integer cartaoDiaFechamento) {
		this.cartaoDiaFechamento = cartaoDiaFechamento;
	}

	public Integer getCartaoDiaPagamento() {
		return cartaoDiaPagamento;
	}

	public void setCartaoDiaPagamento(Integer cartaoDiaPagamento) {
		this.cartaoDiaPagamento = cartaoDiaPagamento;
	}

	public Integer getCartaoDiaFechamentoAnterior() {
		return cartaoDiaFechamentoAnterior;
	}

	public void setCartaoDiaFechamentoAnterior(Integer cartaoDiaFechamentoAnterior) {
		this.cartaoDiaFechamentoAnterior = cartaoDiaFechamentoAnterior;
	}

	public Integer getCartaoDiaPagamentoAnterior() {
		return cartaoDiaPagamentoAnterior;
	}

	public void setCartaoDiaPagamentoAnterior(Integer cartaoDiaPagamentoAnterior) {
		this.cartaoDiaPagamentoAnterior = cartaoDiaPagamentoAnterior;
	}

	public Account getAccountPagamentoFatura() {
		return contaPagamentoFatura;
	}

	public void setContaPagamentoFatura(Account contaPagamentoFatura) {
		this.contaPagamentoFatura = contaPagamentoFatura;
	}

	public List<Transaction> getLancamentos() {
		return transactions;
	}

	public void setLancamentos(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}