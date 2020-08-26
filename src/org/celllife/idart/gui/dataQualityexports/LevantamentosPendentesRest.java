package org.celllife.idart.gui.dataQualityexports;

import model.manager.excel.conversion.exceptions.ReportException;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.eclipse.core.runtime.IProgressMonitor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LevantamentosPendentesRest extends DataQualityBase {

    private final String[] patientHeadings = new String[]{"Lista de Levantamentos de pacientes referidos que ainda não foram enviados \n\n" +
            "Esta lista ilustra todos os levantamentos sem a confirmacao que foi enviado ao servidor central no iDART.\n" +
            "Nota: Esta lista apresenta informação por medicamento.\n" +
            "\nNID", "Nome", "Data da Prescricao", "Tipo de Paciente", "Regime Terapeutico", "Linha Terapeutica",
            "Tipo de Dispensa", "Medicamento", "Data Levantamento", "Data Proximo Levantamento", "Farmacia de Referencia"};

    Date date = new Date();

    @SuppressWarnings("unchecked")
    @Override
    public void getData() {
        setHeadings(patientHeadings);

        data = HibernateUtil
                .getNewSession()
                .createSQLQuery(
                        "select patientid as nid, " +
                                "patientfirstname ||' '|| patientlastname as nome, " +
                                "date as dataPrescricao, " +
                                "reasonforupdate as tipoPaciente, " +
                                "regimenome as regimeTerapeutico, " +
                                "linhanome as linhaTerapeutica, " +
                                "CASE   " +
                                "WHEN dispensatrimestral = 1 THEN 'DT' " +
                                "WHEN dispensasemestral = 1 THEN 'DS'  " +
                                "ELSE 'DM' " +
                                "END AS tipodispensa, " +
                                "drugname as medicamento, " +
                                "pickupdate as dataLevantamento, " +
                                "dateexpectedstring as dataProximoLev, " +
                                "mainclinicname as referencia " +
                                "from sync_temp_dispense " +
                                "where syncstatus = 'P' " +
                                "GROUP BY 1,2,3,4,5,6,7,8,9,10,11 " +
                                "ORDER BY pickupdate")
                .list();
    }

    @Override
    public String getFileName() {

        return "Monitoria_de_Levantamentos_Pendentes."+new SimpleDateFormat("dd-MM-yyyy").format(date);

    }

    @Override
    public void performJob(IProgressMonitor monitor) throws ReportException {
        super.performJob(monitor);

    }

    @Override
    public String getMessage() {
        return "Levantamentos que ainda não foram enviados";
    }

}
