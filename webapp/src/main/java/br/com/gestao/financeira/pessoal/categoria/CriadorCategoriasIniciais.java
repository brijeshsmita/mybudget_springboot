package br.com.gestao.financeira.pessoal.categoria;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.ObjectUtils;

import br.com.gestao.financeira.pessoal.InOut;

@Stateless
public class CriadorCategoriasIniciais {

	@EJB
	private CategoriaService categoriaService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute() throws MesmoNomeExistenteException {
		List<Categoria> categorias = new ArrayList<Categoria>();

		InputStream is = ObjectUtils.firstNonNull(
				CriadorCategoriasIniciais.class.getResourceAsStream("categorias_iniciais_despesas"),
				CriadorCategoriasIniciais.class.getResourceAsStream("/categorias_iniciais_despesas"));

		if (is == null)
			return;
		try {
			saveCategorias(categorias, is, InOut.S);
		} catch (MesmoNomeExistenteException e) {
			e.printStackTrace();
		}

		categorias = new ArrayList<Categoria>();
		is = ObjectUtils.firstNonNull(
				CriadorCategoriasIniciais.class.getResourceAsStream("categorias_iniciais_receitas"),
				CriadorCategoriasIniciais.class.getResourceAsStream("/categorias_iniciais_receitas"));
		saveCategorias(categorias, is, InOut.E);
	}

	private void saveCategorias(List<Categoria> categorias, InputStream is, InOut inOut)
			throws MesmoNomeExistenteException {
		Scanner scanner = new Scanner(is, "UTF-8");
		while (scanner.hasNextLine()) {
			categorias.add(new Categoria(scanner.nextLine(), inOut));
		}
		categoriaService.saveCategorias(categorias);
		scanner.close();
	}

}
