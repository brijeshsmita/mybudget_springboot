package br.com.gestao.financeira.pessoal.conta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ContaCartaoDTO extends ContaDTO {

	private Integer contaPagamentoId;
	private Integer diaFechamento;
	private Integer diaPagamento;

}
