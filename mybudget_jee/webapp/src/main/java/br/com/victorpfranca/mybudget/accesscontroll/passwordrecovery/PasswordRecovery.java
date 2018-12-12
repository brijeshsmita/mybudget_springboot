package br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.accesscontroll.User;

@Entity
@Table(name = "recuperacao_senha")
public class PasswordRecovery implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String GENERATOR = "GeneratorRecuperacaoSenha";

    @Id
    @SequenceGenerator(name = GENERATOR, sequenceName = "sq_recuperacaosenha", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;
    @NotNull
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User alvo;
    @NotNull
    @Column(name = "dt_solicitacao")
    private Date dataSolicitacao;
    @NotNull
    @Column(name = "cod_solicitacao")
    private String codigo;
    @NotNull
    @Column(name = "ativo")
    private Boolean ativo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAlvo() {
        return alvo;
    }

    public void setAlvo(User alvo) {
        this.alvo = alvo;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
