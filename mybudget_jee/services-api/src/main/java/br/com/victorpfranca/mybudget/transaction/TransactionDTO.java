package br.com.victorpfranca.mybudget.transaction;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "id")
@Data
public class TransactionDTO {

	private Integer id;
	private String conta;
	private String categoria;
	private String data;
	private Character status;
	private String contaOrigem;
	private String contaDestino;
	private String comentario;
	private BigDecimal valor;
	private BigDecimal saldo;
	private boolean ajuste;
	private boolean faturaCartao;
	private boolean saldoInicial;
	private boolean parteSerie;
	private String cartaoCreditoFatura;
	private Integer parcelas;

}
