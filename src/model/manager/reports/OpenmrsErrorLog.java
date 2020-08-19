package model.manager.reports;

import model.manager.excel.conversion.exceptions.ReportException;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.eclipse.swt.widgets.Shell;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpenmrsErrorLog extends AbstractJasperReport {

    private final Date theEndDate;
    private Date theStartDate;

    public OpenmrsErrorLog(Shell parent, Date theStartDate,
                            Date theEndDate) {
        super(parent);
        this.theStartDate = theStartDate;
        this.theEndDate = theEndDate;
    }

    @Override
    protected void generateData() throws ReportException {

    }

    @Override
    protected String getReportFileName() {
        return "OpenmrsErrorLog";
    }

    @Override
    protected Map<String, Object> getParameterMap() throws ReportException {
        ConexaoJDBC conn = new ConexaoJDBC();

        // Set the parameters for the report
        Map<String, Object> map = new HashMap<>();

        map.put("facilityName", LocalObjects.currentClinic.getClinicName());
        map.put("date", theStartDate);
        map.put("endDate", theEndDate);

        return map;
    }

}
