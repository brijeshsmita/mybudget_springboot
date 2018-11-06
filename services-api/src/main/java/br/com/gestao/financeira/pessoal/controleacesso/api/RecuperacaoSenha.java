package br.com.gestao.financeira.pessoal.controleacesso.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class RecuperacaoSenha {
    private String email;
}
