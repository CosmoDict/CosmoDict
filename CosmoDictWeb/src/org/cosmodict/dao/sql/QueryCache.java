package org.cosmodict.dao.sql;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueryCache {

	private Map<String, String> queries;

	public QueryCache() {
		queries = new ConcurrentHashMap<String, String>();
	}

	public String getQuery(String name) throws IOException {
		String query = null;
		query = queries.get(name);
		if (query == null) {
			query = getSQL(name);
			queries.put(name, query);
		}
		return query;
	}

	private String getSQL(String script) throws IOException {
		Reader r = null;
		try {
			r = new InputStreamReader(QueryCache.class.getResourceAsStream(script), "UTF-8");
			StringBuilder sb = new StringBuilder();
			int c = 0;
			while ((c = r.read()) != -1) {
				sb.append((char) c);
			}
			return sb.toString();
		} finally {
			if (r != null) {
				r.close();
			}
		}
	}

	public String getQuery(String name, String replace) throws IOException {
		String query = null;
		boolean empty = replace == null || "".equals(replace);
		String key = (empty) ? name : name + replace;
		query = queries.get(key);
		if (query == null) {
			query = getSQL(name);
			if (!empty) {
				query = query.replace(replace, "");
			}
			queries.put(key, query);
		}
		return query;
	}

	public String total(String sql) {
		return "SELECT COUNT(*) FROM (" + sql + ") TOT";
	}

	public String limit(String sql) {
		return sql + "\n LIMIT ?,?";
	}

	public String updateSQL(String[] columns, String[] values, String[] params, boolean init) {
		StringBuilder sql = new StringBuilder();
		int num = 0;
		for (int i = 0; i < columns.length; i++) {
			String column = columns[i];
			String param = params[i];
			if (param != null) {
				if (init && num == 0) {
					sql.append("SET ");
				} else {
					sql.append(",");
				}
				sql.append(column);
				sql.append("=");
				if (values != null) {
					sql.append(values[i]);
				} else {
					sql.append("?");
				}
				num++;
			}
		}
		return (num > 0) ? sql.toString() : null;
	}

	public String updateSQL(String sql, String[] columns, String[] params) {
		return updateSQL(sql, columns, null, params, true);
	}

	public String updateSQL(String sql, String[] columns, String[] params, boolean init) {
		String columnsSQL = updateSQL(columns, null, params, init);
		return sql.replace(":columns", columnsSQL);
	}

	public String updateSQL(String sql, String[] columns, String[] values, String[] params, boolean init) {
		String columnsSQL = updateSQL(columns, values, params, init);
		return sql.replace(":columns", columnsSQL);
	}
}
