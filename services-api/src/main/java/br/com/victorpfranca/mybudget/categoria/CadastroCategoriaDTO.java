package br.com.victorpfranca.mybudget.categoria;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastroCategoriaDTO {

	@NotNull
	private String nome;
	@NotNull
	private Character tipo;

}