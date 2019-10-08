package br.fiap.domain.service;

import static br.fiap.infrastructure.ProdutoServiceException.OPERACACAO_INCLUSAO_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.OPERACAO_ATUALIZACAO_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.OPERACAO_BUSCA_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.OPERACAO_EXCLUSAO_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.OPERACAO_PROCURA_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.PARAMETRO_INVALIDO_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.PRODUTO_NAO_LOCALIZADO_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.fiap.application.ProdutoApplication;
import br.fiap.domain.entity.Produto;
import br.fiap.domain.repository.ProdutoRepository;
import br.fiap.infrastructure.ProdutoServiceException;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class ProdutoServiceImpl implements ProdutoService {

	private static final Logger log = LoggerFactory.getLogger(ProdutoApplication.class);
	
	private final ProdutoRepository repositorio;
			
	@Autowired
    public ProdutoServiceImpl(ProdutoRepository repositorio) {
        this.repositorio = repositorio;
    }
	
	@Override
    public List<Produto> pesquisar(String descricao) throws ProdutoServiceException {
        log.info("Pesquisando produtos com a descrição={}", descricao);
        try {
            List<Produto> resultado = new ArrayList<>();

            if (descricao == null || descricao.isEmpty()) {
                log.debug("Sem descricao especificada, listando todos produtos");
                resultado.addAll(repositorio.findAll());
            } else {
                log.debug("Finding products by name or description");
                resultado.addAll(repositorio.findByNomeOrDescricaoAllIgnoreCase(descricao, descricao));
            }

            return resultado;
        } catch (Exception e) {
            log.error("Error searching product", e);
            throw new ProdutoServiceException(OPERACAO_PROCURA_ERROR, e);
        }
    }

    @Override
    public Produto incluir(Produto produto) throws ProdutoServiceException {
        log.info("Incluindo produto ({})", produto);
        try {
            if (produto == null) {
                log.error("Produto inválido");
                throw new ProdutoServiceException(PARAMETRO_INVALIDO_ERROR, null);
            }
            Produto resultado = repositorio.save(produto);
            return resultado;
        } catch (Exception e) {
            log.error("Erro ao criar produto", e);
            throw new ProdutoServiceException(OPERACACAO_INCLUSAO_ERROR, e);
        }
    }

    @Override
    public Produto localizarPorChave(Long id) throws ProdutoServiceException {
        log.info("Recuperando produto por id={}", id);
        try {
            if (id == null) {
                log.error("id inválido");
                throw new ProdutoServiceException(PARAMETRO_INVALIDO_ERROR, null);
            }
            Produto resultado = repositorio.findById(id).get();
            return resultado;
        } catch (Exception e) {
            log.error("Erro ao recuperar id", e);
            throw new ProdutoServiceException(OPERACAO_BUSCA_ERROR, e);
        }
    }

    @Override
    public Produto atualizar(Long id, Produto produto) throws ProdutoServiceException {
        log.info("Atualizando produto ({}) para o id={}", produto, id);
        try {
            if (id == null || produto == null) {
                log.error("Id inavlido ou produto");
                throw new ProdutoServiceException(PARAMETRO_INVALIDO_ERROR, null);
            }
            if (!repositorio.existsById(id)) {
                log.debug("Produto não localizado para o id={}", id);
                throw new ProdutoServiceException(PRODUTO_NAO_LOCALIZADO_ERROR, null);
            }
            Produto resultado = repositorio.save(produto);
            return resultado;
        } catch (Exception e) {
            log.error("Error ao atualizar product", e);
            throw new ProdutoServiceException(OPERACAO_ATUALIZACAO_ERROR, e);
        }
    }

    @Override
    public void excluir(Long id) throws ProdutoServiceException {
        log.info("Excluindo produto para o id={}", id);
        try {
            if (id == null) {
                log.error("Id invalido ou produto");
                throw new ProdutoServiceException(PARAMETRO_INVALIDO_ERROR, null);
            }
            if (!repositorio.existsById(id)) {
                log.debug("Produto não localizado para o id={}", id);
                throw new ProdutoServiceException(PRODUTO_NAO_LOCALIZADO_ERROR, null);
            }
            repositorio.deleteById(id);
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw new ProdutoServiceException(OPERACAO_EXCLUSAO_ERROR, e);
        }
    }
	
}
