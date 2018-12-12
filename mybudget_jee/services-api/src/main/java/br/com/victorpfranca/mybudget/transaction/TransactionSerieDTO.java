package br.com.victorpfranca.mybudget.transaction;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSerieDTO {
	/**
	 * '1' : MENSAL(1, "Mensal")
	 * '2' : QUINZENAL(2, "Quinzenal")
	 * '3' : SEMANAL(3, "Semanal")
	 */
	@NotNull
	private Character frequencia;
	@NotNull
	private String dataInicio;
	@NotNull
	private String dataLimite;

}