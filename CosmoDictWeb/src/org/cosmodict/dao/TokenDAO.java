package org.cosmodict.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.cosmodict.jpa.Token;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.primefaces.model.SortOrder;

public class TokenDAO extends EntityDAO<Token> {

	@SuppressWarnings("unchecked")
	public List<Token> findTokens(Token search, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws DAOException {
		List<Token> results = null;
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = criteria(session, search);

			criteria.setFirstResult(first);
			criteria.setMaxResults(pageSize);

			if (sortField == null) {
				sortField = "tokenId";
			}

			Order ord = (sortOrder == null || sortOrder.equals(SortOrder.UNSORTED) || sortOrder
					.equals(SortOrder.ASCENDING)) ? Order.asc(sortField) : Order.desc(sortField);
			criteria.addOrder(ord);

			results = criteria.list();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return results;
	}

	private Criteria criteria(Session session, Token search) {
		Criteria criteria = session.createCriteria(Token.class);

		{
			Integer tokenId = search.getTokenId();
			if (tokenId != null) {
				criteria.add(Restrictions.eq("tokenId", tokenId));
			}
		}

		{
			String value = search.getValue();
			if (value != null && !value.isEmpty()) {
				criteria.add(Restrictions.like("value", value));
			}
		}

		return criteria;
	}

	public int countTotal(Token search) throws DAOException {
		int total = 0;
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = criteria(session, search);
			criteria.setProjection(Projections.rowCount());
			Number result = (Number) criteria.uniqueResult();
			total = result.intValue();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return total;
	}

	public boolean existId(Integer tokenId) throws DAOException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Token.class);
			criteria.add(Restrictions.eq("tokenId", tokenId));
			criteria.setProjection(Projections.rowCount());
			Number result = (Number) criteria.uniqueResult();
			int total = result.intValue();
			return total > 0;
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public Integer save(final Token token) throws DAOException {
		Session session = null;
		Transaction tr = null;
		final Integer[] r = {null};
		try {
			session = getSession();
			tr = session.beginTransaction();
			final String sql = queryCache.getQuery("save_token.sql");
			Work work = new Work() {

				@Override
				public void execute(Connection c) throws SQLException {
					PreparedStatement st = c.prepareStatement(sql);
					int ix = 1;
					{
						Integer id = token.getTokenId();
						if (id == null) {
							st.setNull(ix++, Types.INTEGER);
						} else {
							st.setInt(ix++, id);
						}
					}

					{
						String value = token.getValue();
						if (value == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, value);
						}
					}

					st.executeUpdate();
					ResultSet rs = st.getGeneratedKeys();
					if (rs.next()) {
						r[0] = rs.getInt(1);
					}
				}
			};

			session.doWork(work);
			tr.commit();
			return r[0];
		} catch (Exception e) {
			if (tr != null) {
				tr.rollback();
			}
			throw new DAOException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
