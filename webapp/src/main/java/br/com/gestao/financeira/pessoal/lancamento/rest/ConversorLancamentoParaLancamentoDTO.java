package br.com.gestao.financeira.pessoal.lancamento.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.infra.date.DateUtils;
import br.com.gestao.financeira.pessoal.lancamento.AtualizacaoLancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.AtualizacaoSerieLancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.CadastroLancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.CategoriaDTO;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoFrequencia;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoStatus;
import br.com.gestao.financeira.pessoal.lancamento.SerieLancamento;
import br.com.gestao.financeira.pessoal.lancamento.SerieLancamentoDTO;

public class ConversorLancamentoParaLancamentoDTO {

	public void aplicarValores(Lancamento lancamento, AtualizacaoSerieLancamentoDTO dto,
			Function<Integer, Categoria> categoriaFinder, Function<Integer, Conta> contaFinder) {
		lancamento.setCategoria(categoria(dto, categoriaFinder.compose(AtualizacaoSerieLancamentoDTO::getCategoria)));
		lancamento.setConta(conta(dto, contaFinder.compose(AtualizacaoSerieLancamentoDTO::getConta)));
		lancamento.setValor(dto.getValor());
		lancamento.setStatus(LancamentoStatus.fromChar(dto.getStatus()));
		lancamento.setComentario(dto.getComentario());
	}

	public void aplicarValores(Lancamento lancamento, AtualizacaoLancamentoDTO dto,
			Function<Integer, Categoria> categoriaFinder, Function<Integer, Conta> contaFinder) {
		lancamento.setCategoria(categoria(dto, categoriaFinder.compose(AtualizacaoLancamentoDTO::getCategoria)));
		lancamento.setConta(conta(dto, contaFinder.compose(AtualizacaoLancamentoDTO::getConta)));
		lancamento.setData(data(dto, AtualizacaoLancamentoDTO::getData));
		lancamento.setValor(dto.getValor());
		lancamento.setStatus(LancamentoStatus.fromChar(dto.getStatus()));
		lancamento.setComentario(dto.getComentario());
	}

	public Lancamento converter(CadastroLancamentoDTO dto, Function<Integer, Categoria> categoriaFinder,
			Function<Integer, Conta> contaFinder) {
		Lancamento resultado = null;
		Conta conta = conta(dto, contaFinder.compose(CadastroLancamentoDTO::getConta));
		if (dto.isLancamentoCartao() || conta instanceof ContaCartao) {
			resultado = new LancamentoCartaoCredito();
			((LancamentoCartaoCredito) resultado).setQtdParcelas(dto.getParcelas());
		} else {
			resultado = new LancamentoContaCorrente();

			if (dto.getContaDestino() != null)
				((LancamentoContaCorrente) resultado)
						.setContaDestino(conta(dto, contaFinder.compose(CadastroLancamentoDTO::getContaDestino)));

		}
		resultado.setData(data(dto, CadastroLancamentoDTO::getData));
		resultado.setConta(conta);
		if(dto.getCategoria() != null)
			resultado.setCategoria(categoria(dto, categoriaFinder.compose(CadastroLancamentoDTO::getCategoria)));
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

	private <X> Categoria categoria(X dto, Function<X, Categoria> categoriaFinder) {
		return Optional.ofNullable(dto).map(categoriaFinder).orElse(null);
	}

	private <X> Conta conta(X dto, Function<X, Conta> contaFinder) {
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
		return Optional.ofNullable(lancamento).map(Lancamento::getConta).map(c -> c.getNome()).orElse(null);
	}

//	private ContaDTO conta(Lancamento lancamento) {
//		return Optional.ofNullable(lancamento).map(Lancamento::getConta)
//				.map(c -> new ContaDTO(c.getId(), c.getNome(),
//						c instanceof ContaDinheiro ? ContaTipo.CONTA_DINHEIRO.getValue()
//								: c instanceof ContaBanco ? ContaTipo.CONTA_BANCO.getValue()
//										: ContaTipo.CARTAO_CREDITO.getValue()))
//				.orElse(null);
//	}

	private BigDecimal saldoInicial(Conta c) {
		return Optional.ofNullable(c).filter(ContaCorrente.class::isInstance).map(ContaCorrente.class::cast)
				.map(ContaCorrente::getSaldoInicial).orElse(null);
	}

	private String nomeCategoria(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).map(Lancamento::getCategoria).map(c -> c.getNome()).orElse(null);
	}

	private CategoriaDTO categoria(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).map(Lancamento::getCategoria)
				.map(c -> new CategoriaDTO(c.getId(), c.getNome())).orElse(null);
	}

	private String nomeContaDestino(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).filter(LancamentoContaCorrente.class::isInstance)
				.map(LancamentoContaCorrente.class::cast).map(LancamentoContaCorrente::getContaDestino)
				.map(Conta::getNome).orElse(null);
	}

	private String nomeContaOrigem(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).filter(LancamentoContaCorrente.class::isInstance)
				.map(LancamentoContaCorrente.class::cast).map(LancamentoContaCorrente::getContaOrigem)
				.map(Conta::getNome).orElse(null);
	}

	private String nomeCartaoCreditoFatura(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).filter(LancamentoContaCorrente.class::isInstance)
				.map(LancamentoContaCorrente.class::cast).map(LancamentoContaCorrente::getCartaoCreditoFatura)
				.map(Conta::getNome).orElse(null);
	}

}