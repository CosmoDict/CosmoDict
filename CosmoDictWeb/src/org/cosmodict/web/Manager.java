package org.cosmodict.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.cosmodict.dao.DAOException;
import org.cosmodict.dao.DefinitionDAO;
import org.cosmodict.dao.LangDAO;
import org.cosmodict.dao.TokenDAO;
import org.cosmodict.dao.TranslationDAO;
import org.cosmodict.jpa.Definition;
import org.cosmodict.jpa.Lang;
import org.cosmodict.jpa.Token;
import org.cosmodict.jpa.Translation;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

@ManagedBean(name = "manager")
@SessionScoped
public class Manager {

	public static final AtomicBoolean ok = new AtomicBoolean(false);

	public static List<Lang> langsAll = new ArrayList<Lang>();
	public static final Map<String, Lang> langsMap = new HashMap<String, Lang>(500);
	public static final Map<Integer, Lang> priorityMap = new HashMap<Integer, Lang>(500);

	//public static final Map<Integer, Token> tokensMap = new LinkedHashMap<Integer, Token>(200000);
	//public static final Map<Integer, Definition> defsMap = new LinkedHashMap<Integer, Definition>(400000);
	//public static final Map<Integer, Translation> trsMap = new LinkedHashMap<Integer, Translation>(1000000);
	//public static final Map<String, Token> tokensStringMap = new LinkedHashMap<String, Token>(200000);
	//public static final Map<String, Definition> defsStringMap = new LinkedHashMap<String, Definition>(400000);
	//public static final Map<String, Translation> trsStringMap = new LinkedHashMap<String, Translation>(1000000);

	private final DualListModel<Lang> langs = new DualListModel<Lang>(new ArrayList<Lang>(), new ArrayList<Lang>());

	private TokenLazyDataModel tokens;
	private DefinitionLazyDataModel definitions;
	private TranslationLazyDataModel translations;

	private Token tokenSearch;
	private Token tokenEdit;

	private Definition defSearch;
	private Definition defEdit;
	private Definition defItem;

	private List<Definition> searches;

	private Definition srcDef;
	private Definition destDef;

	private Translation trSearch;
	private Translation trEdit;
	private Translation trItem;

	private LangDAO langDAO;
	private TokenDAO tokenDAO;
	private DefinitionDAO defDAO;
	private TranslationDAO trDAO;

	private Comparator<? super Lang> langComparator = new Comparator<Lang>() {

		@Override
		public int compare(Lang o1, Lang o2) {
			return priority(o1) - priority(o2);
		}

		public int priority(Lang o1) {
			Integer p = o1.getPriority();
			return (p != null) ? p.intValue() : -1;
		}
	};

	@PostConstruct
	public void init() {
		try {
			initDAOs();
			initStaticData();
			initObjects();
			initViews();
			initResults();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Initialization Error", e.getMessage()));
		}
	}

	private void initStaticData() throws DAOException {
		if (!ok.get()) {
			initLangs();
			//initMaps();
			ok.set(true);
		}
	}

	private void initDAOs() {
		langDAO = new LangDAO();
		tokenDAO = new TokenDAO();
		defDAO = new DefinitionDAO();
		trDAO = new TranslationDAO();
	}

	private void initLangs() throws DAOException {
		//importLangsList();
		initLangsList();
		initLangsMap();
		//setPriority();
	}

