package br.com.victorpfranca.mybudget.budget;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "categoria", "data" })
public class BudgetRealDTO {
	private String categoria;
	private String data;
	private BigDecimal orcado;
	private BigDecimal realizado;
}