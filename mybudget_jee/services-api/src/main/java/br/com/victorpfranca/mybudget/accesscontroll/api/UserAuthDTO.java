package br.com.victorpfranca.mybudget.accesscontroll.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of="email")
public class UserAuthDTO {

    private String email;
    private String password;
    
    @Override
    public String toString() {
        return getEmail();
    }

}
