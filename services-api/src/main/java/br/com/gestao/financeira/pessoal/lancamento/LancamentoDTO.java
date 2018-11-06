package br.com.gestao.financeira.pessoal.lancamento;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "id")
@Data
public class LancamentoDTO {

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
