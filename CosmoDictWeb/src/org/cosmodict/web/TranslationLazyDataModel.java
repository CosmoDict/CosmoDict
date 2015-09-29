package org.cosmodict.web;

import java.util.List;
import java.util.Map;

import org.cosmodict.dao.TranslationDAO;
import org.cosmodict.jpa.Translation;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

public class TranslationLazyDataModel extends LazyDataModel<Translation> {

	private static final long serialVersionUID = 1L;

	private Translation search;
	private TranslationDAO dao;

	public TranslationLazyDataModel(TranslationDAO dao, Translation search) {
		this.dao = dao;
		this.search = search;
	}

	@Override
	public List<Translation> load(int first, int pageSize, List<SortMeta> sortMeta, Map<String, Object> filters) {
		List<Translation> results = null;
		try {
			results = dao.findTranslations(search, first, pageSize, sortMeta);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		if (getRowCount() < 0) {
			int count = -1;
			try {
				count = dao.countTotal(search);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			setRowCount(count);
		}
		setPageSize(pageSize);
		return results;
	}
	public Translation getSearch() {
		return search;
	}

	public void setSearch(Translation search) {
		this.search = search;
	}

}
