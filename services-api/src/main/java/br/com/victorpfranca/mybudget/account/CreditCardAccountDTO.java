package br.com.victorpfranca.mybudget.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CreditCardAccountDTO extends AccountDTO {

	private Integer contaPagamentoId;
	private Integer diaFechamento;
	private Integer diaPagamento;

}
