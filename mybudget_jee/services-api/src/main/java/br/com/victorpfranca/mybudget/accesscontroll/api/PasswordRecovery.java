package br.com.victorpfranca.mybudget.accesscontroll.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PasswordRecovery {
    private String email;
}
