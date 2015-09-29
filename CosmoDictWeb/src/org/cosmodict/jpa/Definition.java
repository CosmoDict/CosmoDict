package org.cosmodict.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.cosmodict.web.Manager;

/**
 * The persistent class for the DEFINITION database table.
 * 
 */
@Entity
@Table(name = "DEFINITION")
@NamedQuery(name = "Definition.findAll", query = "SELECT d FROM Definition d")
public class Definition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "def_id")
	private Integer defId;

	//bi-directional many-to-one association to Token
	@ManyToOne
	@JoinColumn(name = "token_id")
	//@Transient
	private Token token = new Token();

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "type")
	private String type;

	@Column(name = "grp")
	private String group;

	@Column(name = "value")
	private String value;

	@Column(name = "mask")
	//@Transient
	private String mask;

	@Column(name = "ord")
	private Integer ordinal;

	@Transient
	private boolean maskNot;

	@Transient
	private List<String> maskLangs;

	//@Column(name = "token_id")
	//private Integer tokenId;

	//bi-directional many-to-one association to Translation
	@OneToMany(mappedBy = "definition", fetch = FetchType.LAZY)
	//@Transient
	private List<Translation> translations;

	@Transient
	private List<Lang> requiredLangs;

	public Definition() {
	}

	public Definition(Integer defId) {
		this.defId = defId;
	}

	public Integer getTokenId() {
		return (token != null) ? this.token.getTokenId() : null;
	}

	public synchronized void setTokenId(Integer tokenId) {
		if (this.token == null) {
			this.token = new Token();
		}
		this.token.setTokenId(tokenId);
	}

	public String getTokenValue() {
		return (token != null) ? this.token.getValue() : null;
	}

	public synchronized void setTokenValue(String value) {
		if (this.token == null) {
			this.token = new Token();
		}
		this.token.setValue(value);
	}

	public Integer getDefId() {
		return this.defId;
	}

	public void setDefId(Integer defId) {
		this.defId = defId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Token getToken() {
		return this.token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public List<Translation> getTranslations() {
		return this.translations;
	}

	public void setTranslations(List<Translation> translations) {
		this.translations = translations;
	}

	public Translation addTranslation(Translation translation) {
		getTranslations().add(translation);
		translation.setDefinition(this);

		return translation;
	}

	public Translation removeTranslation(Translation translation) {
		getTranslations().remove(translation);
		translation.setDefinition(null);

		return translation;
	}

	private String langMask() {
		if (requiredLangs != null && !requiredLangs.isEmpty()) {
			StringBuilder mask = new StringBuilder();
			for (int i = 0; i < Manager.langsAll.size(); i++) {
				mask.append("_");
			}
			for (Lang l : requiredLangs) {
				Integer p = l.getPriority();
				if (p != null && p >= 0 && p < mask.length()) {
					mask.setCharAt(p, '1');
				}
			}
			return mask.toString();
		}
		return null;
	}

	public String getGroup() {
		return group;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public boolean isMaskNot() {
		return maskNot;
	}

	public void setMaskNot(boolean maskNot) {
		this.maskNot = maskNot;
	}

	public List<String> getMaskLangs() {
		return maskLangs;
	}

	public void setMaskLangs(List<String> maskLangs) {
		this.maskLangs = maskLangs;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public List<Lang> getRequiredLangs() {
		return requiredLangs;
	}

	public void setRequiredLangs(List<Lang> requiredLangs) {
		this.requiredLangs = requiredLangs;
		this.mask = this.langMask();
	}
}