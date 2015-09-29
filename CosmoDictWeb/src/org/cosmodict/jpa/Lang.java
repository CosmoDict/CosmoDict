package org.cosmodict.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the LANG database table.
 * 
 */
@Entity
@Table(name = "LANG")
@NamedQuery(name = "Lang.findAll", query = "SELECT l FROM Lang l ORDER BY l.langId")
public class Lang implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "lang_id")
	private String langId;

	private String name;

	private String original;

	private Integer priority;

	public Lang(String langId, String name, String original) {
		this.langId = langId;
		this.name = name;
		this.original = original;
	}

	public Lang() {
	}

	public String getLangId() {
		return this.langId;
	}

	public void setLangId(String langId) {
		this.langId = langId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginal() {
		return this.original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}