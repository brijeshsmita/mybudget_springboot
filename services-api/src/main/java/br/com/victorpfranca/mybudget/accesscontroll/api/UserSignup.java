package br.com.victorpfranca.mybudget.accesscontroll.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.victorpfranca.mybudget.view.validation.Email;
import br.com.victorpfranca.mybudget.view.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of="email")
public class UserSignup {
    
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
