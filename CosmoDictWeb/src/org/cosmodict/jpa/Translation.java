package org.cosmodict.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.cosmodict.web.Manager;

/**
 * The persistent class for the TRANSLATION database table.
 * 
 */
@Entity
@Table(name = "TRANSLATION")
@NamedQuery(name = "Translation.findAll", query = "SELECT t FROM Translation t")
public class Translation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tr_id")
	private Integer trId;

	@ManyToOne
	@JoinColumn(name = "lang_id")
	private Lang lang;

	//bi-directional many-to-one association to Definition
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "def_id")
	//@Transient
	private Definition definition;

	@Column(name = "gender")
	private String gender;

	@Column(name = "gender_value")
	private String genderValue;

	@Column(name = "value")
	private String value;

	@Column(name = "pronunciation")
	private String pronunciation;

	public Translation() {
	}

	public Translation(Integer trId) {
	}

	public Integer getTrId() {
		return this.trId;
	}

	public void setTrId(Integer trId) {
		this.trId = trId;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGenderValue() {
		return this.genderValue;
	}

	public void setGenderValue(String genderValue) {
		this.genderValue = genderValue;
	}

	public String getLangId() {
		return (lang != null) ? lang.getLangId() : null;
	}

	public void setLangId(String langId) {
		this.lang = Manager.langsMap.get(langId);
	}

	public String getPronunciation() {
		return this.pronunciation;
	}

	public void setPronunciation(String pronunciation) {
		this.pronunciation = pronunciation;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Definition getDefinition() {
		return this.definition;
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	public Integer getDefId() {
		return definition != null ? definition.getDefId() : null;
	}

	public synchronized void setDefId(Integer defId) {
		if (definition == null) {
			definition = new Definition();
		}
		definition.setDefId(defId);
	}

	public Lang getLang() {
		return lang;
	}

	public void setLang(Lang lang) {
		this.lang = lang;
	}

}