	/*
	private void importLangsList() {
		File index = new File("/opt/src/workspace1/CosmoDictWeb/WebContent/app_langs.txt");
		BufferedReader r = null;
		try {
			try {
				r = new BufferedReader(new FileReader(index));
				String line = null;
				Pattern p = Pattern.compile("(?is)^([^=]*)=([^=]*)=(.*)$");
				while ((line = r.readLine()) != null) {
					if (!line.isEmpty() && !line.startsWith("#")) {
						Matcher m = p.matcher(line);
						if (m.matches()) {
							try {
								Lang instance = new Lang(m.group(1), m.group(3), m.group(2));
								langDAO.update(instance);
							} catch (Exception e) {
								throw new DAOException(e);
							}
						}
					}
				}
			} finally {
				if (r != null) {
					r.close();
				}
			}
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	private void setPriority() {
		String[] langs = "en	de	fr	it	ru	cmn	es	pt	et	he	ar	hi	yi	fa	ja	el	tr	sv	pl	hu	eo	ia	la	ko	id	af	sw	zh	arz	sa	bo	jv	mg	uk	ms	nl	qu	io	yo	sh	yue	arc	vi	no	nah	jbo	cs	nan	bn	fi	ro	ca	ie	am	sr	gan	ur	pa	my	sk	da	eu	zu	kk	hak	pnb	te	tl	lt	gl	so	bg	wuu	ku	mr	ceb	sl	az	oc	hr	ckb	ta	th	nn	ka	pms	uz	mzn	gu	ilo	cy	lb	br	vo	ps	ml	mn	lv	is	lmo	ce	glk	su	war	fy	ga	an	be	zza	kn	min	sco	nds	ast	mk	tk	or	bug	bar	als	scn	bs	tt	bcl	sgs	nds-nl	nap	be-x-old	new	si	wa	vls	vec	sq	hif	pam	gd	xmf	roa-tar	ba	bh	mi	fo	ht	co	ky	rmy	lo	hsb	pcd	cv	li	tg	se	sah	vro	os	mrj	mhr	rue"
				.split("[\\s]+", -1);
		int i = 0;
		for (String l : langs) {
			Lang lang = langsMap.get(l);
			if (lang != null) {
				lang.setPriority(i);
			}
			try {
				langDAO.update(lang);
			} catch (DAOException e) {
				throw new DAOException(e);
			}
			i++;
		}
	}
	*/

	private void initLangsMap() {
		for (Lang l : langsAll) {
			langsMap.put(l.getLangId(), l);
			//priorityMap.put(l.getPriority(), l);
		}
	}

	private void initLangsList() throws DAOException {
		langsAll = langDAO.list("Lang.findAll");
	}

	/*
	private void initMaps() throws DAOException {
		initTokensMap();
		initDefinitionsMap();
		initTranslationsMap();
	}

	private void initTokensMap() throws DAOException {
		List<Token> list = tokenDAO.list("Token.findAll");
		if (list != null) {
			for (Token t : list) {
				tokensMap.put(t.getTokenId(), t);
			}
		}
	}

	private void initDefinitionsMap() throws DAOException {
		List<Definition> list = defDAO.list("Definition.findAll");
		if (list != null) {
			for (Definition t : list) {
				defsMap.put(t.getDefId(), t);
			}
		}
	}

	private void initTranslationsMap() throws DAOException {
		List<Translation> list = trDAO.list("Translation.findAll");
		if (list != null) {
			for (Translation t : list) {
				trsMap.put(t.getTrId(), t);
			}
		}
	}
	*/

	private void initObjects() {
		tokenSearch = new Token();
		tokenEdit = new Token();

		defSearch = new Definition();
		defEdit = new Definition();
		defItem = new Definition();

		trSearch = new Translation();
		trEdit = new Translation();
		trItem = new Translation();

		searches = new ArrayList<Definition>(500);

	}

	@SuppressWarnings("unused")
	private void initResults() {
		searchTokens();
		searchDefinitions();
		searchTranslations();
	}

	private void initViews() {
		initLangsView();
		initTokensView();
		initDefinitionsView();
		initTranslationView();
	}

	private void initTranslationView() {
		translations = new TranslationLazyDataModel(trDAO, trSearch);
	}

	private void initDefinitionsView() {
		definitions = new DefinitionLazyDataModel(defDAO, defSearch);
	}

	private void initTokensView() {
		tokens = new TokenLazyDataModel(tokenDAO, tokenSearch);
	}

	private void initLangsView() {
		//langs.getSource().add(null);
		langs.getSource().addAll(langsAll);
		langs.getTarget().addAll(
				Arrays.asList(langsMap.get("en"), langsMap.get("de"), langsMap.get("fr"), langsMap.get("it"),
						langsMap.get("ru"), langsMap.get("cmn"), langsMap.get("es"), langsMap.get("pt"),
						langsMap.get("et"), langsMap.get("he"), langsMap.get("ar"), langsMap.get("hi"),
						langsMap.get("yi"), langsMap.get("fa"), langsMap.get("ja"), langsMap.get("el"),
						langsMap.get("tr"), langsMap.get("sv"), langsMap.get("pl"), langsMap.get("hu"),
						langsMap.get("eo"), langsMap.get("ia"), langsMap.get("la"), langsMap.get("ko"),
						langsMap.get("id"), langsMap.get("af"), langsMap.get("sw")));
		Collections.sort(langs.getSource(), langComparator);
		Collections.sort(langs.getTarget(), langComparator);
	}

