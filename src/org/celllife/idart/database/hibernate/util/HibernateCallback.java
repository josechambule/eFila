package org.celllife.idart.database.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.sql.SQLException;

public interface HibernateCallback<T> {

	T doInHibernate(Session session) throws HibernateException,
			SQLException;

}
