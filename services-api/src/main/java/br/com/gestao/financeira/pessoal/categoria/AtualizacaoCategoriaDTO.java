package br.com.gestao.financeira.pessoal.categoria;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoCategoriaDTO {

	@NotNull
	private String nome;

}