/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.manager.reports;

import model.manager.excel.conversion.exceptions.ReportException;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.eclipse.swt.widgets.Shell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author agnaldo
 */
public class LinhaTerapeutica extends AbstractJasperReport {

    private final Date theEndDate;
    private Date theStartDate;

    public LinhaTerapeutica(Shell parent, Date theStartDate,
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
        return "NovasLinhasTerapeuticas";
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
