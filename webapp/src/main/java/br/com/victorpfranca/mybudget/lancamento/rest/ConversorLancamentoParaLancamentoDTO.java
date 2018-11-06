package br.com.victorpfranca.mybudget.lancamento.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.lancamento.AtualizacaoLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.AtualizacaoSerieLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.CadastroLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.CategoriaDTO;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.LancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.LancamentoFrequencia;
import br.com.victorpfranca.mybudget.lancamento.LancamentoStatus;
import br.com.victorpfranca.mybudget.lancamento.SerieLancamento;
import br.com.victorpfranca.mybudget.lancamento.SerieLancamentoDTO;

public class ConversorLancamentoParaLancamentoDTO {

	public void aplicarValores(Lancamento lancamento, AtualizacaoSerieLancamentoDTO dto,
			Function<Integer, Category> categoriaFinder, Function<Integer, Account> contaFinder) {
		lancamento.setCategory(category(dto, categoriaFinder.compose(AtualizacaoSerieLancamentoDTO::getCategoria)));
		lancamento.setAccount(account(dto, contaFinder.compose(AtualizacaoSerieLancamentoDTO::getConta)));
		lancamento.setValor(dto.getValor());
		lancamento.setStatus(LancamentoStatus.fromChar(dto.getStatus()));
		lancamento.setComentario(dto.getComentario());
	}

	public void aplicarValores(Lancamento lancamento, AtualizacaoLancamentoDTO dto,
			Function<Integer, Category> categoriaFinder, Function<Integer, Account> contaFinder) {
		lancamento.setCategory(category(dto, categoriaFinder.compose(AtualizacaoLancamentoDTO::getCategoria)));
		lancamento.setAccount(account(dto, contaFinder.compose(AtualizacaoLancamentoDTO::getConta)));
		lancamento.setData(data(dto, AtualizacaoLancamentoDTO::getData));
		lancamento.setValor(dto.getValor());
		lancamento.setStatus(LancamentoStatus.fromChar(dto.getStatus()));
		lancamento.setComentario(dto.getComentario());
	}

	public Lancamento converter(CadastroLancamentoDTO dto, Function<Integer, Category> categoriaFinder,
			Function<Integer, Account> contaFinder) {
		Lancamento resultado = null;
		Account account = account(dto, contaFinder.compose(CadastroLancamentoDTO::getConta));
		if (dto.isLancamentoCartao() || account instanceof CreditCardAccount) {
			resultado = new LancamentoCartaoCredito();
			((LancamentoCartaoCredito) resultado).setQtdParcelas(dto.getParcelas());
		} else {
			resultado = new LancamentoContaCorrente();

			if (dto.getContaDestino() != null)
				((LancamentoContaCorrente) resultado)
						.setContaDestino(account(dto, contaFinder.compose(CadastroLancamentoDTO::getContaDestino)));

		}
		resultado.setData(data(dto, CadastroLancamentoDTO::getData));
		resultado.setAccount(account);
		if(dto.getCategoria() != null)
			resultado.setCategory(category(dto, categoriaFinder.compose(CadastroLancamentoDTO::getCategoria)));
		resultado.setValor(dto.getValor());
		resultado.setInOut(InOut.fromChar(dto.getTipo()));
		resultado.setStatus(LancamentoStatus.fromChar(dto.getStatus()));
		resultado.setComentario(dto.getComentario());
		resultado.setSerie(serie(dto));
		resultado.setAjuste(dto.isAjuste());

		return resultado;
	}

	private <X> Date data(X dto, Function<X, String> getData) {
		return Optional.ofNullable(dto).map(getData).map(DateUtils::iso8601).orElse(null);
	}

	private SerieLancamento serie(CadastroLancamentoDTO dto) {
		return Optional.ofNullable(dto).map(CadastroLancamentoDTO::getSerie).map(serie -> {
			SerieLancamento serieLancamento = new SerieLancamento();
			serieLancamento.setDataInicio(data(serie, SerieLancamentoDTO::getDataInicio));
			serieLancamento.setDataLimite(data(serie, SerieLancamentoDTO::getDataLimite));
			serieLancamento.setFrequencia(LancamentoFrequencia.fromChar(serie.getFrequencia()));
			return serieLancamento;
		}).orElse(null);
	}

