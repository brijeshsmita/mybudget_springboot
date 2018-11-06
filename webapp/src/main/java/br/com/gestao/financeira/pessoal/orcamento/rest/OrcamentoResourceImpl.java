package br.com.gestao.financeira.pessoal.orcamento.rest;

import static br.com.gestao.financeira.pessoal.infra.LambdaUtils.compose;
import static br.com.gestao.financeira.pessoal.infra.LambdaUtils.nullSafeConvert;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.infra.date.DateUtils;
import br.com.gestao.financeira.pessoal.orcamento.ConsultaOrcamentos;
import br.com.gestao.financeira.pessoal.orcamento.OrcadoRealDTO;
import br.com.gestao.financeira.pessoal.orcamento.OrcadoRealMesCategoria;
import br.com.gestao.financeira.pessoal.orcamento.OrcamentoResource;
import br.com.gestao.financeira.pessoal.view.AnoMes;

public class OrcamentoResourceImpl implements OrcamentoResource {

	@Inject
	private ConsultaOrcamentos consultaOrcamentos;
	private Integer ano;
	private Integer mes;

	public OrcamentoResourceImpl ano(Integer ano) {
		this.ano = ano;
		return this;
	}

	public OrcamentoResourceImpl mes(Integer mes) {
		this.mes = mes;
		return this;
	}

	@Override
	public List<OrcadoRealDTO> receitasReaisOrcadas() {
		AnoMes anoMes = new AnoMes(ano, mes);
		return consultaOrcamentos.recuperarReceitasPorCategoriaOrcada(anoMes).stream().map(conversorOrcadoReal(anoMes))
				.collect(Collectors.toList());
	}

	@Override
	public List<OrcadoRealDTO> despesasReaisOrcadas() {
		AnoMes anoMes = new AnoMes(ano, mes);
		return consultaOrcamentos.recuperarDespesasPorCategoriaOrcada(anoMes).stream().map(conversorOrcadoReal(anoMes))
				.collect(Collectors.toList());
	}

	private Function<OrcadoRealMesCategoria, OrcadoRealDTO> conversorOrcadoReal(AnoMes anoMes) {
		return orcadoRealMesCategoria -> converter(orcadoRealMesCategoria, anoMes);
	}

	private OrcadoRealDTO converter(OrcadoRealMesCategoria ent, AnoMes anoMes) {
		compose(OrcadoRealMesCategoria::getCategoria, Categoria::getNome);
		String categoria = nullSafeConvert(ent, compose(OrcadoRealMesCategoria::getCategoria, Categoria::getNome));
		String data = nullSafeConvert(anoMes,
				compose(AnoMes::getDate, DateUtils::localDateToDate).andThen(DateUtils::iso8601));
		BigDecimal orcado = nullSafeConvert(ent, OrcadoRealMesCategoria::getOrcado);
		BigDecimal realizado = nullSafeConvert(ent, OrcadoRealMesCategoria::getRealizado);
		return new OrcadoRealDTO(categoria, data, orcado, realizado);
	}

}
