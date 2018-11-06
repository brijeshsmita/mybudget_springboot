package br.com.victorpfranca.mybudget.orcamento.view;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.apache.logging.log4j.core.appender.SyslogAppender;

import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.orcamento.Orcamento;
import br.com.victorpfranca.mybudget.view.AnoMes;

@Named
public class CriadorOrcamentoDataGrid implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Category, Map<AnoMes, Orcamento>> orcamentosGridData;

	protected List<Category> categories;

	private List<AnoMes> anosMeses;

	private List<Orcamento> orcamentos;

	public Map<Category, Map<AnoMes, Orcamento>> criar(List<Category> categories, List<AnoMes> anosMeses,
			List<Orcamento> orcamentos) {

		this.categories = categories;
		this.anosMeses = anosMeses;
		this.orcamentos = orcamentos;

		this.orcamentosGridData = iniciarMapsCategorias();

		preencherOrcamentosExistentes();

		iniciarMesesSemOrcamento();

		return orcamentosGridData;
	}

	private void iniciarMesesSemOrcamento() {
		// Preencher orcamentosGridData com meses zerados
		for (Map.Entry<Category, Map<AnoMes, Orcamento>> categoriaEntry : orcamentosGridData.entrySet()) {
			Map<AnoMes, Orcamento> categoriaMap = ((Map<AnoMes, Orcamento>) categoriaEntry.getValue());
			for (Iterator<AnoMes> iterator = anosMeses.iterator(); iterator.hasNext();) {
				AnoMes anoMes = iterator.next();
				if (categoriaMap.get(anoMes) == null) {
					Orcamento orcamento = new Orcamento();
					orcamento.setCategory(categoriaEntry.getKey());
					orcamento.setAno(anoMes.getAno());
					orcamento.setMes(anoMes.getMes());
					categoriaMap.put(anoMes, orcamento);
				}
			}
		}
	}

	private void preencherOrcamentosExistentes() {

		for (Iterator<Orcamento> iterator = orcamentos.iterator(); iterator.hasNext();) {
			Orcamento orcamento = (Orcamento) iterator.next();
			Map<AnoMes, Orcamento> orcamentosCategoria = orcamentosGridData.get(orcamento.getCategoria());

			AnoMes anoMes = new AnoMes(orcamento.getAno(), orcamento.getMes());

			orcamentosCategoria.put(anoMes, orcamento);
			if (!anosMeses.contains(anoMes))
				anosMeses.add(anoMes);
		}
		Collections.sort(anosMeses);
	}

	private Map<Category, Map<AnoMes, Orcamento>> iniciarMapsCategorias() {
		// inicia valores lista de categories com mapas vazios
		Map<Category, Map<AnoMes, Orcamento>> orcamentosGridData = new LinkedHashMap<Category, Map<AnoMes, Orcamento>>();
		for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();) {
			Category category = (Category) iterator.next();
			orcamentosGridData.put(category, new LinkedHashMap<AnoMes, Orcamento>());
		}
		return orcamentosGridData;
	}

	public List<AnoMes> getAnosMeses() {
		return anosMeses;
	}

	public List<Category> getCategorias() {
		return categories;
	}

	public Map<Category, Map<AnoMes, Orcamento>> getOrcamentosGridData() {
		return orcamentosGridData;
	}

}
