/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.manager.reports;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.eclipse.swt.widgets.Shell;

import model.manager.excel.conversion.exceptions.ReportException;


public class DispensaTrimestral extends AbstractJasperReport {

    private final  Date theEndDate;
    private final Date theStartDate;


    public DispensaTrimestral(Shell parent ,Date theStartDate,
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
        return "PacientesDispensaTrimestral";
    }

    @Override
    protected Map<String, Object> getParameterMap() throws ReportException {
        ConexaoJDBC conn = new ConexaoJDBC();

        // Set the parameters for the report
        Map<String, Object> map = new HashMap<>();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            //Total de pacientes que levantaram arv 20 a 20
            String totalpacientesnovos = String.valueOf(conn.totalPacientesNovosDispensaTrimestral(dateFormat.format(theStartDate), dateFormat.format(theEndDate)));
            System.out.println("Total de pacientes novos dispensa trimestral " + totalpacientesnovos);


            String totalpacientesmanter = String.valueOf(conn.totalPacientesManterDispensaTrimestral(dateFormat.format(theStartDate), dateFormat.format(theEndDate)));
            System.out.println("Total de pacientes a manter arv " + totalpacientesmanter);

            String totalpacientesTransporte = String.valueOf(conn.totalPacientesManuntencaoTransporteDispensaTrimestral(dateFormat.format(theStartDate), dateFormat.format(theEndDate)));
            System.out.println("Total de pacientes a transportar arv " + totalpacientesTransporte);

            String totalpacientesCumulativo = String.valueOf(conn.totalPacientesCumulativoDispensaTrimestral(dateFormat.format(theStartDate), dateFormat.format(theEndDate)));
            System.out.println("Total de pacientes Cumulativo " + totalpacientesCumulativo);
            
            map.put("totalpacientesmanter", totalpacientesmanter);
            map.put("totalpacientesnovos", totalpacientesnovos);
            map.put("totalpacienteManuntencaoTransporte", totalpacientesTransporte);
            map.put("totalpacienteCumulativo", totalpacientesCumulativo);
            map.put("facilityName", LocalObjects.currentClinic.getClinicName());
            map.put("dateStart",  theStartDate);
            map.put("dateEnd", theEndDate);
            map.put("path", getReportPath());
           // map.put("dataelaboracao", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

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