	private <X> Category category(X dto, Function<X, Category> categoriaFinder) {
		return Optional.ofNullable(dto).map(categoriaFinder).orElse(null);
	}

	private <X> Account account(X dto, Function<X, Account> contaFinder) {
		return Optional.ofNullable(dto).map(contaFinder).orElse(null);
	}

	public LancamentoDTO converter(Lancamento lancamento) {
		LancamentoDTO dto = new LancamentoDTO();
		dto.setId(lancamento.getId());
		dto.setStatus(status(lancamento));
		dto.setData(dataIso8601(lancamento));
		dto.setConta(nomeConta(lancamento));
		dto.setCategoria(nomeCategoria(lancamento));
		dto.setContaOrigem(nomeContaOrigem(lancamento));
		dto.setContaDestino(nomeContaDestino(lancamento));
		dto.setFaturaCartao(isFaturaCartao(lancamento));
		dto.setSaldoInicial(isSaldoInicial(lancamento));
		dto.setParteSerie(isParteSerie(lancamento));
		dto.setComentario(lancamento.getComentario());
		dto.setValor(valorResolvido(lancamento));
		dto.setSaldo(lancamento.getSaldo());
		dto.setAjuste(lancamento.isAjuste());
		dto.setCartaoCreditoFatura(nomeCartaoCreditoFatura(lancamento));
		return dto;
	}

	private BigDecimal valorResolvido(Lancamento lancamento) {
		BigDecimal resultado = lancamento.getValor();
		if (InOut.S == lancamento.getInOut()) {
			resultado = resultado.multiply(BigDecimal.valueOf(-1l));
		}
		return resultado;
	}

	private boolean isParteSerie(Lancamento lancamento) {
		return lancamento.getSerie() != null;
	}

	private boolean isSaldoInicial(Lancamento lancamento) {
		return lancamento instanceof LancamentoContaCorrente && ((LancamentoContaCorrente) lancamento).isSaldoInicial();
	}

	private boolean isFaturaCartao(Lancamento lancamento) {
		return lancamento instanceof LancamentoContaCorrente && ((LancamentoContaCorrente) lancamento).isFaturaCartao();
	}

	private String dataIso8601(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).map(Lancamento::getData).map(DateUtils::iso8601).orElse(null);
	}

	private Character status(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).map(Lancamento::getStatus).map(LancamentoStatus::getValue).orElse(null);
	}

	private String nomeConta(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).map(Lancamento::getAccount).map(c -> c.getNome()).orElse(null);
	}

//	private ContaDTO account(Lancamento lancamento) {
//		return Optional.ofNullable(lancamento).map(Lancamento::getAccount)
//				.map(c -> new ContaDTO(c.getId(), c.getNome(),
//						c instanceof MoneyAccount ? AccountType.CONTA_DINHEIRO.getValue()
//								: c instanceof BankAccount ? AccountType.CONTA_BANCO.getValue()
//										: AccountType.CARTAO_CREDITO.getValue()))
//				.orElse(null);
//	}

	private BigDecimal saldoInicial(Account c) {
		return Optional.ofNullable(c).filter(CheckingAccount.class::isInstance).map(CheckingAccount.class::cast)
				.map(CheckingAccount::getSaldoInicial).orElse(null);
	}

	private String nomeCategoria(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).map(Lancamento::getCategory).map(c -> c.getNome()).orElse(null);
	}

	private CategoriaDTO categoria(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).map(Lancamento::getCategory)
				.map(c -> new CategoriaDTO(c.getId(), c.getNome())).orElse(null);
	}

	private String nomeContaDestino(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).filter(LancamentoContaCorrente.class::isInstance)
				.map(LancamentoContaCorrente.class::cast).map(LancamentoContaCorrente::getContaDestino)
				.map(Account::getNome).orElse(null);
	}

	private String nomeContaOrigem(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).filter(LancamentoContaCorrente.class::isInstance)
				.map(LancamentoContaCorrente.class::cast).map(LancamentoContaCorrente::getContaOrigem)
				.map(Account::getNome).orElse(null);
	}

	private String nomeCartaoCreditoFatura(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).filter(LancamentoContaCorrente.class::isInstance)
				.map(LancamentoContaCorrente.class::cast).map(LancamentoContaCorrente::getCartaoCreditoFatura)
				.map(Account::getNome).orElse(null);
	}

}