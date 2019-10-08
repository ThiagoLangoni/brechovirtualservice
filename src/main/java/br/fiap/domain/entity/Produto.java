package br.fiap.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Produto")
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
	private String nome;
    
	private String descricao;
	
	private String imagem;
   
	private Double preco;
	
}
