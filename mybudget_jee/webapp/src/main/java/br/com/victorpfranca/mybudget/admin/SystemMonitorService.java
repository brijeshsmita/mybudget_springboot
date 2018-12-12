package br.com.victorpfranca.mybudget.admin;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.infra.date.api.CurrentDateSupplier;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SystemMonitorService {

	@Inject
	private EntityManager em;

	@EJB
	private CurrentDateSupplier date;

	private Date yesterday;
	private Date today;
	private Date tomorrow;

	@PostConstruct
	public void init() {
		yesterday = DateUtils.localDateTimeToDate(date.currentLocalDate().minusDays(1).atStartOfDay());
		today = DateUtils.localDateToDate(date.currentLocalDate());
		tomorrow = DateUtils.localDateTimeToDate(date.currentLocalDate().plusDays(1).atStartOfDay());
	}

	public int getQuantidadeNovosUsuariosDiaAtual() {
		return em
				.createQuery("select count(u) from User u where dataCadastro >=:today and dataCadastro < :tomorrow",
						Long.class)
				.setParameter("today", today).setParameter("tomorrow", tomorrow).getSingleResult().intValue();
	}

	public int getQuantidadeNovosUsuariosDiaAnterior() {
		return em
				.createQuery(
						"select count(u) from User u where dataCadastro >= :yesterday and dataCadastro < :today",
						Long.class)
				.setParameter("yesterday", yesterday).setParameter("today", today).getSingleResult().intValue();
	}

	public int getQuantidadeNovosUsuariosUltimosDias(int qtdDias) {
		Date dayOne = LocalDateConverter.toDate(LocalDateConverter.fromDate(date.currentDate()).minusDays(qtdDias - 1));

		return em
				.createQuery(
						"select count(u) from User u where dataCadastro >= :dayOne and dataCadastro < :tomorrow",
						Long.class)
				.setParameter("dayOne", dayOne).setParameter("tomorrow", tomorrow).getSingleResult().intValue();
	}

	public int getQuantidadeAcessosDiaAtual() {
		return em.createQuery("select count(l) from LogAcesso l where data >=:today and data < :tomorrow", Long.class)
				.setParameter("today", today).setParameter("tomorrow", tomorrow).getSingleResult().intValue();
	}

	public int getQuantidadeAcessosDiaAnterior() {
		return em.createQuery("select count(l) from LogAcesso l where data >= :yesterday and data < :today", Long.class)
				.setParameter("yesterday", yesterday).setParameter("today", today).getSingleResult().intValue();
	}

	public int getQuantidadeAcessosUltimosDias(int qtdDias) {
		Date dayOne = LocalDateConverter.toDate(LocalDateConverter.fromDate(date.currentDate()).minusDays(qtdDias - 1));

		return em.createQuery("select count(l) from LogAcesso l where data >= :dayOne and data < :tomorrow", Long.class)
				.setParameter("dayOne", dayOne).setParameter("tomorrow", tomorrow).getSingleResult().intValue();
	}

}
