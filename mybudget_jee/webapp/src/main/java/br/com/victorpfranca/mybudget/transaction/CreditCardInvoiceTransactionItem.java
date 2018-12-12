package br.com.victorpfranca.mybudget.transaction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("2")
@Getter @Setter @NoArgsConstructor
public class CreditCardInvoiceTransactionItem extends Transaction {

	private static final long serialVersionUID = 1L;

	@Column(name = "qtd_parcelas", nullable = true, unique = false)
	private Integer qtdParcelas;

	@Column(name = "indice_parcela", nullable = true, unique = false)
	private Integer indiceParcela;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "lancamento_cartao_id")
	private Transaction lancamentoCartao;

}
