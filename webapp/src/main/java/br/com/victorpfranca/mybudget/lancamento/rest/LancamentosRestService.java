package br.com.victorpfranca.mybudget.lancamento.rest;

import java.util.Optional;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.groups.Default;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.lancamento.AtualizacaoLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.AtualizacaoSerieLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.CadastroLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.ConsultaLancamentos;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.rules.CategoriasIncompativeisException;
import br.com.victorpfranca.mybudget.lancamento.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.lancamento.rules.DataSerieLancamentoInvalidaException;
import br.com.victorpfranca.mybudget.lancamento.rules.LancamentoService;
import br.com.victorpfranca.mybudget.lancamento.rules.MesLancamentoAlteradoException;
import br.com.victorpfranca.mybudget.lancamento.rules.TipoContaException;
import br.com.victorpfranca.mybudget.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.victorpfranca.mybudget.view.AnoMes;

@Stateless
@LocalBean
public class LancamentosRestService {

	@Inject
	private ConsultaLancamentos consultaLancamentos;
	@Inject
	private LancamentoService lancamentoService;
	@Inject
	private EntityManager em;

	public void cadastrar(CadastroLancamentoDTO cadastroLancamento) {
		Class<?>[] groups = { Valid.class, Default.class };
		Set<ConstraintViolation<CadastroLancamentoDTO>> violations = Validation.buildDefaultValidatorFactory()
				.getValidator().validate(cadastroLancamento, groups);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional.ofNullable(cadastroLancamento).map(
				dto -> new ConversorLancamentoParaLancamentoDTO().converter(dto, this::findCategoria, this::findConta))
				.ifPresent(this::processarCadastro);
	}

	private void processarCadastro(Lancamento lancamento) {
		try {
			if (lancamento.getSerie() != null) {
				lancamentoService.saveSerie(lancamento);
			} else {
				lancamentoService.save(lancamento);
			}
		} catch (DataSerieLancamentoInvalidaException | ContaNotNullException | CategoriasIncompativeisException
				| MesLancamentoAlteradoException | TipoContaException | ValorLancamentoInvalidoException e) {
			throw new WebApplicationException(e.getMessage(), e,
					Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
		}
	}

	public void atualizar(Integer id, AnoMes anoMes, AtualizacaoLancamentoDTO atualizacaoLancamento) {
		Optional.ofNullable(consultaLancamentos.recuperarLancamento(id, anoMes))
				.map(this::validaSePodeAlterarLancamento).ifPresent(lancamento -> {
					new ConversorLancamentoParaLancamentoDTO().aplicarValores(lancamento, atualizacaoLancamento,
							this::findCategoria, this::findConta);
					try {
						lancamentoService.save(lancamento);
					} catch (ContaNotNullException | CategoriasIncompativeisException | MesLancamentoAlteradoException
							| TipoContaException | ValorLancamentoInvalidoException e) {
						throw new WebApplicationException(e.getMessage(), e,
								Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
					}
				});
	}

	private Account findConta(Integer contaId) {
		return em.find(Account.class, contaId);
	}

	private Category findCategoria(Integer categoriaId) {
		return em.find(Category.class, categoriaId);
	}

	private Lancamento validaSeExisteSerie(Lancamento lancamento) {
		if (lancamento.getSerie() == null) {
			throw new WebApplicationException(Response.status(422).type(MediaType.TEXT_PLAIN)
					.entity("crud.lancamento.error.editar.serie.null").build());
		}
		return lancamento;
	}

	private Lancamento validaSePodeAlterarLancamento(Lancamento lancamento) {
		if (lancamento instanceof LancamentoContaCorrente) {
			LancamentoContaCorrente lancamentoContaCorrente = (LancamentoContaCorrente) lancamento;
			if (lancamentoContaCorrente.isFaturaCartao() || lancamentoContaCorrente.isTransferencia()) {
				throw new WebApplicationException(Response.status(422).type(MediaType.TEXT_PLAIN)
						.entity("crud.lancamento.error.editar.faturaCartaoOuTransferencia").build());
			}
		}
		return lancamento;
	}

	public void remover(Integer id, AnoMes anoMes) {
		Optional.ofNullable(consultaLancamentos.recuperarLancamento(id, anoMes)).ifPresent(lancamentoService::remove);
	}

	public void atualizarSerie(Integer id, AnoMes anoMes, AtualizacaoSerieLancamentoDTO atualizacao) {
		Optional.ofNullable(consultaLancamentos.recuperarLancamento(id, anoMes)).map(this::validaSeExisteSerie)
				.map(this::validaSePodeAlterarLancamento).ifPresent(lancamento -> {
					new ConversorLancamentoParaLancamentoDTO().aplicarValores(lancamento, atualizacao,
							this::findCategoria, this::findConta);
					try {
						lancamentoService.saveSerie(lancamento);
					} catch (ContaNotNullException | CategoriasIncompativeisException | MesLancamentoAlteradoException
							| TipoContaException | ValorLancamentoInvalidoException
							| DataSerieLancamentoInvalidaException e) {
						throw new WebApplicationException(e.getMessage(), e,
								Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
					}
				});
	}

	public void removerSerie(Integer id, AnoMes anoMes) {
		Optional.ofNullable(consultaLancamentos.recuperarLancamento(id, anoMes)).map(this::validaSeExisteSerie)
				.map(Lancamento::getSerie).ifPresent(lancamentoService::removeSerie);
	}

}
