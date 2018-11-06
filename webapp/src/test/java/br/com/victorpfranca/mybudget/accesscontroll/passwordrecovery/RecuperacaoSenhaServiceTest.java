package br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery.PasswordRecovery;
import br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery.PasswordRecoveryService;

public class RecuperacaoSenhaServiceTest {

    @Test
    public void dataSolicitacaoAposDataAtual() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataSolicitacao = dataAtual.plusSeconds(1);
        assertFalse(service.dataSolicitacaoDentroDoIntervaloValido(dataAtual, dataSolicitacao));
    }

    @Test
    public void dataAtualAposLimiteDataSolicitacao() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        LocalDateTime dataSolicitacao = LocalDateTime.now();
        LocalDateTime dataAtual = dataSolicitacao.plusMinutes(45).plusSeconds(1);
        assertFalse(service.dataSolicitacaoDentroDoIntervaloValido(dataAtual, dataSolicitacao));
    }

    @Test
    public void dataAtualDentroDoIntervaloDeSolicitacao() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        LocalDateTime dataSolicitacao = LocalDateTime.now();
        LocalDateTime dataAtual = dataSolicitacao.plusMinutes(45).minusSeconds(1);
        assertTrue(service.dataSolicitacaoDentroDoIntervaloValido(dataAtual, dataSolicitacao));
    }

    @Test
    public void testaTokenRecuperarSenhaInativo() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setAtivo(false);
        assertFalse(service.codigoAtivo(passwordRecovery));
    }

    @Test
    public void testaTokenRecuperarSenhaNulo() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setAtivo(null);
        assertFalse(service.codigoAtivo(passwordRecovery));
    }

    @Test
    public void testaTokenRecuperarSenhaAtivo() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setAtivo(true);
        assertTrue(service.codigoAtivo(passwordRecovery));
    }

    @Test
    public void testaTokenRecuperacaoSenhaTokenValido() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setCodigo("321");
        assertTrue(service.codigoValido(passwordRecovery, "321"));
    }

    @Test
    public void testaTokenRecuperacaoSenhaTokenDiferente() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setCodigo("123");
        assertFalse(service.codigoValido(passwordRecovery, "321"));
    }

    @Test
    public void testaTokenRecuperacaoSenhaTokenNulo() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setCodigo(null);
        assertFalse(service.codigoValido(passwordRecovery, null));
    }
    @Test
    public void testaTokenRecuperacaoSenhaTokenVazio() {
        PasswordRecoveryService service = new PasswordRecoveryService();
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setCodigo("");
        assertFalse(service.codigoValido(passwordRecovery, ""));
    }

    @Test
    public void testaCriarSolicitacaoSenha() {
        PasswordRecoveryService service = new PasswordRecoveryService(Date::new);
        User user = new User();
        user.setEmail("usuarioQualquer@meussaldos.com.br");
        PasswordRecovery resultado = service.criarSolicitacaoSenha(user);
        assertNotNull(resultado);
        assertNotNull(resultado.getAlvo());
        assertNotNull(resultado.getDataSolicitacao());
        assertTrue(resultado.getAtivo());
        assertTrue(StringUtils.isNotBlank(resultado.getCodigo()));
        assertEquals(user.getEmail(), resultado.getAlvo().getEmail());
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioNulo() {
        PasswordRecoveryService service = new PasswordRecoveryService(Date::new);
        assertFalse(service.permiteRecuperacaoDeSenha((User) null));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioNaoPersistidoSemSituacaoDefinida() {
        PasswordRecoveryService service = new PasswordRecoveryService(Date::new);
        assertFalse(service.permiteRecuperacaoDeSenha(new User()));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioNaoPersistidoAtivo() {
        PasswordRecoveryService service = new PasswordRecoveryService(Date::new);
        User user = new User();
        user.setAtivo(true);
        assertFalse(service.permiteRecuperacaoDeSenha(user));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioNaoPersistidoInativo() {
        PasswordRecoveryService service = new PasswordRecoveryService(Date::new);
        User user = new User();
        user.setAtivo(false);
        assertFalse(service.permiteRecuperacaoDeSenha(user));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioPersistidoSemSituacaoDefinida() {
        PasswordRecoveryService service = new PasswordRecoveryService(Date::new);
        User user = new User();
        user.setId(1);
        assertFalse(service.permiteRecuperacaoDeSenha(user));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioPersistidoInativo() {
        PasswordRecoveryService service = new PasswordRecoveryService(Date::new);
        User user = new User();
        user.setId(1);
        user.setAtivo(false);
        assertFalse(service.permiteRecuperacaoDeSenha(user));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioPersistidoAtivo() {
        PasswordRecoveryService service = new PasswordRecoveryService(Date::new);
        User user = new User();
        user.setId(1);
        user.setAtivo(true);
        assertTrue(service.permiteRecuperacaoDeSenha(user));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesAtivasContendoUmaDentroDoIntervaloValido() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        PasswordRecoveryService service = new PasswordRecoveryService();
        User user = new User();
        user.setId(1);
        user.setAtivo(true);
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setAlvo(user);
        passwordRecovery.setDataSolicitacao(Date.from(baseDate.minusMinutes(15).toInstant(ZoneOffset.UTC)));
        passwordRecovery.setAtivo(true);
        assertFalse(
                service.permiteNovaRecuperacaoDeSenha(user, Collections.singletonList(passwordRecovery), baseDate));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesAtivasContendoUmaAntesDoIntervaloValido() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        PasswordRecoveryService service = new PasswordRecoveryService(
                () -> Date.from(baseDate.toInstant(ZoneOffset.UTC)));
        User user = new User();
        user.setId(1);
        user.setAtivo(true);
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setAlvo(user);
        passwordRecovery.setDataSolicitacao(Date.from(baseDate.minusMinutes(55).toInstant(ZoneOffset.UTC)));
        passwordRecovery.setAtivo(true);
        assertTrue(
                service.permiteNovaRecuperacaoDeSenha(user, Collections.singletonList(passwordRecovery), baseDate));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesAtivasContendoUmaAposIntervaloValido() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        PasswordRecoveryService service = new PasswordRecoveryService(
                () -> Date.from(baseDate.toInstant(ZoneOffset.UTC)));
        User user = new User();
        user.setId(1);
        user.setAtivo(true);
        PasswordRecovery passwordRecovery = new PasswordRecovery();
        passwordRecovery.setAlvo(user);
        passwordRecovery.setDataSolicitacao(Date.from(baseDate.plusMinutes(1).toInstant(ZoneOffset.UTC)));
        passwordRecovery.setAtivo(true);
        assertTrue(
                service.permiteNovaRecuperacaoDeSenha(user, Collections.singletonList(passwordRecovery), baseDate));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesNula() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        PasswordRecoveryService service = new PasswordRecoveryService();
        User user = new User();
        user.setId(1);
        user.setAtivo(true);
        assertTrue(service.permiteNovaRecuperacaoDeSenha(user, null, baseDate));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesAtivasListaVazia() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        PasswordRecoveryService service = new PasswordRecoveryService();
        User user = new User();
        user.setId(1);
        user.setAtivo(true);
        assertTrue(service.permiteNovaRecuperacaoDeSenha(user, Collections.emptyList(), baseDate));
    }


}
