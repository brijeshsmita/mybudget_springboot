package br.com.victorpfranca.mybudget.infra.security.jwt;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.SimplePrincipalCollection;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class JwtAuthenticationToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private Jws<Claims> jwt;

    public JwtAuthenticationToken() {
    }
    
    public JwtAuthenticationToken(Jws<Claims> jwt) {
        this.jwt=jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    public String getUsername() {
        return jwt.getBody().get(JWTFilter.CLAIM_JWT_USER_ID, String.class);
    }

}
