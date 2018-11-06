package br.com.gestao.financeira.pessoal.controleacesso.recuperasenha;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario_;
import br.com.gestao.financeira.pessoal.infra.date.DateUtils;
import br.com.gestao.financeira.pessoal.infra.date.api.CurrentDateSupplier;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RecuperacaoSenhaConsultas {

    @Inject
    private EntityManager entityManager;
    
    @EJB
    private CurrentDateSupplier dateUtils;

    public boolean confirmaValidadeDoCodigo(String email, String codigo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> critQuery = cb.createQuery(Long.class);

        Root<RecuperacaoSenha> recSenha = critQuery.from(RecuperacaoSenha.class);
        Join<?, Usuario> usuario = recSenha.join(RecuperacaoSenha_.alvo, JoinType.INNER);

        Predicate usuarioIgual = cb.equal(usuario.get(Usuario_.email), StringUtils.lowerCase(email));
        Predicate codigoIgual = cb.equal(recSenha.get(RecuperacaoSenha_.codigo), codigo);
        Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(RecuperacaoSenha_.ativo));
        Predicate dataSolicitacaoValida = filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha());

        critQuery.select(cb.count(recSenha.get(RecuperacaoSenha_.id))).where(usuarioIgual, codigoIgual, recuperacaoAtiva,
                dataSolicitacaoValida);

        return entityManager.createQuery(critQuery).getSingleResult() > 0;
    }

    public Pair<Date, Date> periodoValidoParaRecuperacaoDeSenha() {
        LocalDateTime dataFim = dateUtils.currentLocalDateTime();
        Date inicioPeriodoValidoSolicitacao = DateUtils.localDateTimeToDate(dataFim.minusMinutes(45));
        Date fimPeriodoValidoSolicitacao = DateUtils.localDateTimeToDate(dataFim);
        return new ImmutablePair<>(inicioPeriodoValidoSolicitacao, fimPeriodoValidoSolicitacao);
    }

    public RecuperacaoSenha ultimaRecuperacaoValidaPara(Usuario usuario) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecuperacaoSenha> critQuery = cb.createQuery(RecuperacaoSenha.class);

        Root<RecuperacaoSenha> recSenha = critQuery.from(RecuperacaoSenha.class);

        Predicate usuarioIgual = cb.equal(recSenha.get(RecuperacaoSenha_.alvo), usuario);
        Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(RecuperacaoSenha_.ativo));
        Predicate dataSolicitacaoValida = filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha());

        critQuery.select(recSenha).where(usuarioIgual, recuperacaoAtiva, dataSolicitacaoValida);

        return entityManager.createQuery(critQuery).getSingleResult();
    }

    public RecuperacaoSenha ultimaRecuperacaoValidaPara(String email, String codigo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecuperacaoSenha> critQuery = cb.createQuery(RecuperacaoSenha.class);

        Root<RecuperacaoSenha> recSenha = critQuery.from(RecuperacaoSenha.class);
        Join<?, Usuario> usuario = recSenha.join(RecuperacaoSenha_.alvo, JoinType.INNER);

        Predicate usuarioIgual = cb.equal(usuario.get(Usuario_.email), StringUtils.lowerCase(email));
        Predicate codigoIgual = cb.equal(recSenha.get(RecuperacaoSenha_.codigo), codigo);
        Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(RecuperacaoSenha_.ativo));
        Predicate dataSolicitacaoValida = filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha());

        critQuery.select(recSenha).where(usuarioIgual, codigoIgual, recuperacaoAtiva, dataSolicitacaoValida);
        try {
            return entityManager.createQuery(critQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<RecuperacaoSenha> listarAtivasComDataExpirada() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecuperacaoSenha> critQuery = cb.createQuery(RecuperacaoSenha.class);

        Root<RecuperacaoSenha> recSenha = critQuery.from(RecuperacaoSenha.class);
        Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(RecuperacaoSenha_.ativo));

        Predicate dataSolicitacaoInvalida = cb
                .not(filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha()));
        critQuery.select(recSenha).where(recuperacaoAtiva, dataSolicitacaoInvalida);

        return entityManager.createQuery(critQuery).getResultList();
    }

    public List<RecuperacaoSenha> listarAtivasDeUsuario(Usuario usuario) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RecuperacaoSenha> critQuery = cb.createQuery(RecuperacaoSenha.class);

        Root<RecuperacaoSenha> recSenha = critQuery.from(RecuperacaoSenha.class);

        Predicate usuarioIgual = cb.equal(recSenha.get(RecuperacaoSenha_.alvo), usuario);
        Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(RecuperacaoSenha_.ativo));
        Predicate dataSolicitacaoValida = filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha());
        critQuery.select(recSenha).where(usuarioIgual, recuperacaoAtiva, dataSolicitacaoValida);
        return entityManager.createQuery(critQuery).getResultList();
    }

    private Predicate filtroDeDataValida(CriteriaBuilder cb, Root<RecuperacaoSenha> recSenha,
            Pair<Date, Date> periodoValido) {
        Predicate dataSolicitacaoValida = cb.between(recSenha.get(RecuperacaoSenha_.dataSolicitacao),
                periodoValido.getLeft(), periodoValido.getRight());
        return dataSolicitacaoValida;
    }

}
