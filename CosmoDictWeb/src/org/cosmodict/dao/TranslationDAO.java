package org.cosmodict.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.cosmodict.jpa.Translation;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class TranslationDAO extends EntityDAO<Translation> {
	private Projection listProjection;
	private Projection totalProjection;

	{
		ProjectionList projection = Projections.projectionList();
		projection.add(Projections.property("trId").as("trId"));
		projection.add(Projections.property("definition.defId").as("defId"));
		projection.add(Projections.property("lang.langId").as("langId"));
		projection.add(Projections.property("gender").as("gender"));
		projection.add(Projections.property("genderValue").as("genderValue"));
		projection.add(Projections.property("value").as("value"));
		projection.add(Projections.property("pronunciation").as("pronunciation"));

		listProjection = projection;
		totalProjection = Projections.count("trId");

	}

	@SuppressWarnings("unchecked")
	public List<Translation> findTranslations(Translation search, int first, int pageSize, List<SortMeta> sortMeta)
			throws DAOException {
		List<Translation> results = null;
		Session session = null;
		try {
			session = getSession();
			Transaction tr = session.beginTransaction();
			Criteria criteria = criteria(session, search);

			criteria.setFirstResult(first);
			criteria.setMaxResults(pageSize);

			if (sortMeta == null || sortMeta.isEmpty()) {
				criteria.addOrder(Order.asc("definition.defId"));
				criteria.addOrder(Order.asc("lang.priority"));
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

			criteria.setProjection(listProjection);
			criteria.setResultTransformer(Transformers.aliasToBean(Translation.class));
			results = criteria.list();
			tr.commit();
			return results;
		} catch (Exception e) {
			throw new DAOException();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	private Criteria criteria(Session session, Translation search) {
		Criteria criteria = session.createCriteria(Translation.class, "this");
		Criteria defCriteria = criteria.createCriteria("definition", "definition", Criteria.INNER_JOIN);
		Criteria langCriteria = criteria.createCriteria("lang", "lang", Criteria.INNER_JOIN);

		{
			Integer trId = search.getTrId();
			if (trId != null) {
				criteria.add(Restrictions.eq("this.trId", trId));
			}
		}

		{
			Integer defId = search.getDefId();
			if (defId != null) {
				defCriteria.add(Restrictions.eq("definition.defId", defId));
			}
		}

		{
			String value = search.getValue();
			if (value != null) {
				criteria.add(Restrictions.like("this.value", value));
			}
		}

		{
			String langId = search.getLangId();
			if (langId != null) {
				langCriteria.add(Restrictions.eq("lang.langId", langId));
			}
		}

		{
			String value = search.getValue();
			if (value != null) {
				criteria.add(Restrictions.like("this.value", value));
			}
		}

		{
			String gender = search.getGender();
			if (gender != null) {
				criteria.add(Restrictions.like("this.gender", gender));
			}
		}

		{
			String value = search.getGenderValue();
			if (value != null) {
				criteria.add(Restrictions.like("this.genderValue", value));
			}
		}

		return criteria;
	}

	public int countTotal(Translation search) throws DAOException {
		int total = 0;
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = criteria(session, search);
			criteria.setProjection(totalProjection);
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

	public String getMask(Integer defId) throws DAOException {
		Session session = null;
		StringBuilder sb = null;
		try {
			session = getSession();
			Query q = session.createSQLQuery(queryCache.getQuery("mask_bit.sql"));
			int ix = 0;
			q.setInteger(ix++, defId);
			List list = q.list();
			sb = new StringBuilder();
			sb.ensureCapacity(list.size());
			for (Object o : list) {
				Object[] i = (Object[]) o;
				int p = ((Number) i[0]).intValue();
				int b = ((Number) i[1]).intValue();
				if (p >= sb.length()) {
					sb.setLength(p + 1);
				}
				sb.setCharAt(p, (b == 1) ? '1' : '0');
			}
			return sb.toString();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public int updateMask(Integer defId, String mask) throws DAOException {
		Session session = null;
		int r = 0;
		try {
			session = getSession();
			Query q = session.createQuery("update Definition set mask = ?");
			int ix = 0;
			q.setString(ix++, mask);
			r = q.executeUpdate();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return r;
	}

	public boolean existId(Integer trId) throws DAOException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Translation.class);
			criteria.add(Restrictions.eq("trId", trId));
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

	public Integer save(final Translation t) throws DAOException {
		Session session = null;
		Transaction tr = null;
		final Integer[] r = {null};
		try {
			session = getSession();
			tr = session.beginTransaction();
			final String sql = queryCache.getQuery("save_tr.sql");
			Work work = new Work() {

				@Override
				public void execute(Connection c) throws SQLException {
					PreparedStatement st = c.prepareStatement(sql);
					int ix = 1;
					{
						Integer trId = t.getTrId();
						if (trId == null) {
							st.setNull(ix++, Types.INTEGER);
						} else {
							st.setInt(ix++, trId);
						}
					}
					{
						String langId = t.getLangId();
						if (langId == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, langId);
						}
					}
					{
						Integer id = t.getDefId();
						if (id == null) {
							st.setNull(ix++, Types.INTEGER);
						} else {
							st.setInt(ix++, id);
						}
					}
					{
						String gender = t.getGender();
						if (gender == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, gender);
						}
					}
					{
						String gv = t.getGenderValue();
						if (gv == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, gv);
						}
					}
					{
						String value = t.getValue();
						if (value == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, value);
						}
					}
					{
						String prn = t.getPronunciation();
						if (prn == null) {
							st.setNull(ix++, Types.VARCHAR);
						} else {
							st.setString(ix++, prn);
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