	public String searchTokens() {
		try {
			tokens.setSearch(tokenSearch);
			tokens.setRowCount(-1);
			tokens.load(0, 0, null, null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search Error", e.getMessage()));
		}
		return "";
	}

	public String addNewToken() {
		try {
			Integer tokenId = tokenEdit.getTokenId();
			if (tokenId == null || !tokenDAO.existId(tokenId)) {
				tokenId = (Integer) tokenDAO.save(tokenEdit);
				if (tokenId != null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Token saved", "Id:" + tokenId));
				} else {
					throw new Exception("No generated id");
				}
			} else {
				tokenDAO.update(tokenEdit);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
		}
		return "";
	}

	public String deleteToken() {
		try {
			tokenDAO.delete(tokenEdit);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deletion Error", e.getMessage()));
		}
		return "";
	}

	public String searchItemDefinitions() {
		appendDefinition();
		searchDefinitions();
		return "";
	}

	public String selectSearch() {
		selectItem();
		searchDefinitions();
		return "";
	}

	public String deleteSearch() {
		if (defItem != null) {
			searches.remove(defItem);
			defItem = new Definition();
		}
		return "";
	}

	public String saveDefSearch() {
		if (defSearch != null) {
			searches.add(defSearch);
			defSearch = new Definition();
		}
		return "";
	}

	public String searchDefinitionTranslations() {
		appendTranslation();
		searchTranslations();
		return "";
	}

	private void appendTranslation() {
		trSearch = trItem;
		trItem = new Translation();
	}

	private void appendDefinition() {
		if (defSearch != null) {
			searches.add(defSearch);
			defSearch = defItem;
			defItem = new Definition();
		}
	}

	private void selectItem() {
		if (defItem != null) {
			defSearch = defItem;
			defItem = new Definition();
		}
	}

	public String searchDefinitions() {
		try {
			definitions.setSearch(defSearch);
			definitions.setRowCount(-1);
			definitions.load(0, 0, null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search Error", e.getMessage()));
		}
		return "";
	}

	@SuppressWarnings("unused")
	private String[] getLangIds() {
		List<Lang> t = langs.getTarget();
		String[] arr = (t != null) ? new String[t.size()] : null;
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				arr[i] = t.get(i).getLangId();
			}
		}
		return arr;
	}

	public String addNewDefinition() {
		try {
			Integer defId = defEdit.getDefId();
			if (defId == null || !defDAO.existId(defId)) {
				defId = (Integer) defDAO.save(defEdit);
				if (defId != null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Definition saved", "Id:" + defId));
				} else {
					throw new Exception("No generated id");
				}
			} else {
				defDAO.update(defEdit);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
		}
		return "";
	}

	public String deleteDefinition() {
		try {
			defDAO.delete(defEdit);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deletion Error", e.getMessage()));
		}
		return "";
	}

	public String searchTranslations() {
		try {
			translations.setSearch(trSearch);
			translations.setRowCount(-1);
			translations.load(0, 0, null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Search Error", e.getMessage()));
		}
		return "";
	}

	public String editToken() {
		return "";
	}

	public String editDefinition() {
		return "";
	}

	public String editTranslation() {
		return "";
	}

	public String addNewTranslation() {
		try {
			Integer trId = trEdit.getTrId();
			if (trId == null || !trDAO.existId(trId)) {
				trId = (Integer) trDAO.save(trEdit);
				if (trId != null) {
					Integer defId = trEdit.getDefId();
					if (defId != null) {
						updateMask(defId);
					}
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Translation saved", "Id:" + trId));
				} else {
					throw new Exception("No generated id");
				}
			} else {
				trDAO.update(trEdit);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save Error", e.getMessage()));
		}
		return "";
	}

	private void updateMask(Integer defId) throws DAOException {
		String mask = trDAO.getMask(defId);
		trDAO.updateMask(defId, mask);
	}

	public String deleteTranslation() {
		try {
			trDAO.delete(trEdit);
			updateMask(trEdit.getDefId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deletion Error", e.getMessage()));
		}
		return "";
	}

	public void onTransfer(TransferEvent event) {

	}

	public void onSelect(SelectEvent event) {

	}

	public void onUnselect(UnselectEvent event) {

	}

	public synchronized List<String> maskLangs(Definition def) {
		List<String> maskLangs = def.getMaskLangs();
		String mask = def.getMask();
		if (maskLangs == null && mask != null) {
			maskLangs = initMaskLangs(mask);
		}
		return maskLangs;
	}

	private List<String> initMaskLangs(String mask) {
		LinkedList<String> maskLangs = new LinkedList<String>();
		List<Lang> selected = langs.getTarget();
		int n = selected.size();
		int length = mask.length();
		for (int i = 0; i < n; i++) {
			Lang l = selected.get(i);
			if (l != null) {
				int p = l.getPriority();
				if (p > -1 && p < length) {
					if (mask.charAt(p) == '1') {
						maskLangs.add(l.getLangId());
					} else {
						maskLangs.add("!" + l.getLangId());
					}
				}
			}
		}
		return maskLangs;
	}

	public String configureLangs() {
		return "";
	}

	public String nextDefinition() {
		return "";
	}

	public Token getTokenSearch() {
		return tokenSearch;
	}

	public Token getTokenEdit() {
		return tokenEdit;
	}

	public Definition getDefSearch() {
		return defSearch;
	}

	public Definition getDefEdit() {
		return defEdit;
	}

	public List<Definition> getSearches() {
		return searches;
	}
	public Translation getTrSearch() {
		return trSearch;
	}

	public Translation getTrEdit() {
		return trEdit;
	}

	public void setTokenSearch(Token tokenSearch) {
		this.tokenSearch = (tokenSearch != null) ? tokenSearch : new Token();
	}

	public void setTokenEdit(Token tokenEdit) {
		this.tokenEdit = (tokenEdit != null) ? tokenEdit : new Token();
	}

	public void setDefSearch(Definition defSearch) {
		this.defSearch = (defSearch != null) ? defSearch : new Definition();;
	}

	public void setDefEdit(Definition defEdit) {
		this.defEdit = (defEdit != null) ? defEdit : new Definition();
	}

	public void setSearches(List<Definition> searches) {
		this.searches = searches;
	}

	public void setTrSearch(Translation trSearch) {
		this.trSearch = (trSearch != null) ? trSearch : new Translation();
	}

	public void setTrEdit(Translation trEdit) {
		this.trEdit = (trEdit != null) ? trEdit : new Translation();
	}

	public Definition getSrcDef() {
		return srcDef;
	}

	public void setSrcDef(Definition srcDef) {
		this.srcDef = srcDef;
	}

	public Definition getDestDef() {
		return destDef;
	}

	public void setDestDef(Definition destDef) {
		this.destDef = destDef;
	}

	public DualListModel<Lang> getLangs() {
		return langs;
	}

	public TokenLazyDataModel getTokens() {
		return tokens;
	}

	public DefinitionLazyDataModel getDefinitions() {
		return definitions;
	}

	public TranslationLazyDataModel getTranslations() {
		return translations;
	}

	public void setTokens(TokenLazyDataModel tokens) {
		this.tokens = tokens;
	}

	public void setDefinitions(DefinitionLazyDataModel definitions) {
		this.definitions = definitions;
	}

	public void setTranslations(TranslationLazyDataModel translations) {
		this.translations = translations;
	}

	public Definition getDefItem() {
		return defItem;
	}

	public Translation getTrItem() {
		return trItem;
	}

	public void setDefItem(Definition defItem) {
		this.defItem = defItem;
	}

	public void setTrItem(Translation trItem) {
		this.trItem = trItem;
	}

	public void setLangs(DualListModel<Lang> langs) {
		clearFill(this.langs.getSource(), langs.getSource());
		clearFill(this.langs.getTarget(), langs.getTarget());
	}

	private void clearFill(List<Lang> source, List<Lang> source2) {
		source.clear();
		Collections.sort(source2, langComparator);
		Set<String> values = new HashSet<String>();
		for (Lang l : source2) {
			if (!values.contains(l.getLangId())) {
				values.add(l.getLangId());
				source.add(l);
			}
		}
	}
}
