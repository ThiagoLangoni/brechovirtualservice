package br.fiap.application;

import static br.fiap.infrastructure.ProdutoServiceException.OPERACACAO_INCLUSAO_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.OPERACAO_ATUALIZACAO_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.OPERACAO_BUSCA_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.OPERACAO_EXCLUSAO_ERROR;
import static br.fiap.infrastructure.ProdutoServiceException.PRODUTO_NAO_LOCALIZADO_ERROR;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.fiap.domain.entity.Produto;
import br.fiap.domain.entity.ProdutoResponse;
import br.fiap.domain.service.ProdutoService;
import br.fiap.infrastructure.ProdutoServiceException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/produtos")
@Slf4j
public class ProdutoApplication {

	private final ProdutoService servico;

	@Autowired
	public ProdutoApplication(ProdutoService servico) {
		this.servico = servico;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.OK)
	public ProdutoResponse pesquisar(@RequestParam(required = false) String descricao) {
		log.info("Processando a requisição de pesquisa");
		try {
			ProdutoResponse resposta = new ProdutoResponse();
			
			List<Produto> produtos = servico.pesquisar(descricao);
			resposta.setProdutos(produtos);
			
			return resposta;
		} catch (ProdutoServiceException e) {
			log.error("Error processing search request", e);
			throw exceptionHandler(e);
		}
	}

	private ResponseStatusException exceptionHandler(ProdutoServiceException e) {
		if (e.getOperation() == null || e.getOperation().isEmpty()) {
			return new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (OPERACAO_BUSCA_ERROR.equals(e.getOperation()) || OPERACACAO_INCLUSAO_ERROR.equals(e.getOperation())
				|| OPERACAO_BUSCA_ERROR.equals(e.getOperation()) || OPERACAO_ATUALIZACAO_ERROR.equals(e.getOperation())
				|| OPERACAO_EXCLUSAO_ERROR.equals(e.getOperation())) {
			return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (PRODUTO_NAO_LOCALIZADO_ERROR.equals(e.getOperation())) {
			return new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<Void> incluir(@RequestBody Produto produto) throws ProdutoServiceException {
		log.info("Processando resquest de inclusão");
		try {
			Produto result = servico.incluir(produto);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
					.toUri();
			return ResponseEntity.created(location).build();
		} catch (ProdutoServiceException e) {
			log.error("Erro ao processar o request de inclusão", e);
			throw exceptionHandler(e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Produto localizarPorChave(@PathVariable Long id) throws ProdutoServiceException {
		log.info("Processando request de localização por Chave");
		try {
			return servico.localizarPorChave(id);
		} catch (ProdutoServiceException e) {
			log.error("Erro ao processar request de localizar produto por chave", e);
			throw exceptionHandler(e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Produto atualizar(@PathVariable Long id, @RequestBody Produto product) throws ProdutoServiceException {
		log.info("Processando resquest de atualização");
		try {
			return servico.atualizar(id, product);
		} catch (ProdutoServiceException e) {
			log.error("Erro ao processar request de atualizacao", e);
			throw exceptionHandler(e);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public void delete(@PathVariable Long id) throws ProdutoServiceException {
		log.info("Processando resquest de exclusão");
		try {
			servico.excluir(id);
		} catch (ProdutoServiceException e) {
			log.error("Erro ao processar request de exclusão", e);
			throw exceptionHandler(e);
		}
	}

}
