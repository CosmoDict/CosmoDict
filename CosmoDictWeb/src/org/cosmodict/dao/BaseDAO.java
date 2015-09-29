package org.cosmodict.dao;

import org.cosmodict.dao.sql.QueryCache;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

public class BaseDAO {

	protected static SessionFactory sessionFactory = buildSessionFactory();
	protected static QueryCache queryCache = new QueryCache();

	private static SessionFactory buildSessionFactory() {
		try {
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.addResource("/hibernate.cfg.xml");
			return cfg.configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
		}
		return null;
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			buildSessionFactory();
		}
		return sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.openSession();
	}

}
