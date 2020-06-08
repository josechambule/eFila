package org.celllife.idart.facade;

import model.manager.AdherenceManager;
import org.celllife.idart.database.hibernate.Packages;
import org.celllife.idart.database.hibernate.PillCount;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Set;

/**
 */
public class PillCountFacade {

	private Session sess = null;

	/**
	 * Constructor for PillCountFacade.
	 * @param sess Session
	 */
	public PillCountFacade(Session sess) {
		this.sess = sess;
	}

	/**
	 * @param pcList
	 * @throws HibernateException
	 */
	public void save(Set<PillCount> pcList) throws HibernateException {
		if (pcList != null) {
			AdherenceManager.save(sess, pcList);
		}
	}

	/**
	 * Method getPillCountsReturnedFromThisPackage.
	 * @param p Packages
	 * @return Set<PillCount>
	 * @throws HibernateException
	 */
	public Set<PillCount> getPillCountsReturnedFromThisPackage(Packages p)
			throws HibernateException {

		return p.getPillCounts();

	}

}
