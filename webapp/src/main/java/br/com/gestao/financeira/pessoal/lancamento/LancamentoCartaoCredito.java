package br.com.gestao.financeira.pessoal.lancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("1")
@Getter
@Setter
@NoArgsConstructor
public class LancamentoCartaoCredito extends Lancamento {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Parcelou em quantas vezes?")
	@Column(name = "qtd_parcelas", nullable = true, unique = false)
	private Integer qtdParcelas;

	public LancamentoCartaoCredito(InOut inOut, LancamentoStatus status) {
		setStatus(status);
		setInOut(inOut);
		setSaldo(BigDecimal.ZERO);
	}

	@Override
	public LancamentoVO getVO() {
		LancamentoVO lancamentoVO = super.getVO();
		lancamentoVO.setQtdParcelas(getQtdParcelas());
		return lancamentoVO;
	}

	public LancamentoFaturaCartaoItem buildFaturaItem(Date data, int ano, int mes, int indiceParcela) {
		LancamentoFaturaCartaoItem faturaItem = new LancamentoFaturaCartaoItem();

		faturaItem.setCategoria(categoria);
		faturaItem.setComentario(comentario);
		faturaItem.setConta(conta);
		faturaItem.setAjuste(isAjuste());
		faturaItem.setInOut(inOut.equals(InOut.E) ? InOut.S : InOut.E);
		faturaItem.setLancamentoCartao(this);
		faturaItem.setSerie(serie);
		faturaItem.setStatus(status);

		faturaItem.setData(data);
		faturaItem.setMes(mes);
		faturaItem.setAno(ano);
		faturaItem.setValor(valor.divide(BigDecimal.valueOf(qtdParcelas), 2, RoundingMode.HALF_UP));
		faturaItem.setQtdParcelas(qtdParcelas);
		faturaItem.setIndiceParcela(indiceParcela);

		return faturaItem;
	}

	@Override
	protected void validarConta() throws ContaNotNullException, TipoContaException {
		super.validarConta();
		if (!isAjuste() && InOut.E.equals(getInOut())) {
			throw new TipoLancamentoInvalidoException("crud.lancamento.cartao.error.tipo");
		}
	}

	@Override
	public Object clone() {
		LancamentoCartaoCredito lancamento = new LancamentoCartaoCredito();

		lancamento.setSaldoInicial(saldoInicial);
		lancamento.setData(data);
		lancamento.setDataAnterior(dataAnterior);
		lancamento.setAno(ano);
		lancamento.setMes(mes);
		lancamento.setCategoria(categoria);
		lancamento.setComentario(comentario);
		lancamento.setConta(conta);
		lancamento.setContaAnterior(contaAnterior);
		lancamento.setInOut(inOut);
		lancamento.setSerie(serie);
		lancamento.setStatus(status);
		lancamento.setValor(valor);
		lancamento.setValorAnterior(valorAnterior);

		lancamento.setQtdParcelas(qtdParcelas);

		return lancamento;
	}

}
