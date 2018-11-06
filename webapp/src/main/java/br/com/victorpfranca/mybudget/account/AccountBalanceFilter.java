package br.com.victorpfranca.mybudget.account;

import java.util.Objects;

import br.com.victorpfranca.mybudget.view.AnoMes;

public class AccountBalanceFilter {
    
    private AnoMes anoMes;
    private Integer conta;
    
    public static class Builder {
        private AnoMes anoMes;
        private Integer conta;
        
        public AccountBalanceFilter.Builder anoMes(AnoMes anoMes) {
            this.anoMes=anoMes;
            return this;
        }
        public AccountBalanceFilter.Builder conta(Integer conta) {
            this.conta=conta;
            return this;
        }
        
        public AccountBalanceFilter build() {
            return new AccountBalanceFilter(anoMes, conta);
        }
    }
    
    public AccountBalanceFilter(AccountBalanceFilter accountBalanceFilter) {
        this(accountBalanceFilter.getAnoMes(), accountBalanceFilter.getAccount());
    }
    public AccountBalanceFilter(AnoMes anoMes, Integer conta) {
        this.anoMes = Objects.requireNonNull(anoMes);
        this.conta=conta;
    }
    
    public AnoMes getAnoMes() {
        return anoMes;
    }
    public void setAnoMes(AnoMes anoMes) {
        this.anoMes = anoMes;
    }
    public Integer getAccount() {
        return conta;
    }
    public void setAccount(Integer conta) {
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
        if (!(obj instanceof AccountBalanceFilter)) {
            return false;
        }
        AccountBalanceFilter other = (AccountBalanceFilter) obj;
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