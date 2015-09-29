package org.cosmodict.web;
import java.util.List;
import java.util.Map;

import org.cosmodict.dao.DefinitionDAO;
import org.cosmodict.jpa.Definition;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
public class DefinitionLazyDataModel extends LazyDataModel<Definition> {
	private static final long serialVersionUID = 1L;

	private Definition search;
	private DefinitionDAO dao;

	public DefinitionLazyDataModel(DefinitionDAO dao, Definition search) {
		this.dao = dao;
		this.search = search;
	}

	@Override
	public List<Definition> load(int first, int pageSize, List<SortMeta> sortMeta, Map<String, Object> filters) {
		List<Definition> results = null;
		try {
			results = dao.findDefinitions(search, first, pageSize, sortMeta, filters);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		// set the total of players
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

	public Definition getSearch() {
		return search;
	}

	public void setSearch(Definition search) {
		this.search = search;
	}

}
