package br.com.victorpfranca.mybudget.transaction;

import java.io.Serializable;
import java.util.Date;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionSerieDateException;

public class TransactionSerie implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private TransactionFrequence frequencia;

	private Date dataLimite;

	private Date dataInicio;

	private User user;

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

	public void validarDatas() throws InvalidTransactionSerieDateException {
		if (dataInicio.compareTo(dataLimite) > 0)
			throw new InvalidTransactionSerieDateException("crud.lancamento.serie.error.datas");
	}

}