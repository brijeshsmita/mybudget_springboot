package br.com.gestao.financeira.pessoal.controleacesso.recuperasenha;

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

import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.controleacesso.UsuarioService;
import br.com.gestao.financeira.pessoal.infra.date.DateUtils;
import br.com.gestao.financeira.pessoal.infra.date.api.CurrentDateSupplier;
import br.com.gestao.financeira.pessoal.infra.jsf.GenericExceptionHandler;
import br.com.gestao.financeira.pessoal.infra.mail.MailSender;

@LocalBean
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RecuperacaoSenhaService {

    @EJB
    private CurrentDateSupplier dateUtils;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private EntityManager entityManager;
    @Inject
    private RecuperacaoSenhaConsultas recuperacaoSenhaConsultas;
    @Inject
    private MailSender mailSender;

    public RecuperacaoSenhaService() {
    }

    public RecuperacaoSenhaService(CurrentDateSupplier dateUtils) {
        this.dateUtils = dateUtils;
    }

    public boolean permiteRecuperacaoDeSenha(String loginOuEmail) {
        return permiteRecuperacaoDeSenha(recuperarUsuario(loginOuEmail));
    }

    public boolean permiteCriarNovaRecuperacaoDeSenha(String loginOuEmail) {
        Usuario usuario = recuperarUsuario(loginOuEmail);
        return permiteRecuperacaoDeSenha(usuario) && permiteNovaRecuperacaoDeSenha(usuario,
                recuperacaoSenhaConsultas.listarAtivasDeUsuario(usuario), dateUtils.currentLocalDateTime());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void atualizarSenha(String email, String codigo, String novaSenha) {
        RecuperacaoSenha dadosRecuperacao = recuperacaoSenhaConsultas.ultimaRecuperacaoValidaPara(email, codigo);
        if (!codigoAtivo(dadosRecuperacao)) {
            throw new RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes.CODIGO_RECUPERACAO_INATIVO);
        }
        if (!dataSolicitacaoDentroDoIntervaloValido(dateUtils.currentLocalDateTime(),
                DateUtils.dateToLocalDateTime(dadosRecuperacao.getDataSolicitacao()))) {
            throw new RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes.PRAZO_RECUPERACAO_INVALIDO);
        }
        if (!codigoValido(dadosRecuperacao, codigo)) {
            throw new RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes.CODIGO_RECUPERACAO_INCORRETO);
        }
        Usuario usuarioAlvo = usuarioService.recuperarViaEmail(dadosRecuperacao.getAlvo().getEmail());
        validacoesUsuario(usuarioAlvo);
        usuarioService.updatePassword(usuarioAlvo.getId(), novaSenha);
        dadosRecuperacao.setAtivo(false);
        entityManager.merge(dadosRecuperacao);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public RecuperacaoSenha solicitarRecuperacaoSenha(String email) {
        Usuario usuario = recuperarUsuario(email);
        validacoesUsuario(usuario);
        RecuperacaoSenha recuperacaoSenha = criarSolicitacaoSenha(usuario);
        entityManager.persist(recuperacaoSenha);
        StringBuilder bodyTextBuilder = new StringBuilder();
        bodyTextBuilder.append("<p>").append("Para recuperar sua senha clique ");
        bodyTextBuilder.append("<a href=").append('"')
                .append(resolveUrl(usuario.getEmail(), recuperacaoSenha.getCodigo()))
                .append('"').append(">");
        bodyTextBuilder.append("aqui");
        bodyTextBuilder.append("</a>").append("!");
        bodyTextBuilder.append("</p>");
        System.out.println(bodyTextBuilder.toString());
        mailSender.sendMail(usuario.getEmail(), "Recuperação de Senha", bodyTextBuilder.toString());
        return recuperacaoSenha;
    }

    private Usuario recuperarUsuario(String email) {
        try {
            return usuarioService.recuperarViaEmail(email);
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
        for (RecuperacaoSenha recuperacaoSenha : recuperacaoSenhaConsultas.listarAtivasComDataExpirada()) {
            recuperacaoSenha.setAtivo(false);
            entityManager.merge(recuperacaoSenha);
        }
    }

    boolean usuarioAtivo(Usuario usuarioAlvo) {
        return Optional.ofNullable(usuarioAlvo).map(Usuario::getAtivo).orElse(false);
    }

    boolean usuarioExiste(Usuario usuarioAlvo) {
        return Optional.ofNullable(usuarioAlvo).map(Usuario::getId).isPresent();
    }

    boolean codigoValido(RecuperacaoSenha dadosRecuperacao, String codigo) {
        return !StringUtils.isBlank(codigo) && dadosRecuperacao.getCodigo().equals(codigo);
    }

    boolean dataSolicitacaoDentroDoIntervaloValido(LocalDateTime dataAtual, LocalDateTime inicioDataSolicitacao) {
        LocalDateTime fimDataSolicitacao = inicioDataSolicitacao.plusMinutes(45);
        return dataAtual.isAfter(inicioDataSolicitacao) && dataAtual.isBefore(fimDataSolicitacao);
    }

    boolean codigoAtivo(RecuperacaoSenha dadosRecuperacao) {
        return Optional.ofNullable(dadosRecuperacao).map(RecuperacaoSenha::getAtivo).orElse(false);
    }

    RecuperacaoSenha criarSolicitacaoSenha(Usuario usuario) {
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setAlvo(usuario);
        recuperacaoSenha.setCodigo(UUID.randomUUID().toString().replaceAll("-", ""));
        recuperacaoSenha.setDataSolicitacao(dateUtils.currentDate());
        recuperacaoSenha.setAtivo(Boolean.TRUE);
        return recuperacaoSenha;
    }

    boolean permiteRecuperacaoDeSenha(Usuario usuario) {
        return usuarioExiste(usuario) && usuarioAtivo(usuario);
    }

    boolean permiteNovaRecuperacaoDeSenha(RecuperacaoSenha rs, LocalDateTime dataAtual) {
        return dataSolicitacaoDentroDoIntervaloValido(dataAtual,
                LocalDateTime.ofInstant(rs.getDataSolicitacao().toInstant(), ZoneId.of("UTC")));
    }

    boolean permiteNovaRecuperacaoDeSenha(Usuario usuario, List<RecuperacaoSenha> recuperacoesAtivas,
            LocalDateTime dataAtual) {
        return permiteRecuperacaoDeSenha(usuario)
                && Optional.ofNullable(recuperacoesAtivas).orElse(Collections.emptyList()).stream()
                        .noneMatch(rs -> permiteNovaRecuperacaoDeSenha(rs, dataAtual));
    }

    private void validacoesUsuario(Usuario usuario) {
        if (!usuarioExiste(usuario)) {
            throw new RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes.USUARIO_INEXISTENTE);
        }
        if (!usuarioAtivo(usuario)) {
            throw new RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes.USUARIO_INATIVO);
        }
    }

}