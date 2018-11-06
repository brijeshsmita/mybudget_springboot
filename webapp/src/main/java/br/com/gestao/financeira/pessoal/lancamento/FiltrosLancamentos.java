package br.com.gestao.financeira.pessoal.lancamento;

import br.com.gestao.financeira.pessoal.conta.FiltrosSaldos;
import br.com.gestao.financeira.pessoal.view.AnoMes;

public class FiltrosLancamentos extends FiltrosSaldos {

    private Integer categoria;
    private LancamentoStatus status;

    public static class Builder {
        private AnoMes anoMes;
        private Integer categoria;
        private Integer conta;
        private LancamentoStatus status;

        public FiltrosLancamentos.Builder anoMes(AnoMes anoMes) {
            this.anoMes = anoMes;
            return this;
        }

        public FiltrosLancamentos.Builder categoria(Integer categoria) {
            this.categoria = categoria;
            return this;
        }

        public FiltrosLancamentos.Builder conta(Integer conta) {
            this.conta = conta;
            return this;
        }

        public FiltrosLancamentos.Builder status(LancamentoStatus status) {
            this.status = status;
            return this;
        }

        public FiltrosLancamentos build() {
            return new FiltrosLancamentos(anoMes, categoria, conta, status);
        }
    }

    public FiltrosLancamentos(AnoMes anoMes, Integer categoria, Integer conta) {
        super(anoMes, conta);
        this.categoria = categoria;
    }

    public FiltrosLancamentos(AnoMes anoMes, Integer categoria, Integer conta, LancamentoStatus status) {
        super(anoMes, conta);
        this.categoria = categoria;
        this.status = status;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }
    
    public LancamentoStatus getStatus() {
		return status;
	}
    
    public void setStatus(LancamentoStatus status) {
		this.status = status;
	}
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
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
        if (!(obj instanceof FiltrosLancamentos)) {
            return false;
        }
        FiltrosLancamentos other = (FiltrosLancamentos) obj;
        if (!super.equals(obj)) {
            return false;
        }
        if (categoria == null) {
            if (other.categoria != null) {
                return false;
            }
        } else if (!categoria.equals(other.categoria)) {
            return false;
        }
        return true;
    }

}