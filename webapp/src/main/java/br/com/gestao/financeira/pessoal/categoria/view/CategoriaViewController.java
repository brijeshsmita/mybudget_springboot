package br.com.gestao.financeira.pessoal.categoria.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.categoria.CategoriaService;
import br.com.gestao.financeira.pessoal.categoria.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;

@Named
@ViewScoped
public class CategoriaViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaService categoriaService;

	private int selectedTab;
	private Categoria objeto;
	private boolean telaGrid = true;

	private List<Categoria> receitas;
	private List<Categoria> despesas;

	@PostConstruct
	public void init() {
		setSelectedTab(0);
		initCategorias();
	}

	private void initCategorias() {
		this.receitas = categoriaService.findReceitas();
		this.despesas = categoriaService.findDespesas();
	}

	public void incluirReceita() {
		setSelectedTab(0);
		setTelaGrid(false);
		Categoria categoria = new Categoria();
		categoria.setInOut(InOut.E);
		setObjeto(categoria);
	}

	public void incluirDespesa() {
		setSelectedTab(0);
		setTelaGrid(false);
		Categoria categoria = new Categoria();
		categoria.setInOut(InOut.S);
		setObjeto(categoria);
	}

	public void alterar(Categoria categoria) {
		setSelectedTab(0);
		setTelaGrid(false);
		setObjeto(categoria);
	}

	public void voltar() {
		setTelaGrid(true);
		setSelectedTab(0);
		setObjeto(null);
	}

	public boolean isTelaGrid() {
		return telaGrid;
	}

	public void setTelaGrid(boolean telaGrid) {
		this.telaGrid = telaGrid;
	}

	public Categoria getObjeto() {
		return objeto;
	}

	public void setObjeto(Categoria objeto) {
		this.objeto = objeto;
	}

	public void excluir(Categoria categoria) {
		try {
			categoriaService.remove(categoria);
			initCategorias();
		} catch (RemocaoNaoPermitidaException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
		}
	}

	public void salvar() {
		try {
			setObjeto(categoriaService.save(getObjeto()));
		} catch (MesmoNomeExistenteException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
			return;
		}
		initCategorias();
		voltar();
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

	public List<Categoria> getReceitas() {
		return receitas;
	}

	public List<Categoria> getDespesas() {
		return despesas;
	}

}
