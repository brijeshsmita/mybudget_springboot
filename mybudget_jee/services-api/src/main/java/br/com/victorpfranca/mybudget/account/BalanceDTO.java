package br.com.victorpfranca.mybudget.account;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class BalanceDTO {
    private String date;
    private BigDecimal valor;

}
