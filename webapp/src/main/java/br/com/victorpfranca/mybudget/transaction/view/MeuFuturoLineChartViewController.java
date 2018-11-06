package br.com.victorpfranca.mybudget.transaction.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.periodo.PeriodoPlanejamento;
import br.com.victorpfranca.mybudget.transaction.MonthlyTransactions;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Named
@ViewScoped
public class MeuFuturoLineChartViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PeriodoPlanejamento periodoPlanejamento;
	@Inject
	private MonthlyTransactions monthlyTransactions;

	private LineChartModel lineModel1;

	private List<AccountBalance> saldos;

	private BigDecimal minValue;
	private BigDecimal maxValue;

	private AccountBalance saldoAtual;
	private AccountBalance saldoProximo;
	private AccountBalance menorSaldo;
	private AccountBalance maiorSaldo;
	private AccountBalance ultimoSaldo;

	@PostConstruct
	public void init() {
		carregarSaldoFuturoPrevisto();
		carregarValorMaximoEMinimo();
		carregarSaldosResumo();
		createLineModels();
	}

	private void carregarSaldosResumo() {
		this.saldoAtual = monthlyTransactions.getSaldoAtual();
		this.saldoProximo = monthlyTransactions.getSaldoProximo();
		this.maiorSaldo = monthlyTransactions.getMaiorSaldo();
		this.menorSaldo = monthlyTransactions.getMenorSaldo();
		this.ultimoSaldo = monthlyTransactions.getUltimoSaldo();
	}

	public void carregarSaldoFuturoPrevisto() {
		this.saldos = monthlyTransactions.getSaldos();
	}

	private void carregarValorMaximoEMinimo() {
		minValue = monthlyTransactions.getMinValue();
		maxValue = monthlyTransactions.getMaxValue();
	}

	private void createLineModels() {
		lineModel1 = initLinearModel();
		lineModel1.setLegendPosition("e");
		lineModel1.setShowPointLabels(true);
		lineModel1.getAxes().put(AxisType.X, new CategoryAxis());
		Axis yAxis = lineModel1.getAxis(AxisType.Y);
		yAxis.setMin(minValue.add(minValue.multiply(new BigDecimal(10)).divide(new BigDecimal(100))));
		yAxis.setMax(maxValue.add(maxValue.multiply(new BigDecimal(10)).divide(new BigDecimal(100))));
		lineModel1.setExtender("skinChart");
	}

	private LineChartModel initLinearModel() {
		LineChartModel model = new LineChartModel();

		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Saldo Previsto");

		for (int i = 0; i < saldos.size(); i += 3) {
			AccountBalance accountBalance = saldos.get(i);
			MonthYear monthYear = new MonthYear(accountBalance.getAno(), accountBalance.getMes());
			series1.set(monthYear, accountBalance.getValor());
		}

		model.addSeries(series1);

		return model;
	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	public String getInicio() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(periodoPlanejamento.getMesAtual().getMes());
		stringBuffer.append("/");
		stringBuffer.append(periodoPlanejamento.getMesAtual().getAno());
		return stringBuffer.toString();
	}

	public String getFim() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(periodoPlanejamento.getMesFinal().getMes());
		stringBuffer.append("/");
		stringBuffer.append(periodoPlanejamento.getMesFinal().getAno());
		return stringBuffer.toString();
	}

	public AccountBalance getSaldoAtual() {
		return saldoAtual;
	}

	public AccountBalance getSaldoProximo() {
		return saldoProximo;
	}

	public AccountBalance getMenorSaldo() {
		return menorSaldo;
	}

	public AccountBalance getMaiorSaldo() {
		return maiorSaldo;
	}

	public AccountBalance getUltimoSaldo() {
		return ultimoSaldo;
	}

}
