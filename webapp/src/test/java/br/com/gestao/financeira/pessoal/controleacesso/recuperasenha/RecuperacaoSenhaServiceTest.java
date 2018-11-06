package br.com.gestao.financeira.pessoal.controleacesso.recuperasenha;

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

import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

public class RecuperacaoSenhaServiceTest {

    @Test
    public void dataSolicitacaoAposDataAtual() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataSolicitacao = dataAtual.plusSeconds(1);
        assertFalse(service.dataSolicitacaoDentroDoIntervaloValido(dataAtual, dataSolicitacao));
    }

    @Test
    public void dataAtualAposLimiteDataSolicitacao() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        LocalDateTime dataSolicitacao = LocalDateTime.now();
        LocalDateTime dataAtual = dataSolicitacao.plusMinutes(45).plusSeconds(1);
        assertFalse(service.dataSolicitacaoDentroDoIntervaloValido(dataAtual, dataSolicitacao));
    }

    @Test
    public void dataAtualDentroDoIntervaloDeSolicitacao() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        LocalDateTime dataSolicitacao = LocalDateTime.now();
        LocalDateTime dataAtual = dataSolicitacao.plusMinutes(45).minusSeconds(1);
        assertTrue(service.dataSolicitacaoDentroDoIntervaloValido(dataAtual, dataSolicitacao));
    }

    @Test
    public void testaTokenRecuperarSenhaInativo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setAtivo(false);
        assertFalse(service.codigoAtivo(recuperacaoSenha));
    }

    @Test
    public void testaTokenRecuperarSenhaNulo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setAtivo(null);
        assertFalse(service.codigoAtivo(recuperacaoSenha));
    }

    @Test
    public void testaTokenRecuperarSenhaAtivo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setAtivo(true);
        assertTrue(service.codigoAtivo(recuperacaoSenha));
    }

    @Test
    public void testaTokenRecuperacaoSenhaTokenValido() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setCodigo("321");
        assertTrue(service.codigoValido(recuperacaoSenha, "321"));
    }

    @Test
    public void testaTokenRecuperacaoSenhaTokenDiferente() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setCodigo("123");
        assertFalse(service.codigoValido(recuperacaoSenha, "321"));
    }

    @Test
    public void testaTokenRecuperacaoSenhaTokenNulo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setCodigo(null);
        assertFalse(service.codigoValido(recuperacaoSenha, null));
    }
    @Test
    public void testaTokenRecuperacaoSenhaTokenVazio() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setCodigo("");
        assertFalse(service.codigoValido(recuperacaoSenha, ""));
    }

    @Test
    public void testaCriarSolicitacaoSenha() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(Date::new);
        Usuario usuario = new Usuario();
        usuario.setEmail("usuarioQualquer@meussaldos.com.br");
        RecuperacaoSenha resultado = service.criarSolicitacaoSenha(usuario);
        assertNotNull(resultado);
        assertNotNull(resultado.getAlvo());
        assertNotNull(resultado.getDataSolicitacao());
        assertTrue(resultado.getAtivo());
        assertTrue(StringUtils.isNotBlank(resultado.getCodigo()));
        assertEquals(usuario.getEmail(), resultado.getAlvo().getEmail());
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioNulo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(Date::new);
        assertFalse(service.permiteRecuperacaoDeSenha((Usuario) null));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioNaoPersistidoSemSituacaoDefinida() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(Date::new);
        assertFalse(service.permiteRecuperacaoDeSenha(new Usuario()));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioNaoPersistidoAtivo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(Date::new);
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);
        assertFalse(service.permiteRecuperacaoDeSenha(usuario));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioNaoPersistidoInativo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(Date::new);
        Usuario usuario = new Usuario();
        usuario.setAtivo(false);
        assertFalse(service.permiteRecuperacaoDeSenha(usuario));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioPersistidoSemSituacaoDefinida() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(Date::new);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        assertFalse(service.permiteRecuperacaoDeSenha(usuario));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioPersistidoInativo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(Date::new);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setAtivo(false);
        assertFalse(service.permiteRecuperacaoDeSenha(usuario));
    }

    @Test
    public void testaPermiteRecuperacaoDeSenha_UsuarioPersistidoAtivo() {
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(Date::new);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setAtivo(true);
        assertTrue(service.permiteRecuperacaoDeSenha(usuario));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesAtivasContendoUmaDentroDoIntervaloValido() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setAtivo(true);
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setAlvo(usuario);
        recuperacaoSenha.setDataSolicitacao(Date.from(baseDate.minusMinutes(15).toInstant(ZoneOffset.UTC)));
        recuperacaoSenha.setAtivo(true);
        assertFalse(
                service.permiteNovaRecuperacaoDeSenha(usuario, Collections.singletonList(recuperacaoSenha), baseDate));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesAtivasContendoUmaAntesDoIntervaloValido() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(
                () -> Date.from(baseDate.toInstant(ZoneOffset.UTC)));
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setAtivo(true);
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setAlvo(usuario);
        recuperacaoSenha.setDataSolicitacao(Date.from(baseDate.minusMinutes(55).toInstant(ZoneOffset.UTC)));
        recuperacaoSenha.setAtivo(true);
        assertTrue(
                service.permiteNovaRecuperacaoDeSenha(usuario, Collections.singletonList(recuperacaoSenha), baseDate));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesAtivasContendoUmaAposIntervaloValido() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        RecuperacaoSenhaService service = new RecuperacaoSenhaService(
                () -> Date.from(baseDate.toInstant(ZoneOffset.UTC)));
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setAtivo(true);
        RecuperacaoSenha recuperacaoSenha = new RecuperacaoSenha();
        recuperacaoSenha.setAlvo(usuario);
        recuperacaoSenha.setDataSolicitacao(Date.from(baseDate.plusMinutes(1).toInstant(ZoneOffset.UTC)));
        recuperacaoSenha.setAtivo(true);
        assertTrue(
                service.permiteNovaRecuperacaoDeSenha(usuario, Collections.singletonList(recuperacaoSenha), baseDate));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesNula() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setAtivo(true);
        assertTrue(service.permiteNovaRecuperacaoDeSenha(usuario, null, baseDate));
    }

    @Test
    public void testaPermiteNovaRecuperacaoDeSenha_RecuperacoesAtivasListaVazia() {
        LocalDateTime baseDate = LocalDateTime.of(2018, 6, 6, 12, 15);
        RecuperacaoSenhaService service = new RecuperacaoSenhaService();
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setAtivo(true);
        assertTrue(service.permiteNovaRecuperacaoDeSenha(usuario, Collections.emptyList(), baseDate));
    }


}
