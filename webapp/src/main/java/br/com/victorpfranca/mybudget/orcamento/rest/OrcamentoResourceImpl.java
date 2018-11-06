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
import br.com.victorpfranca.mybudget.view.MonthYear;

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
		MonthYear monthYear = new MonthYear(ano, mes);
		return consultaOrcamentos.recuperarReceitasPorCategoriaOrcada(monthYear).stream().map(conversorOrcadoReal(monthYear))
				.collect(Collectors.toList());
	}

	@Override
	public List<OrcadoRealDTO> despesasReaisOrcadas() {
		MonthYear monthYear = new MonthYear(ano, mes);
		return consultaOrcamentos.recuperarDespesasPorCategoriaOrcada(monthYear).stream().map(conversorOrcadoReal(monthYear))
				.collect(Collectors.toList());
	}

	private Function<OrcadoRealMesCategoria, OrcadoRealDTO> conversorOrcadoReal(MonthYear monthYear) {
		return orcadoRealMesCategoria -> converter(orcadoRealMesCategoria, monthYear);
	}

	private OrcadoRealDTO converter(OrcadoRealMesCategoria ent, MonthYear monthYear) {
		compose(OrcadoRealMesCategoria::getCategoria, Category::getNome);
		String categoria = nullSafeConvert(ent, compose(OrcadoRealMesCategoria::getCategoria, Category::getNome));
		String data = nullSafeConvert(monthYear,
				compose(MonthYear::getDate, DateUtils::localDateToDate).andThen(DateUtils::iso8601));
		BigDecimal orcado = nullSafeConvert(ent, OrcadoRealMesCategoria::getOrcado);
		BigDecimal realizado = nullSafeConvert(ent, OrcadoRealMesCategoria::getRealizado);
		return new OrcadoRealDTO(categoria, data, orcado, realizado);
	}

}
