package br.com.victorpfranca.mybudget.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	protected String comentario;

	protected Integer ano;

	protected Integer mes;

	protected Date data;

	protected Date dataAnterior;

	protected TransactionStatus status;

	protected InOut inOut;

	protected BigDecimal valor;

	protected Account conta;

	protected Account contaAnterior;

	protected TransactionSerie serie;

	protected Category categoria;

	private User user;

	private boolean ajuste;

	protected boolean saldoInicial;

	protected BigDecimal valorAnterior;

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

	public TransactionVO getVO() {
		TransactionVO transactionVO = new TransactionVO();
		transactionVO.setCategoria(getCategoria());
		transactionVO.setComentario(getComentario());
		transactionVO.setConta(getConta());
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
		transaction.setCategoria(categoria);
		transaction.setComentario(comentario);
		transaction.setConta(conta);
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
		lancamentoAntigo.setConta(getContaAnterior());
		lancamentoAntigo.setValor(getValorAnterior());
		lancamentoAntigo.setDataAnterior(getDataAnterior());
		return lancamentoAntigo;
	}

	public boolean contaFoiAlterada() {
		return getContaAnterior() != null && !getConta().equals(getContaAnterior());
	}

	protected boolean isPermiteCategoriaNula() {
		return isAjuste() || isSaldoInicial();
	}

}