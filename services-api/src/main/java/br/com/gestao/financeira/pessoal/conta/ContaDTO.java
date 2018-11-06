package br.com.gestao.financeira.pessoal.conta;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {

	private Integer id;

	private String nome;

	/**
	 * '0' : Banco
	 * '1' : Cartão
	 * '2' : Dinheiro
	 */
	private Character tipo;
	
	private String dataSaldoInicial;
	
	// conta corrente(banco e dinheiro)
	private BigDecimal saldoInicial;
	
	// conta cartão
	private Integer contaPagamentoId;
	private String contaPagamentoNome;
	private Integer diaFechamento;
	private Integer diaPagamento;

}
