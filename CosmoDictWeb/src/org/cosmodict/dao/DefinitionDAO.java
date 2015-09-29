package org.cosmodict.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.cosmodict.jpa.Definition;
import org.cosmodict.jpa.Token;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.jdbc.Work;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class DefinitionDAO extends EntityDAO<Definition> {

	public int countTotal(Definition search) throws DAOException {
		//int total = 10000;
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

	private Criteria criteria(Session session, Definition search) {
		Criteria criteria = session.createCriteria(Definition.class);
		Criteria tokenCriteria = criteria.createCriteria("token", "token", Criteria.LEFT_JOIN);
		{
			Integer defId = search.getDefId();
			if (defId != null) {
				criteria.add(Restrictions.eq("defId", defId));
			}
		}

		{
			Token token = search.getToken();
			if (token != null) {
				{
					Integer tokenId = token.getTokenId();
					if (tokenId != null) {
						tokenCriteria.add(Restrictions.eq("token.tokenId", tokenId));
					}
				}

				{
					String value = token.getValue();
					if (value != null) {
						tokenCriteria.add(Restrictions.like("token.value", value));
					}
				}
			}
		}

		{
			String type = search.getType();
			if (type != null) {
				criteria.add(Restrictions.like("type", type));
			}
		}

		{
			String group = search.getGroup();
			if (group != null) {
				criteria.add(Restrictions.like("group", group));
			}
		}

		{
			Integer parentId = search.getParentId();
			if (parentId != null) {
				criteria.add(Restrictions.eq("parentId", parentId));
			}
		}

		{
			String value = search.getValue();
			if (value != null) {
				criteria.add(Restrictions.like("value", value));
			}
		}

		{
			String mask = search.getMask();
			if (mask != null) {
				SimpleExpression like = Restrictions.like("mask", mask);
				if (search.isMaskNot()) {
					criteria.add(Restrictions.not(like));
				} else {
					criteria.add(like);
				}
			}
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	public List<Definition> findDefinitions(Definition search, int first, int pageSize, List<SortMeta> sortMeta,
			Map<String, Object> filters) throws DAOException {
		List<Definition> results = null;
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = criteria(session, search);

			criteria.setFirstResult(first);
			criteria.setMaxResults(pageSize);

			if (sortMeta == null || sortMeta.isEmpty()) {
				criteria.addOrder(Order.asc("ordinal"));
				criteria.addOrder(Order.asc("token.tokenId"));
			} else {
				for (SortMeta m : sortMeta) {
					String field = m.getSortField();
					SortOrder order = m.getSortOrder();

					Order ord = (order == null || order.equals(SortOrder.UNSORTED) || order.equals(SortOrder.ASCENDING))
							? Order.asc(field)
							: Order.desc(field);
					criteria.addOrder(ord);
				}
			}

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

	public boolean existId(Integer defId) throws DAOException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Definition.class);
			criteria.add(Restrictions.eq("defId", defId));
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

	public Integer save(final Definition def) throws DAOException {
		Session session = null;
		Transaction tr = null;
		final Integer[] r = {null};
		try {
			session = getSession();
			tr = session.beginTransaction();
			final String sql = queryCache.getQuery("save_def.sql");
			Work work = new Work() {

				@Override
				public void execute(Connection c) throws SQLException {
					PreparedStatement st = c.prepareStatement(sql);
					int ix = 1;
					{
						Integer id = def.getDefId();
						if (id == null) {
							st.setNull(ix++, Types.INTEGER);
						} else {
							st.setInt(ix++, id);
						}
					}

					{
						Integer tokenId = def.getTokenId();
						if (tokenId == null) {
							st.setNull(ix++, Types.INTEGER);
						} else {
							st.setInt(ix++, tokenId);
						}
					}

					{
						Integer parentId = def.getParentId();
						if (parentId == null) {
							st.setNull(ix++, Types.INTEGER);
						} else {
							st.setInt(ix++, parentId);
						}
					}

					{
						String type = def.getType();
						if (type == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, type);
						}
					}

					{
						String group = def.getGroup();
						if (group == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, group);
						}
					}

					{
						String value = def.getValue();
						if (value == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, value);
						}
					}

					{
						Integer ord = def.getOrdinal();
						if (ord == null) {
							st.setNull(ix++, Types.INTEGER);
						} else {
							st.setInt(ix++, ord);
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
