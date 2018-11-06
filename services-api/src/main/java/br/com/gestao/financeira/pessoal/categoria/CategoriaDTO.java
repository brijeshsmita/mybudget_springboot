package br.com.gestao.financeira.pessoal.categoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CategoriaDTO {

	private Integer id;

	private String nome;

	private Character tipo;

}