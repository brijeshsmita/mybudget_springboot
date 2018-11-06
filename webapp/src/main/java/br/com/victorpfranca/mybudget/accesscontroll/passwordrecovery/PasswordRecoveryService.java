package br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UnknownAccountException;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.infra.date.api.CurrentDateSupplier;
import br.com.victorpfranca.mybudget.infra.jsf.GenericExceptionHandler;
import br.com.victorpfranca.mybudget.infra.mail.MailSender;

@LocalBean
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PasswordRecoveryService {

    @EJB
    private CurrentDateSupplier dateUtils;
    @Inject
    private UserService userService;
    @Inject
    private EntityManager entityManager;
    @Inject
    private PasswordRecoveryQueries passwordRecoveryQueries;
    @Inject
    private MailSender mailSender;

    public PasswordRecoveryService() {
    }

    public PasswordRecoveryService(CurrentDateSupplier dateUtils) {
        this.dateUtils = dateUtils;
    }

    public boolean permiteRecuperacaoDeSenha(String loginOuEmail) {
        return permiteRecuperacaoDeSenha(recuperarUsuario(loginOuEmail));
    }

    public boolean permiteCriarNovaRecuperacaoDeSenha(String loginOuEmail) {
        User user = recuperarUsuario(loginOuEmail);
        return permiteRecuperacaoDeSenha(user) && permiteNovaRecuperacaoDeSenha(user,
                passwordRecoveryQueries.listarAtivasDeUsuario(user), dateUtils.currentLocalDateTime());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void atualizarSenha(String email, String codigo, String novaSenha) {
        PasswordRecovery dadosRecuperacao = passwordRecoveryQueries.ultimaRecuperacaoValidaPara(email, codigo);
        if (!codigoAtivo(dadosRecuperacao)) {
            throw new PasswordRecoveryException(PasswordRecoveryErrorCodes.CODIGO_RECUPERACAO_INATIVO);
        }
        if (!dataSolicitacaoDentroDoIntervaloValido(dateUtils.currentLocalDateTime(),
                DateUtils.dateToLocalDateTime(dadosRecuperacao.getDataSolicitacao()))) {
            throw new PasswordRecoveryException(PasswordRecoveryErrorCodes.PRAZO_RECUPERACAO_INVALIDO);
        }
        if (!codigoValido(dadosRecuperacao, codigo)) {
            throw new PasswordRecoveryException(PasswordRecoveryErrorCodes.CODIGO_RECUPERACAO_INCORRETO);
        }
        User usuarioAlvo = userService.recuperarViaEmail(dadosRecuperacao.getAlvo().getEmail());
        validacoesUsuario(usuarioAlvo);
        userService.updatePassword(usuarioAlvo.getId(), novaSenha);
        dadosRecuperacao.setAtivo(false);
        entityManager.merge(dadosRecuperacao);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PasswordRecovery solicitarRecuperacaoSenha(String email) {
        User user = recuperarUsuario(email);
        validacoesUsuario(user);
        PasswordRecovery passwordRecovery = criarSolicitacaoSenha(user);
        entityManager.persist(passwordRecovery);
        StringBuilder bodyTextBuilder = new StringBuilder();
        bodyTextBuilder.append("<p>").append("Para recuperar sua senha clique ");
        bodyTextBuilder.append("<a href=").append('"')
                .append(resolveUrl(user.getEmail(), passwordRecovery.getCodigo()))
                .append('"').append(">");
        bodyTextBuilder.append("aqui");
        bodyTextBuilder.append("</a>").append("!");
        bodyTextBuilder.append("</p>");
        System.out.println(bodyTextBuilder.toString());
        mailSender.sendMail(user.getEmail(), "Recuperação de Senha", bodyTextBuilder.toString());
        return passwordRecovery;
    }

    private User recuperarUsuario(String email) {
        try {
            return userService.recuperarViaEmail(email);
        } catch (Exception e) {
            GenericExceptionHandler.handle(NoResultException.class, e, r -> {
                throw new UnknownAccountException();
            });
            throw e;
        }
    }

    private String resolveUrl(String email, String codigo) {
        Map<String, List<String>> params = new HashMap<>();
        params.put("codigo", Collections.singletonList(codigo));
        params.put("email", Collections.singletonList(email));
        return resolveUrl("/recoverPassword", params);
    }
    private String resolveUrl(String viewId, Map<String, List<String>> params) {
        UriBuilder uriBuilder = UriBuilder.fromUri(URI.create("https://www.e3a.com.br/e3a"))
            .path(viewId);
        for (Entry<String, List<String>> entry : params.entrySet()) {
            uriBuilder = uriBuilder.queryParam(entry.getKey(), entry.getValue().toArray());
        }
        return uriBuilder.build().toString();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void inativarCodigosExpirados() {
        for (PasswordRecovery passwordRecovery : passwordRecoveryQueries.listarAtivasComDataExpirada()) {
            passwordRecovery.setAtivo(false);
            entityManager.merge(passwordRecovery);
        }
    }

    boolean usuarioAtivo(User usuarioAlvo) {
        return Optional.ofNullable(usuarioAlvo).map(User::getAtivo).orElse(false);
    }

    boolean usuarioExiste(User usuarioAlvo) {
        return Optional.ofNullable(usuarioAlvo).map(User::getId).isPresent();
    }

    boolean codigoValido(PasswordRecovery dadosRecuperacao, String codigo) {
        return !StringUtils.isBlank(codigo) && dadosRecuperacao.getCodigo().equals(codigo);
    }

    boolean dataSolicitacaoDentroDoIntervaloValido(LocalDateTime dataAtual, LocalDateTime inicioDataSolicitacao) {
        LocalDateTime fimDataSolicitacao = inicioDataSolicitacao.plusMinutes(45);
        return dataAtual.isAfter(inicioDataSolicitacao) && dataAtual.isBefore(fimDataSolicitacao);
    }

    boolean codigoAtivo(PasswordRecovery dadosRecuperacao) {
        return Optional.ofNullable(dadosRecuperacao).map(PasswordRecovery::getAtivo).orElse(false);
    }

    PasswordRecovery criarSolicitacaoSenha(User user) {
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setAlvo(user);
        passwordRecovery.setCodigo(UUID.randomUUID().toString().replaceAll("-", ""));
        passwordRecovery.setDataSolicitacao(dateUtils.currentDate());
        passwordRecovery.setAtivo(Boolean.TRUE);
        return passwordRecovery;
    }

    boolean permiteRecuperacaoDeSenha(User user) {
        return usuarioExiste(user) && usuarioAtivo(user);
    }

    boolean permiteNovaRecuperacaoDeSenha(PasswordRecovery rs, LocalDateTime dataAtual) {
        return dataSolicitacaoDentroDoIntervaloValido(dataAtual,
                LocalDateTime.ofInstant(rs.getDataSolicitacao().toInstant(), ZoneId.of("UTC")));
    }

    boolean permiteNovaRecuperacaoDeSenha(User user, List<PasswordRecovery> recuperacoesAtivas,
            LocalDateTime dataAtual) {
        return permiteRecuperacaoDeSenha(user)
                && Optional.ofNullable(recuperacoesAtivas).orElse(Collections.emptyList()).stream()
                        .noneMatch(rs -> permiteNovaRecuperacaoDeSenha(rs, dataAtual));
    }

    private void validacoesUsuario(User user) {
        if (!usuarioExiste(user)) {
            throw new PasswordRecoveryException(PasswordRecoveryErrorCodes.USUARIO_INEXISTENTE);
        }
        if (!usuarioAtivo(user)) {
            throw new PasswordRecoveryException(PasswordRecoveryErrorCodes.USUARIO_INATIVO);
        }
    }

}