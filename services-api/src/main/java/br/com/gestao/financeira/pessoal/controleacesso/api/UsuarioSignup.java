package br.com.gestao.financeira.pessoal.controleacesso.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.gestao.financeira.pessoal.view.validation.Email;
import br.com.gestao.financeira.pessoal.view.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of="email")
public class UsuarioSignup {
    
    @NotNull
    @Size(min = 1, max=20)
    private String nome;
    @NotNull
    @Size(min = 1, max=20)
    private String sobrenome;
    @NotNull
    @Email
    @Size(min = 1, max=70)
    private String email;
    @NotNull
    @ValidPassword
    private String password;

}
