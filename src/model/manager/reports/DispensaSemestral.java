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

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DispensaSemestral extends AbstractJasperReport {

    private final  Date theEndDate;
    private final Date theStartDate;


    public DispensaSemestral(Shell parent ,Date theStartDate,
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
        return "PacientesDispensaSemestral";
    }

    @Override
    protected Map<String, Object> getParameterMap() throws ReportException {
        ConexaoJDBC conn = new ConexaoJDBC();

        // Set the parameters for the report
        Map<String, Object> map = new HashMap<>();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Map mapaDispensaSemestral = conn.DispensaSemestral(dateFormat.format(theStartDate),dateFormat.format(theEndDate));

            int totalpacientesmanter = Integer.parseInt(mapaDispensaSemestral.get("totalpacientesmanter").toString());
            int totalpacientesnovos = Integer.parseInt(mapaDispensaSemestral.get("totalpacientesnovos").toString());
            int totalpacienteManuntencaoTransporte = Integer.parseInt(mapaDispensaSemestral.get("totalpacienteManuntencaoTransporte").toString());
            int totalpacienteCumulativo = Integer.parseInt(mapaDispensaSemestral.get("totalpacienteCumulativo").toString());

            map.put("totalpacientesnovos", String.valueOf(totalpacientesnovos));
            map.put("totalpacientesmanter", String.valueOf(totalpacientesmanter));
            map.put("totalpacienteManuntencaoTransporte", String.valueOf(totalpacienteManuntencaoTransporte));
            map.put("totalpacienteCumulativo", String.valueOf(totalpacienteCumulativo));
            map.put("facilityName", LocalObjects.currentClinic.getClinicName());
            map.put("dateStart",  theStartDate);
            map.put("dateEnd", theEndDate);
            map.put("path", getReportPath());

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

}
