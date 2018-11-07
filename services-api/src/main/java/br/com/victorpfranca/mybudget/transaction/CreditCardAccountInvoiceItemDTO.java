package br.com.victorpfranca.mybudget.transaction;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "id")
@Data
public class CreditCardAccountInvoiceItemDTO {

	private Integer id;
	private String conta;
	private String categoria;
	private String dataCompra;
	private Character status;
	private String comentario;
	private Integer qtdParcelas;
	private Integer indiceParcelas;
	
	private BigDecimal valorCompra;
	private BigDecimal valorParcela;
	private BigDecimal saldo;
	
}
