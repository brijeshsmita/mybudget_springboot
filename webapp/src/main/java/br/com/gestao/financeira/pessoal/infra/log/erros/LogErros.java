package br.com.gestao.financeira.pessoal.infra.log.erros;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "log_erros")
public class LogErros {

    @Id
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;
    @Column(name = "uuid", insertable = false, updatable = false)
    private String codigo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data", insertable = false, updatable = false)
    private Date dataOcorrencia;
    @Column(name = "severidade", insertable = false, updatable = false)
    private String severidade;
    @Column(name = "categoria", insertable = false, updatable = false)
    private String categoria;
    @Column(name = "ip", insertable = false, updatable = false)
    private String ip;
    @Column(name = "usuario", insertable = false, updatable = false)
    private String usuario;
    @Column(name = "cabecalhos_http", insertable = false, updatable = false)
    private String cabecalhosHttp;
    @Column(name = "mensagem", insertable = false, updatable = false)
    private String mensagem;
    @Column(name = "pilha_excecao", insertable = false, updatable = false)
    private String pilhaExcecao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(Date dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public String getSeveridade() {
        return severidade;
    }

    public void setSeveridade(String severidade) {
        this.severidade = severidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCabecalhosHttp() {
        return cabecalhosHttp;
    }

    public void setCabecalhosHttp(String cabecalhosHttp) {
        this.cabecalhosHttp = cabecalhosHttp;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getPilhaExcecao() {
        return pilhaExcecao;
    }

    public void setPilhaExcecao(String pilhaExcecao) {
        this.pilhaExcecao = pilhaExcecao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof LogErros)) {
            return false;
        }
        LogErros other = (LogErros) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
