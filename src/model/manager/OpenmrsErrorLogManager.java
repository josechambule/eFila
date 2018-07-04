package model.manager;

import org.apache.log4j.Logger;
import org.celllife.idart.database.hibernate.OpenmrsErrorLog;
import org.hibernate.Session;

public class OpenmrsErrorLogManager {
	
	private static Logger log = Logger.getLogger(OpenmrsErrorLogManager.class);
	
	public static void saveOpenmrsRestLog(Session session, OpenmrsErrorLog errorLog) {
		if (errorLog!=null)
			session.save(errorLog);
	}
}
