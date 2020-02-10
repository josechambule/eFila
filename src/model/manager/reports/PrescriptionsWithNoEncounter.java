package model.manager.reports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.celllife.idart.database.hibernate.StockCenter;
import org.eclipse.swt.widgets.Shell;

import model.manager.excel.conversion.exceptions.ReportException;

public class PrescriptionsWithNoEncounter extends AbstractJasperReport {
	
	private final StockCenter stockCenter;
	private final Date theEndDate;
	private Date theStartDate;
	private boolean inicio;

	public PrescriptionsWithNoEncounter(Shell parent, StockCenter stockCenter, Date theStartDate, Date theEndDate) {
		super(parent);
		this.stockCenter = stockCenter;
		this.theStartDate=theStartDate;
		this.theEndDate = theEndDate;
	}

	@Override
	protected void generateData() throws ReportException {
		
		/*
		 * HSSFWorkbook workbook = new HSSFWorkbook(); HSSFSheet sheet =
		 * workbook.createSheet("Historico de Levantamento");
		 */
	}

	@Override
	protected String getReportFileName() {
		return "PrescricoesSemDispensa"; 
	}

	@Override
	protected Map<String, Object> getParameterMap() throws ReportException {
		
		ConexaoJDBC conn = new ConexaoJDBC();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		map.put("date", theStartDate);
		map.put("dateFormat", dateFormat.format(theStartDate));
		map.put("monthStart", dateFormat.format(theStartDate));
		//calStart.add(Calendar.MONTH, 1);

		map.put("monthEnd", dateFormat.format(theEndDate));
		map.put("dateEnd", theEndDate);
				
		String query= conn.getQueryPrescricoeSemDispensas(dateFormat.format(theStartDate), dateFormat.format(theEndDate));
		
		map.put("query", query);

		return map;
	}

}
