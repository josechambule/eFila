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

            //Total de pacientes que levantaram arv 20 a 20
            int totalpacientesnovos= conn.totalPacientesNovosDispensaSemestral(dateFormat.format(theStartDate), dateFormat.format(theEndDate));
            System.out.println("Total de pacientes novos dispensa semestral " + totalpacientesnovos);

            int totalpacientesmanter = conn.totalPacientesManterDispensaSemestral(dateFormat.format(theStartDate), dateFormat.format(theEndDate));
            System.out.println("Total de pacientes a manter arv " + totalpacientesmanter);

            int totalpacientesTransporte = conn.totalPacientesManuntencaoTransporteDispensaSemestral(dateFormat.format(theStartDate), dateFormat.format(theEndDate));
            System.out.println("Total de pacientes a transportar arv " + totalpacientesTransporte);

//          int totalpacientesCumulativo = conn.totalPacientesCumulativoDispensaSemestral(dateFormat.format(theStartDate), dateFormat.format(theEndDate));
            int totalpacientesCumulativo = totalpacientesnovos + totalpacientesmanter + totalpacientesTransporte;
          
            System.out.println("Total de pacientes Cumulativo " + totalpacientesCumulativo);
            
            map.put("totalpacientesmanter", String.valueOf(totalpacientesmanter));
            map.put("totalpacientesnovos", String.valueOf(totalpacientesnovos));
            map.put("totalpacienteManuntencaoTransporte", String.valueOf(totalpacientesTransporte));
            map.put("totalpacienteCumulativo", String.valueOf(totalpacientesCumulativo));
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
