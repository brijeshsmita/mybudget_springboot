package br.com.victorpfranca.mybudget.controleacesso.api;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of="email")
public class UsuarioDTO {

    private String email;
    private String nome;
    private String sobrenome;
    private Date dataCadastro;
    private Date dataUltimoAcesso;

}
