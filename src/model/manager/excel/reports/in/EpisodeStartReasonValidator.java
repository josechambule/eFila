package model.manager.excel.reports.in;

import model.manager.AdministrationManager;
import model.manager.excel.interfaces.SessionBasedImportValidator;
import org.celllife.idart.database.hibernate.SimpleDomain;
import org.hibernate.Session;

import java.util.List;

public class EpisodeStartReasonValidator implements SessionBasedImportValidator<String> {

	private Session session;
	private List<SimpleDomain> episodeStartReasons;

	@Override
	public String validate(String rawValue) {
		for (SimpleDomain reas : episodeStartReasons) {
			if (reas.getValue().equalsIgnoreCase(rawValue)) {
				return null;
			}
		}
		return "Invalid episode start reason.";
	}

	@Override
	public void initialise(Session hsession) {
		this.session = hsession;
		this.episodeStartReasons = AdministrationManager
			.getActivationReasons(session);
	}
}
