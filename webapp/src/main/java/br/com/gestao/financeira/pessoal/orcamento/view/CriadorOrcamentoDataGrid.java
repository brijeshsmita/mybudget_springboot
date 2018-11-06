package br.com.gestao.financeira.pessoal.orcamento.view;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.apache.logging.log4j.core.appender.SyslogAppender;

import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.orcamento.Orcamento;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Named
public class CriadorOrcamentoDataGrid implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Categoria, Map<AnoMes, Orcamento>> orcamentosGridData;

	protected List<Categoria> categorias;

	private List<AnoMes> anosMeses;

	private List<Orcamento> orcamentos;

	public Map<Categoria, Map<AnoMes, Orcamento>> criar(List<Categoria> categorias, List<AnoMes> anosMeses,
			List<Orcamento> orcamentos) {

		this.categorias = categorias;
		this.anosMeses = anosMeses;
		this.orcamentos = orcamentos;

		this.orcamentosGridData = iniciarMapsCategorias();

		preencherOrcamentosExistentes();

		iniciarMesesSemOrcamento();

		return orcamentosGridData;
	}

	private void iniciarMesesSemOrcamento() {
		// Preencher orcamentosGridData com meses zerados
		for (Map.Entry<Categoria, Map<AnoMes, Orcamento>> categoriaEntry : orcamentosGridData.entrySet()) {
			Map<AnoMes, Orcamento> categoriaMap = ((Map<AnoMes, Orcamento>) categoriaEntry.getValue());
			for (Iterator<AnoMes> iterator = anosMeses.iterator(); iterator.hasNext();) {
				AnoMes anoMes = iterator.next();
				if (categoriaMap.get(anoMes) == null) {
					Orcamento orcamento = new Orcamento();
					orcamento.setCategoria(categoriaEntry.getKey());
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

	private Map<Categoria, Map<AnoMes, Orcamento>> iniciarMapsCategorias() {
		// inicia valores lista de categorias com mapas vazios
		Map<Categoria, Map<AnoMes, Orcamento>> orcamentosGridData = new LinkedHashMap<Categoria, Map<AnoMes, Orcamento>>();
		for (Iterator<Categoria> iterator = categorias.iterator(); iterator.hasNext();) {
			Categoria categoria = (Categoria) iterator.next();
			orcamentosGridData.put(categoria, new LinkedHashMap<AnoMes, Orcamento>());
		}
		return orcamentosGridData;
	}

	public List<AnoMes> getAnosMeses() {
		return anosMeses;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public Map<Categoria, Map<AnoMes, Orcamento>> getOrcamentosGridData() {
		return orcamentosGridData;
	}

}
