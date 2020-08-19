package model.manager.reports;

import model.manager.AdministrationManager;
import model.manager.excel.conversion.exceptions.ReportException;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.hibernate.Clinic;
import org.celllife.idart.database.hibernate.StockCenter;
import org.eclipse.swt.widgets.Shell;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class AvaliacaoSegundasLinhasReport extends AbstractJasperReport {

        private final String clinicName;
	private final String month;
	private final StockCenter stockCenter;
	private final String year;

	public AvaliacaoSegundasLinhasReport(Shell parent, String clinicName,
                        String Month, String Year,
			StockCenter stockCenter) {
		super(parent);
		month = Month;
		year = Year;
		this.stockCenter = stockCenter;
                this.clinicName=clinicName;
	}

	@Override
	protected void generateData() throws ReportException {
	}

	@Override
	protected Map<String, Object> getParameterMap() throws ReportException {
		int MonthInt = 0;
		if (month.startsWith("Jan")) {
			MonthInt = 1;
		} else if (month.startsWith("Fe")) {
			MonthInt = 2;
		} else if (month.startsWith("Mar")) {
			MonthInt = 3;
		} else if (month.startsWith("Ab") || month.startsWith("Ap")) {
			MonthInt = 4;
		} else if (month.startsWith("Mai") || month.startsWith("May")) {
			MonthInt = 5;
		} else if (month.startsWith("Jun")) {
			MonthInt = 6;
		} else if (month.startsWith("Jul")) {
			MonthInt = 7;
		} else if (month.startsWith("Ag") || month.startsWith("Au")) {
			MonthInt = 8;
		} else if (month.startsWith("Se")) {
			MonthInt = 9;
		} else if (month.startsWith("O")) {
			MonthInt = 10;
		} else if (month.startsWith("Nov")) {
			MonthInt = 11;
		} else if (month.startsWith("De")) {
			MonthInt = 12;
		}
		String startDayStr;
		if (MonthInt > 9) {
			startDayStr = year + "-" + MonthInt + "-20";
		} else {
			startDayStr = year + "-0" + MonthInt + "-20";
		}
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		java.sql.Date theDate = java.sql.Date.valueOf(startDayStr);
                
                Clinic c = AdministrationManager.getClinic(hSession, clinicName);
		// Set the parameters for the report
		Map<String, Object> map = new HashMap<String, Object>();
                map.put("path", getReportPath());
		map.put("clinic", clinicName);
//		map.put("clinicid", c.getId());
		map.put("stockCenterId", stockCenter.getId());
		map.put("stockCenterName", stockCenter.getStockCenterName());
		map.put("date", theDate);
		map.put("facilityName", LocalObjects.pharmacy.getPharmacyName());
		map.put("pharmacist1", LocalObjects.pharmacy.getPharmacist());
		map.put("pharmacist2", LocalObjects.pharmacy.getAssistantPharmacist());
		return map;
	}

	@Override
	protected String getReportFileName() {
		return "pacientesemsegundalinhaAvaliacao";
	}

}
