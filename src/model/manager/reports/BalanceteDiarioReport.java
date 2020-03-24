package model.manager.reports;

import model.manager.excel.conversion.exceptions.ReportException;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.hibernate.StockCenter;
import org.celllife.idart.database.hibernate.User;
import org.eclipse.swt.widgets.Shell;

import java.text.SimpleDateFormat;
import java.util.*;

public class BalanceteDiarioReport extends AbstractJasperReport {

    private final StockCenter stockCenter;
    private final Date theEndDate;
    private Date theStartDate;
    private int drugsId;


    public BalanceteDiarioReport(Shell parent, StockCenter stockCenter, Date theStartDate, Date theEndDate, int drugsId) {
        super(parent);
        this.stockCenter = stockCenter;
        this.theStartDate = theStartDate;
        this.theEndDate = theEndDate;
        this.drugsId = drugsId;
    }

    @Override
    protected void generateData() throws ReportException {
    }

    @Override
    protected Map<String, Object> getParameterMap() throws ReportException {

        Map<String, Object> map = new HashMap<String, Object>();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            map.put("stockCenterId", new Integer(stockCenter.getId()));
            map.put("drugId", drugsId);
            map.put("date", theStartDate);
            map.put("dateFormat", dateFormat.format(theStartDate));
            map.put("monthStart", dateFormat.format(theStartDate));
            User localUser = LocalObjects.getUser(getHSession());
            map.put("username", localUser.getUsername());
            map.put("monthEnd", dateFormat.format(theEndDate));
            map.put("dateEnd", theEndDate);
            map.put("stockCenterName", stockCenter.getStockCenterName());
            map.put("path", getReportPath());
            map.put("facilityName", LocalObjects.currentClinic.getClinicName());
            map.put("pharmacist1", LocalObjects.pharmacy.getPharmacist());
            map.put("pharmacist2", LocalObjects.pharmacy.getAssistantPharmacist());
            map.put("dataelaboracao", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            map.put("mes", mesPortugues(theStartDate));
            map.put("mes2", mesPortugues(theEndDate));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }


    @Override
    protected String getReportFileName() {
        return "BalanceteDiario";
    }


    private String mesPortugues(Date data) {

        String mes = "";

        Calendar calendar = new GregorianCalendar();
        Date trialTime = data;
        calendar.setTime(trialTime);


        int mesint = calendar.get(Calendar.MONTH);

        System.out.println(mesint);

        switch (mesint) {
            case 0:
                mes = "Janeiro";
                break;
            case 1:
                mes = "Fevereiro";
                break;
            case 2:
                mes = "Março";
                break;
            case 3:
                mes = "Abril";
                break;
            case 4:
                mes = "Maio";
                break;
            case 5:
                mes = "Junho";
                break;
            case 6:
                mes = "Julho";
                break;
            case 7:
                mes = "Agosto";
                break;
            case 8:
                mes = "Setembro";
                break;
            case 9:
                mes = "Outubro";
                break;
            case 10:
                mes = "Novembro";
                break;
            case 11:
                mes = "Dezembro";
                break;
            default:
                mes = "";
                break;
        }

        return mes;
    }
}