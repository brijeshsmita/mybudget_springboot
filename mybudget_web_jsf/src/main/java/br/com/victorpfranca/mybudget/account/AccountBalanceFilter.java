package br.com.victorpfranca.mybudget.account;

import java.util.Objects;

import br.com.victorpfranca.mybudget.view.MonthYear;

public class AccountBalanceFilter {
    
    private MonthYear monthYear;
    private Integer conta;
    
    public static class Builder {
        private MonthYear monthYear;
        private Integer conta;
        
        public AccountBalanceFilter.Builder monthYear(MonthYear monthYear) {
            this.monthYear=monthYear;
            return this;
        }
        public AccountBalanceFilter.Builder conta(Integer conta) {
            this.conta=conta;
            return this;
        }
        
        public AccountBalanceFilter build() {
            return new AccountBalanceFilter(monthYear, conta);
        }
    }
    
    public AccountBalanceFilter(AccountBalanceFilter accountBalanceFilter) {
        this(accountBalanceFilter.getAnoMes(), accountBalanceFilter.getAccount());
    }
    public AccountBalanceFilter(MonthYear monthYear, Integer conta) {
        this.monthYear = Objects.requireNonNull(monthYear);
        this.conta=conta;
    }
    
    public MonthYear getAnoMes() {
        return monthYear;
    }
    public void setAnoMes(MonthYear monthYear) {
        this.monthYear = monthYear;
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
        result = prime * result + ((monthYear == null) ? 0 : monthYear.hashCode());
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
        if (monthYear == null) {
            if (other.monthYear != null) {
                return false;
            }
        } else if (!monthYear.equals(other.monthYear)) {
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