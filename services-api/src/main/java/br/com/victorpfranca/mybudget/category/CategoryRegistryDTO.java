package br.com.victorpfranca.mybudget.category;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRegistryDTO {

	@NotNull
	private String nome;
	@NotNull
	private Character tipo;

}