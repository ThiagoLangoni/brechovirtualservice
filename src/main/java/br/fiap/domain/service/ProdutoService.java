package br.fiap.domain.service;

import java.io.Serializable;
import java.util.List;

import br.fiap.domain.entity.Produto;
import br.fiap.infrastructure.ProdutoServiceException;

public interface ProdutoService extends Serializable {

	List<Produto> pesquisar(String descricao) throws ProdutoServiceException;
	
	Produto incluir(Produto produto) throws ProdutoServiceException;
	
	Produto localizarPorChave(Long id) throws ProdutoServiceException;
	
	Produto atualizar(Long id, Produto produto) throws ProdutoServiceException;
	
	void excluir(Long id) throws ProdutoServiceException;
	
}
