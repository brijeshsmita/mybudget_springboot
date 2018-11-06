package br.com.victorpfranca.mybudget.orcamento.rest;

import static br.com.victorpfranca.mybudget.infra.LambdaUtils.compose;
import static br.com.victorpfranca.mybudget.infra.LambdaUtils.nullSafeConvert;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.orcamento.ConsultaOrcamentos;
import br.com.victorpfranca.mybudget.orcamento.OrcadoRealDTO;
import br.com.victorpfranca.mybudget.orcamento.OrcadoRealMesCategoria;
import br.com.victorpfranca.mybudget.orcamento.OrcamentoResource;
import br.com.victorpfranca.mybudget.view.AnoMes;

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
		compose(OrcadoRealMesCategoria::getCategoria, Category::getNome);
		String categoria = nullSafeConvert(ent, compose(OrcadoRealMesCategoria::getCategoria, Category::getNome));
		String data = nullSafeConvert(anoMes,
				compose(AnoMes::getDate, DateUtils::localDateToDate).andThen(DateUtils::iso8601));
		BigDecimal orcado = nullSafeConvert(ent, OrcadoRealMesCategoria::getOrcado);
		BigDecimal realizado = nullSafeConvert(ent, OrcadoRealMesCategoria::getRealizado);
		return new OrcadoRealDTO(categoria, data, orcado, realizado);
	}

}
