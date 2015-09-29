package org.cosmodict.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the TOKEN database table.
 * 
 */
@Entity
@Table(name = "TOKEN")
@NamedQuery(name = "Token.findAll", query = "SELECT t FROM Token t")
public class Token implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "token_id")
	private Integer tokenId;

	@Column(name = "value")
	private String value;

	//bi-directional many-to-one association to Definition
	//@OneToMany(mappedBy = "token", fetch = FetchType.LAZY)
	@Transient
	private List<Definition> definitions;

	public Token() {
	}

	public Token(Integer id, String value) {
		this.tokenId = id;
		this.value = value;
	}

	public Integer getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Definition> getDefinitions() {
		return this.definitions;
	}

	public void setDefinitions(List<Definition> definitions) {
		this.definitions = definitions;
	}

	public Definition addDefinition(Definition definition) {
		getDefinitions().add(definition);
		definition.setToken(this);

		return definition;
	}

	public Definition removeDefinition(Definition definition) {
		getDefinitions().remove(definition);
		definition.setToken(null);

		return definition;
	}

}