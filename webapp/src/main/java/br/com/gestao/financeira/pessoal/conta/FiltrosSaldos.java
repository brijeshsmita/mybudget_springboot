package br.com.gestao.financeira.pessoal.conta;

import java.util.Objects;

import br.com.gestao.financeira.pessoal.view.AnoMes;

public class FiltrosSaldos {
    
    private AnoMes anoMes;
    private Integer conta;
    
    public static class Builder {
        private AnoMes anoMes;
        private Integer conta;
        
        public FiltrosSaldos.Builder anoMes(AnoMes anoMes) {
            this.anoMes=anoMes;
            return this;
        }
        public FiltrosSaldos.Builder conta(Integer conta) {
            this.conta=conta;
            return this;
        }
        
        public FiltrosSaldos build() {
            return new FiltrosSaldos(anoMes, conta);
        }
    }
    
    public FiltrosSaldos(FiltrosSaldos filtrosSaldos) {
        this(filtrosSaldos.getAnoMes(), filtrosSaldos.getConta());
    }
    public FiltrosSaldos(AnoMes anoMes, Integer conta) {
        this.anoMes = Objects.requireNonNull(anoMes);
        this.conta=conta;
    }
    
    public AnoMes getAnoMes() {
        return anoMes;
    }
    public void setAnoMes(AnoMes anoMes) {
        this.anoMes = anoMes;
    }
    public Integer getConta() {
        return conta;
    }
    public void setConta(Integer conta) {
        this.conta = conta;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((anoMes == null) ? 0 : anoMes.hashCode());
        result = prime * result + ((conta == null) ? 0 : conta.hashCode());
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
        if (!(obj instanceof FiltrosSaldos)) {
            return false;
        }
        FiltrosSaldos other = (FiltrosSaldos) obj;
        if (anoMes == null) {
            if (other.anoMes != null) {
                return false;
            }
        } else if (!anoMes.equals(other.anoMes)) {
            return false;
        }
        if (conta == null) {
            if (other.conta != null) {
                return false;
            }
        } else if (!conta.equals(other.conta)) {
            return false;
        }
        return true;
    }
    

}