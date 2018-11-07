package br.com.victorpfranca.mybudget.category.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.category.SameNameException;
import br.com.victorpfranca.mybudget.transaction.rules.DeletionNotPermittedException;
import br.com.victorpfranca.mybudget.view.FacesMessages;
import br.com.victorpfranca.mybudget.view.Messages;

@Named
@ViewScoped
public class CategoryViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaService categoriaService;

	private int selectedTab;
	private Category objeto;
	private boolean telaGrid = true;

	private List<Category> receitas;
	private List<Category> despesas;

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
		Category category = new Category();
		category.setInOut(InOut.E);
		setObjeto(category);
	}

	public void incluirDespesa() {
		setSelectedTab(0);
		setTelaGrid(false);
		Category category = new Category();
		category.setInOut(InOut.S);
		setObjeto(category);
	}

	public void alterar(Category category) {
		setSelectedTab(0);
		setTelaGrid(false);
		setObjeto(category);
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

	public Category getObjeto() {
		return objeto;
	}

	public void setObjeto(Category objeto) {
		this.objeto = objeto;
	}

	public void excluir(Category category) {
		try {
			categoriaService.remove(category);
			initCategorias();
		} catch (DeletionNotPermittedException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
		}
	}

	public void salvar() {
		try {
			setObjeto(categoriaService.save(getObjeto()));
		} catch (SameNameException e) {
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

	public List<Category> getReceitas() {
		return receitas;
	}

	public List<Category> getDespesas() {
		return despesas;
	}

}
