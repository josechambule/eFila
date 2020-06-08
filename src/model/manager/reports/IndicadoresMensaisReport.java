/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */package model.manager.reports;

import model.manager.excel.conversion.exceptions.ReportException;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.eclipse.swt.widgets.Shell;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IndicadoresMensaisReport extends AbstractJasperReport {

	private final Date startDate;
	private final Date endDate;

	public IndicadoresMensaisReport(Shell parent, Date startDate, Date endDate) {
		super(parent);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	protected void generateData() throws ReportException {
	}

	@Override
	protected Map<String, Object> getParameterMap() throws ReportException {

		ConexaoJDBC conn = new ConexaoJDBC();

		Map<String, Object> map = new HashMap<String, Object>();

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			//Total de pacientes que levantaram arv 20 a 20
			Map mapaIndicadoresMesal = conn.indicadoresMensais(dateFormat.format(startDate),dateFormat.format(endDate));

			int adultnovosdt = Integer.parseInt(mapaIndicadoresMesal.get("adultnovosdt").toString());
			int adultmanuntencaodt = Integer.parseInt(mapaIndicadoresMesal.get("adultmanuntencaodt").toString());
			int adulttransportedt = Integer.parseInt(mapaIndicadoresMesal.get("adulttransportedt").toString());
			int adultcumulativodt = Integer.parseInt(mapaIndicadoresMesal.get("adultcumulativodt").toString());

			int pednovosdt = Integer.parseInt(mapaIndicadoresMesal.get("pednovosdt").toString());
			int pedmanuntencaodt = Integer.parseInt(mapaIndicadoresMesal.get("pedmanuntencaodt").toString());
			int pedtransportedt = Integer.parseInt(mapaIndicadoresMesal.get("pedtransportedt").toString());
			int pedcumulativodt = Integer.parseInt(mapaIndicadoresMesal.get("pedcumulativodt").toString());

			int adultnovosds = Integer.parseInt(mapaIndicadoresMesal.get("adultnovosds").toString());
			int adultmanuntencaods = Integer.parseInt(mapaIndicadoresMesal.get("adultmanuntencaods").toString());
			int adulttransporteds = Integer.parseInt(mapaIndicadoresMesal.get("adulttransporteds").toString());
			int adultcumulativods = Integer.parseInt(mapaIndicadoresMesal.get("adultcumulativods").toString());

			int pednovosds = Integer.parseInt(mapaIndicadoresMesal.get("pednovosds").toString());
			int pedmanuntencaods = Integer.parseInt(mapaIndicadoresMesal.get("pedmanuntencaods").toString());
			int pedtransporteds = Integer.parseInt(mapaIndicadoresMesal.get("pedtransporteds").toString());
			int pedcumulativods = Integer.parseInt(mapaIndicadoresMesal.get("pedcumulativods").toString());

			int totalmmia = Integer.parseInt(mapaIndicadoresMesal.get("totalmmia").toString());

			int totalaf = Integer.parseInt(mapaIndicadoresMesal.get("totalaf").toString());
			int totalgaac = Integer.parseInt(mapaIndicadoresMesal.get("totalgaac").toString());
			int totalca = Integer.parseInt(mapaIndicadoresMesal.get("totalca").toString());
			int totalptv = Integer.parseInt(mapaIndicadoresMesal.get("totalptv").toString());
			int totalcpn = Integer.parseInt(mapaIndicadoresMesal.get("totalcpn").toString());
			int totaltb = Integer.parseInt(mapaIndicadoresMesal.get("totaltb").toString());
			int totalccr = Integer.parseInt(mapaIndicadoresMesal.get("totalccr").toString());
			int totalsaaj = Integer.parseInt(mapaIndicadoresMesal.get("totalsaaj").toString());
			int totalprep = Integer.parseInt(mapaIndicadoresMesal.get("totalprep").toString());
			int totaldc = Integer.parseInt(mapaIndicadoresMesal.get("totaldc").toString());
			int totalppe = Integer.parseInt(mapaIndicadoresMesal.get("totalppe").toString());
			int totalCE = Integer.parseInt(mapaIndicadoresMesal.get("totalCE").toString());
			int totalDM = Integer.parseInt(mapaIndicadoresMesal.get("totalDM").toString());


			map.put("path", getReportPath());
			map.put("facilityName", LocalObjects.pharmacy.getPharmacyName());
			map.put("pharmacist1", LocalObjects.pharmacy.getPharmacist());
			map.put("pharmacist2", LocalObjects.pharmacy.getAssistantPharmacist());

			map.put("startDate", new Timestamp(getBeginningOfDay(startDate).getTime()));
			map.put("endDate", new Timestamp(getEndOfDay(endDate).getTime()));

			map.put("adultnovosdt", String.valueOf(adultnovosdt));
			map.put("adultmanuntencaodt", String.valueOf(adultmanuntencaodt));
			map.put("adulttransportedt", String.valueOf(adulttransportedt));
			map.put("adultcumulativodt", String.valueOf(adultcumulativodt));

			map.put("pednovosdt", String.valueOf(pednovosdt));
			map.put("pedmanuntencaodt", String.valueOf(pedmanuntencaodt));
			map.put("pedtransportedt", String.valueOf(pedtransportedt));
			map.put("pedcumulativodt", String.valueOf(pedcumulativodt));

			map.put("adultnovosds", String.valueOf(adultnovosds));
			map.put("adultmanuntencaods", String.valueOf(adultmanuntencaods));
			map.put("adulttransporteds", String.valueOf(adulttransporteds));
			map.put("adultcumulativods", String.valueOf(adultcumulativods));

			map.put("pednovosds", String.valueOf(pednovosds));
			map.put("pedmanuntencaods", String.valueOf(pedmanuntencaods));
			map.put("pedtransporteds", String.valueOf(pedtransporteds));
			map.put("pedcumulativods", String.valueOf(pedcumulativods));

			map.put("totalnovosdt", String.valueOf(adultnovosdt+pednovosdt));
			map.put("totalmanuntencaodt", String.valueOf(adultmanuntencaodt+pedmanuntencaodt));
			map.put("totaltransportedt", String.valueOf(adulttransportedt+pedtransportedt));
			map.put("totalcumulativodt", String.valueOf(adultcumulativodt+pedcumulativodt));

			map.put("totalnovosds", String.valueOf(adultnovosds+pednovosds));
			map.put("totalmanuntencaods", String.valueOf(adultmanuntencaods+pedmanuntencaods));
			map.put("totaltransporteds", String.valueOf(adulttransporteds+pedtransporteds));
			map.put("totalcumulativods", String.valueOf(adultcumulativods+pedcumulativods));

			map.put("totalmmia", String.valueOf(totalmmia));
			map.put("totalaf", String.valueOf(totalaf));
			map.put("totalgaac", String.valueOf(totalgaac));
			map.put("totalca", String.valueOf(totalca));
			map.put("totalptv", String.valueOf(totalptv));
			map.put("totalcpn", String.valueOf(totalcpn));
			map.put("totaltb", String.valueOf(totaltb));
			map.put("totalccr", String.valueOf(totalccr));
			map.put("totalsaaj", String.valueOf(totalsaaj));
			map.put("totalprep", String.valueOf(totalprep));
			map.put("totaldc", String.valueOf(totaldc));
			map.put("totalppe", String.valueOf(totalppe));
			map.put("totalCE", String.valueOf(totalCE));
			map.put("totalDM", String.valueOf(totalDM));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	}

	@Override
	protected String getReportFileName() {
		return "monthlyCCSIndicatorReport";
	}

}
