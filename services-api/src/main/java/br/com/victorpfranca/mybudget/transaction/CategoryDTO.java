package br.com.victorpfranca.mybudget.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of="id") @NoArgsConstructor @AllArgsConstructor @Data
public class CategoryDTO {
    
    private Integer id;
    private String nome;
    
}