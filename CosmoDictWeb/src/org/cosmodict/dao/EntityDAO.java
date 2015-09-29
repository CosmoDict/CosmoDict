package org.cosmodict.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EntityDAO<T> extends BaseDAO {

	public Object saveInstance(T instance) throws DAOException {
		Session session = null;
		Transaction tr = null;
		try {
			session = getSession();
			tr = session.beginTransaction();
			Object r = session.save(instance);
			tr.commit();
			return r;
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

	public void saveOrUpdate(T instance) throws DAOException {
		Session session = null;
		Transaction tr = null;
		try {
			session = getSession();
			tr = session.beginTransaction();
			session.saveOrUpdate(instance);
			tr.commit();
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

	public void update(T instance) throws DAOException {
		Session session = null;
		Transaction tr = null;
		try {
			session = getSession();
			tr = session.beginTransaction();
			session.update(instance);
			tr.commit();
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

	public void delete(T instance) throws DAOException {
		Session session = null;
		Transaction tr = null;
		try {
			session = getSession();
			tr = session.beginTransaction();
			session.delete(instance);
			tr.commit();
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

	@SuppressWarnings("unchecked")
	public List<T> list(String namedQuery) throws DAOException {
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			Query query = session.getNamedQuery(namedQuery);
			return query.list();
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
