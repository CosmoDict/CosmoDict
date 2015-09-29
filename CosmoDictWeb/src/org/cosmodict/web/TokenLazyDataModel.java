package org.cosmodict.web;

import java.util.List;
import java.util.Map;

import org.cosmodict.dao.TokenDAO;
import org.cosmodict.jpa.Token;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class TokenLazyDataModel extends LazyDataModel<Token> {

	private static final long serialVersionUID = 1L;

	private Token search;
	private TokenDAO dao;

	public TokenLazyDataModel(TokenDAO dao, Token search) {
		this.dao = dao;
		this.search = search;
	}

	@Override
	public List<Token> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<Token> results = null;
		try {
			results = dao.findTokens(search, first, pageSize, sortField, sortOrder);
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

	public Token getSearch() {
		return search;
	}

	public void setSearch(Token search) {
		this.search = search;
	}

}
