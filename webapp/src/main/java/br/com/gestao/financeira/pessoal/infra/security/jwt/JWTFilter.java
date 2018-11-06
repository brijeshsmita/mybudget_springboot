package br.com.gestao.financeira.pessoal.infra.security.jwt;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTFilter extends AccessControlFilter {

    public static final String CLAIM_JWT_USER_ID = "http://e3a.com.br/userid";

    @Inject
    private JwtStore jwtStore;

    @Override
    protected boolean isAccessAllowed(ServletRequest arg0, ServletResponse arg1, Object arg2) throws Exception {
        HttpServletRequest request = (HttpServletRequest) arg0;
        String headerJwt = request.getHeader("Authorization");
        String queryParamJwt = request.getParameter("jwt_key");
        String resolvedJwt = Optional.ofNullable(headerJwt).map(s -> s.substring(s.indexOf(" "))).orElse(queryParamJwt);
        if (StringUtils.isNotBlank(resolvedJwt)) {
            try {
                Jws<Claims> validate = jwtStore.validate(resolvedJwt);
                SecurityUtils.getSubject().login(new JwtAuthenticationToken(validate));
                return SecurityUtils.getSubject().isAuthenticated();
            } catch (ExpiredJwtException| UnsupportedJwtException| MalformedJwtException| SignatureException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest arg0, ServletResponse response) throws Exception {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.flushBuffer();
        return false;
    }

}
