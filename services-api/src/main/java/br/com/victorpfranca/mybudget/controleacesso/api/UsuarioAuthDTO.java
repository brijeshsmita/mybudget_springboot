package br.com.victorpfranca.mybudget.controleacesso.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of="email")
public class UsuarioAuthDTO {

    private String email;
    private String password;
    
    @Override
    public String toString() {
        return getEmail();
    }

}
