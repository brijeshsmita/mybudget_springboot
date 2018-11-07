package br.com.victorpfranca.mybudget.transaction;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRegistryDTO {
	private Integer categoria;
	@NotNull
	private Integer conta;
	@NotNull
	private BigDecimal valor;
	@NotNull
	private String data;
	/**
	 * '1' : NAO_CONFIRMADO(1, "Agendado")
	 * '2' : CONFIRMADO(2, "Pago")
	 */
	private Character status;
	/**
	 * '0' : E(0, "Receita")
	 * '1' : S(1, "Despesa")
	 */
	private Character tipo;

	private String comentario;

	private TransactionSerieDTO serie;

	@Min(1)
	private Integer parcelas;

	private Integer contaDestino;

	private boolean ajuste;

	public boolean isLancamentoCartao() {
		return getParcelas() != null;
	}

	public boolean isSerie() {
		return getSerie() != null;
	}

}