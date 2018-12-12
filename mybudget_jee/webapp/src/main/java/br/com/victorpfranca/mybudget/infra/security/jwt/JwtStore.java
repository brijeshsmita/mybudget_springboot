package br.com.victorpfranca.mybudget.infra.security.jwt;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.infra.date.api.CurrentDateSupplier;
import br.com.victorpfranca.mybudget.view.Messages;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;

@ApplicationScoped
public class JwtStore implements Serializable , JwtActions {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private CredentialsStore credentialsStore;
    @EJB
    private CurrentDateSupplier dateUtils;
    @Inject
    private UserService userService;
    
    private Map<String, String> signingKeys;
    private Map<String, String> keyForUser;
    
    @PostConstruct
    void onInit() {
        signingKeys = new HashMap<>();
        keyForUser = new HashMap<>();
    }
    
    public String genJwt() {
        return Optional.ofNullable(credentialsStore)
                .map(CredentialsStore::recuperarUsuarioLogado)
                .map(Function.<User>identity()
                        .andThen(User::getEmail)
                        .andThen(this::genJWTFromMail)).orElse(null);
    }

    @Override
    public String jwt(String email) {
        return Optional.ofNullable(email)
            .map(userService::recuperarViaEmail)
            .filter(User::getAtivo)
            .map(User::getEmail)
            .map(this::genJWTFromMail).orElse(null);
    }
    
    public Jws<Claims> validate(String jwsString) {
        SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter() {
            @Override
            public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
                if (SignatureAlgorithm.HS512.equals(SignatureAlgorithm.forName(header.getAlgorithm()))) {
                    return Optional.ofNullable(claims.get(JWTFilter.CLAIM_JWT_USER_ID, String.class))
                            .map(keyForUser::get).map(s->s.getBytes(StandardCharsets.UTF_8)).orElse(new byte[0]);
                }
                throw new MalformedJwtException(Messages.msg("br.com.victorpfranca.mybudget.infra.security.jwt.JwtStore.MalformedJwtException", header.getAlgorithm()));
            }
        };
        Jws<Claims> jws = Jwts.parser()
            .setClock(dateUtils::currentDate)
            .setSigningKeyResolver(signingKeyResolver)
            .parseClaimsJws(jwsString);
        return jws;
    }
    
    private String genJWTFromMail(String email) {
        final LocalDateTime now = dateUtils.currentLocalDateTime();
        final String uuid = UUID.randomUUID().toString();
        final Date nowDate = DateUtils.localDateTimeToDate(now);
        final Date oneDayFromNow = DateUtils.localDateTimeToDate(now.plusDays(1));
        if (keyForUser == null) {
            keyForUser = new HashMap<>();
        }
        keyForUser.putIfAbsent(email, UUID.randomUUID().toString());
        String JWT = Jwts.builder()
                .setId(uuid)
                .claim(JWTFilter.CLAIM_JWT_USER_ID, email)
                .setIssuedAt(nowDate)
                .setExpiration(oneDayFromNow)
                .signWith(SignatureAlgorithm.HS512, keyForUser.get(email).getBytes(StandardCharsets.UTF_8))
                .compact();
        signingKeys.put(uuid, JWT);
        return JWT;
    }
    
    
}
