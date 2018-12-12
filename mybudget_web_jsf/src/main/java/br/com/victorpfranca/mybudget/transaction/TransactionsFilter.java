package br.com.victorpfranca.mybudget.transaction;

import br.com.victorpfranca.mybudget.account.AccountBalanceFilter;
import br.com.victorpfranca.mybudget.view.MonthYear;

public class TransactionsFilter extends AccountBalanceFilter {

    private Integer categoria;
    private TransactionStatus status;

    public static class Builder {
        private MonthYear monthYear;
        private Integer categoria;
        private Integer conta;
        private TransactionStatus status;

        public TransactionsFilter.Builder monthYear(MonthYear monthYear) {
            this.monthYear = monthYear;
            return this;
        }

        public TransactionsFilter.Builder categoria(Integer categoria) {
            this.categoria = categoria;
            return this;
        }

        public TransactionsFilter.Builder conta(Integer conta) {
            this.conta = conta;
            return this;
        }

        public TransactionsFilter.Builder status(TransactionStatus status) {
            this.status = status;
            return this;
        }

        public TransactionsFilter build() {
            return new TransactionsFilter(monthYear, categoria, conta, status);
        }
    }

    public TransactionsFilter(MonthYear monthYear, Integer categoria, Integer conta) {
        super(monthYear, conta);
        this.categoria = categoria;
    }

    public TransactionsFilter(MonthYear monthYear, Integer categoria, Integer conta, TransactionStatus status) {
        super(monthYear, conta);
        this.categoria = categoria;
        this.status = status;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategory(Integer categoria) {
        this.categoria = categoria;
    }
    
    public TransactionStatus getStatus() {
		return status;
	}
    
    public void setStatus(TransactionStatus status) {
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
        if (!(obj instanceof TransactionsFilter)) {
            return false;
        }
        TransactionsFilter other = (TransactionsFilter) obj;
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