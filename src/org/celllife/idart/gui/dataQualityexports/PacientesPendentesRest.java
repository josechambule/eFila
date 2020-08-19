package org.celllife.idart.gui.dataQualityexports;

import model.manager.excel.conversion.exceptions.ReportException;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.eclipse.core.runtime.IProgressMonitor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PacientesPendentesRest extends DataQualityBase {

    private final String[] patientHeadings = new String[]{"Pacientes referidos para outras farmacias que ainda não foram enviados \n\n" +
            "Esta lista ilustra todos pacientes sem a confirmação que foi enviado ao servidor central no iDART.\n" +
            "\nNID", "Nome", "Idade", "Sexo", "Data Inicio Tarv", "Contacto", "Farmacia de Referencia"};

    Date date = new Date();

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        setHeadings(patientHeadings);

        data = HibernateUtil
                .getNewSession()
                .createSQLQuery(
                        "select patientid as nid, " +
                            "firstnames ||' '|| lastname as nome, " +
                            "extract(year FROM age(current_date, dateofbirth)) as idade, " +
                            "sex as sexo, " +
                            "datainiciotarv as dataTarv, " +
                            "workphone as contacto, " +
                            "clinicname as referencia " +
                            "from sync_temp_patients " +
                            "where syncstatus = 'P' " +
                            "GROUP BY 1,2,3,4,5,6,7 " +
                            "ORDER BY nid, nome")
                .list();
    }

    @Override
    public String getFileName() {

        return "Monitoria_de_Pacientes_Pendentes."+new SimpleDateFormat("dd-MM-yyyy").format(date);

    }

    @Override
    public void performJob(IProgressMonitor monitor) throws ReportException {
        super.performJob(monitor);

    }

    @Override
    public String getMessage() {
        return "Pacientes referidos que ainda não foram enviados";
    }

}
