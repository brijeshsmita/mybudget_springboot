package br.com.victorpfranca.mybudget.account;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BankAccountDTO extends AccountDTO{

	private BigDecimal saldoInicial;

}
