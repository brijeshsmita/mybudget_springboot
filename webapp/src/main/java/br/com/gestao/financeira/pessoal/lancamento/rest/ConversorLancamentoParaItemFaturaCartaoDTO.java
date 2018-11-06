package br.com.gestao.financeira.pessoal.lancamento.rest;

import java.util.Optional;

import br.com.gestao.financeira.pessoal.infra.date.DateUtils;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoFaturaCartaoItem;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoItemFaturaCartaoDTO;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoStatus;

public class ConversorLancamentoParaItemFaturaCartaoDTO {

	public LancamentoItemFaturaCartaoDTO converter(Lancamento lancamento) {
		LancamentoItemFaturaCartaoDTO dto = new LancamentoItemFaturaCartaoDTO();
		dto.setId(lancamento.getId());
		dto.setStatus(status(lancamento));
		dto.setDataCompra(dataIso8601(lancamento));
		dto.setConta(nomeConta(lancamento));
		dto.setCategoria(nomeCategoria(lancamento));
		dto.setComentario(lancamento.getComentario());
		dto.setValorCompra(((LancamentoFaturaCartaoItem)lancamento).getLancamentoCartao().getValor());
		dto.setValorParcela(lancamento.getValor());
		dto.setQtdParcelas(((LancamentoFaturaCartaoItem)lancamento).getQtdParcelas());
		dto.setIndiceParcelas(((LancamentoFaturaCartaoItem)lancamento).getIndiceParcela());
		dto.setSaldo(lancamento.getSaldo());
		return dto;
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

	private String nomeCategoria(Lancamento lancamento) {
		return Optional.ofNullable(lancamento).map(Lancamento::getCategoria).map(c -> c.getNome()).orElse(null);
	}

}