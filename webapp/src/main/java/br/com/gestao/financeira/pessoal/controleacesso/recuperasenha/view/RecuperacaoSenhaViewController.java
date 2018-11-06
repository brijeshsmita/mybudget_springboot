package br.com.gestao.financeira.pessoal.controleacesso.recuperasenha.view;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import br.com.gestao.financeira.pessoal.controleacesso.recuperasenha.RecuperacaoSenhaConsultas;
import br.com.gestao.financeira.pessoal.controleacesso.recuperasenha.RecuperacaoSenhaService;
import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.FacesUtils;
import br.com.gestao.financeira.pessoal.view.validation.ValidPassword;

@Named
@ViewScoped
public class RecuperacaoSenhaViewController implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1)
    private String username;
    @NotNull
    @Size(min = 32, max = 32)
    private String codigo;
    @NotNull
    @ValidPassword
    private String password;
    private String confirmPassword;
    private boolean possuiCodigo = false;
    private boolean codigoValido = false;
    @EJB
    private RecuperacaoSenhaService recuperacaoSenhaService;
    @Inject
    private RecuperacaoSenhaConsultas recuperacaoSenhaConsultas;

    @PostConstruct
    void inicializacao() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String codigo = StringUtils.trimToEmpty(requestParameterMap.get("codigo"));
        String email = StringUtils.trimToEmpty(requestParameterMap.get("email"));
        possuiCodigo = StringUtils.isNotBlank(codigo) && StringUtils.isNotBlank(email);
        if (possuiCodigo) {
            codigoValido = recuperacaoSenhaConsultas.confirmaValidadeDoCodigo(email, codigo);
            if (codigoValido) {
                setCodigo(codigo);
                setUsername(email);
            } else {
                possuiCodigo = false;
                FacesUtils.redirect("");
                FacesMessages.error("Este código não é mais válido");
            }
        }
    }

    public void solicitarRecuperacaoSenha() {
        recuperacaoSenhaService.solicitarRecuperacaoSenha(getUsername());
        FacesMessages.info("e-Mail enviado para confirmação");
        possuiCodigo = true;
    }

    public void confirmarJaPossuiCodigo() {
        possuiCodigo = true;
    }

    public void confirmarCodigo() {
        codigoValido = recuperacaoSenhaConsultas.confirmaValidadeDoCodigo(getUsername(), getCodigo());
    }

    public void atualizarSenha() {
        recuperacaoSenhaService.atualizarSenha(getUsername(), getCodigo(), getPassword());
        FacesUtils.redirect("/");
    }

    public boolean isExibeFormSolicitarSenha() {
        return !(possuiCodigo || codigoValido);
    }

    public boolean isExibeFormConfirmarCodigo() {
        return possuiCodigo && !codigoValido;
    }

    public boolean isExibeFormAtualizarSenha() {
        return codigoValido && possuiCodigo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
