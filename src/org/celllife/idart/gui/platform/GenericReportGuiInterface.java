package org.celllife.idart.gui.platform;

import org.celllife.idart.messages.Messages;


/**
 *
 */
public interface GenericReportGuiInterface {

    static String OPTION_reportType = "reportType"; //$NON-NLS-1$
    static String OPTION_packageStage = "packageStage"; //$NON-NLS-1$

    static int REPORTTYPE_PATIENT = 1, REPORTTYPE_CLINICMANAGEMENT = 2,
            REPORTTYPE_STOCK = 3, REPORTTYPE_MONITORINGANDEVALUATION = 4, REPORTTYPE_MIA = 5, REPORTTYPE_T_TARV = 6;
    ;

    static String REPORT_PATIENT_HISTORY = Messages.getString("reports.patientHistory");  //$NON-NLS-1$
    static String REPORT_PEPFAR = Messages.getString("reports.pepfar"); //$NON-NLS-1$
    static String REPORT_MISSED_APPOINTMENTS = Messages.getString("reports.missedAppointments");  //$NON-NLS-1$
    static String REPORT_DAILY_DISPENSING_TOTALS = Messages.getString("reports.dailyDispensingTotals");  //$NON-NLS-1$
    static String REPORT_TRANSACTION_LOG = Messages.getString("reports.transactionLog");  //$NON-NLS-1$
    static String REPORT_PRESCRIBING_DOCTORS = Messages.getString("reports.prescribingDoctors");  //$NON-NLS-1$
    static String REPORT_PACKAGE_TRACKING = Messages.getString("reports.pacakgeTracking"); //$NON-NLS-1$
    static String REPORT_MONTHLY_STOCK_RECEIPTS = Messages.getString("reports.monthlyStockReceipts");  //$NON-NLS-1$
    static String REPORT_CLINIC_INDICATORS = Messages.getString("reports.clinicIndicators");  //$NON-NLS-1$
    static String REPORT_PATIENTS_EXPECTED_ON_A_DAY = Messages.getString("reports.patientsExpectedOnADay");  //$NON-NLS-1$
    static String REPORT_ARV_DRUG_USAGE = Messages.getString("reports.ARVDrugUsage");  //$NON-NLS-1$
    static String REPORT_ARV_FICHA_STOCK = Messages.getString("reports.fichaStock");  //$NON-NLS-1$
    static String REPORT_ARV_BALANCETE_DIARIO = Messages.getString("reports.balanceteDiario");  //$NON-NLS-1$
    static String REPORT_DRUGS_DISPENSED = Messages.getString("reports.drugsDispensed");  //$NON-NLS-1$
    static String REPORT_MONTHLY_RECEIPT_ISSUE = Messages.getString("reports.montlyIssuesAndReceipts");  //$NON-NLS-1$
    static String REPORT_STOCK_TAKE = Messages.getString("reports.stockTake");  //$NON-NLS-1$
    static String REPORT_PACKAGES_CREATED = Messages.getString("reports.packagesCreated");  //$NON-NLS-1$
    static String REPORT_PACKAGES_LEAVING = Messages.getString("reports.packageLeaving");  //$NON-NLS-1$
    static String REPORT_PACKAGES_RECEIVED = Messages.getString("reports.packagesReceivedAtClinic");  //$NON-NLS-1$
    static String REPORT_PACKAGES_RETURNED = Messages.getString("reports.packagesReturned");  //$NON-NLS-1$
    static String REPORT_PACKAGES_COLLECTED = Messages.getString("reports.packagesCollected");  //$NON-NLS-1$
    static String REPORT_PACKAGES_AWAITING_PICKUP = Messages.getString("reports.packagesAwaiting");  //$NON-NLS-1$
    static String REPORT_PACKAGES_SCANNED_OUT = Messages.getString("reports.packagesScannedOut");  //$NON-NLS-1$
    static String REPORT_EPISODES_STARTED_OR_ENDED = Messages.getString("reports.episodesStartedAndEnded");  //$NON-NLS-1$
    static String REPORT_EPISODES_STATS = Messages.getString("reports.episodeStatistics");  //$NON-NLS-1$
    static String REPORT_DRUG_COMBINATIONS = Messages.getString("reports.drugCombinations");  //$NON-NLS-1$
    static String REPORT_COHORT_COLLECTIONS = Messages.getString("reports.cohortCollections");  //$NON-NLS-1$
    static String REPORT_MIA = Messages.getString("reports.mmia");  //$NON-NLS-1$
    static String REPORT_MIAMISAU = Messages.getString("reports.mmiamisau");  //$NON-NLS-1$
    static String REPORT_PRESCRICOES_SEM_DISPENSAS = Messages.getString("reports.prescricoes.dispensa");
    static String REPORT_MMIA = Messages.getString("reports.mmmia");  //$NON-NLS-1$
    static String REPORT_IDART = Messages.getString("reports.idart");  //$NON-NLS-1$
    static String REPORT_TPC = Messages.getString("reports.idart.tpc");  //$NON-NLS-1$
    static String REPORT_TPI = Messages.getString("reports.idart.tpi");  //$NON-NLS-1$
    static String REPORT_LEVANTAMENTOS_ARV = Messages.getString("reports.idart.levantamentos");  //$NON-NLS-1$
    static String REPORT_REFERIDOS_LEVANTAMENTOS_ARV = Messages.getString("reports.idart.referidos.levantamentos");  //$NON-NLS-1$
    static String REPORT_LINHAS_TERAPEUTICAS = Messages.getString("reports.LinhasTerapeuticas");
    static String REPORT_FARMACIAS_REGISTADAS = Messages.getString("reports.farmacias.registadas");
    static String REPORT_MISSED_APPOINTMENTS_DT = Messages.getString("reports.missedAppointmentsDT");
    static String REPORT_MISSED_APPOINTMENTS_DS = Messages.getString("reports.missedAppointmentsDS");
    static String REPORT_FILA_GERAL = Messages.getString("reports.fila");
    static String REPORT_PACIENTES_REFERIDOS = Messages.getString("reports.referidos");
    static String REPORT_PACIENTES_RECEBIDOS = Messages.getString("reports.recebidos");
    static String REPORT_SECOND_LINE = Messages.getString("reports.secondLine");
    static String REPORT_LOST_TO_FOLLOW_UP = Messages.getString("reports.losttofollowup");
    static String REPORT_LOST_TO_FOLLOW_UP_RETURNED = Messages.getString("reports.losttofollowupreturned");
    static String REPORT_SEGUNDA_LINHA_AVALIACAO = Messages.getString("reports.segundaslinhasavaliacao");
    static String REPORT_LIVRO_ELETRONICO_ARV = Messages.getString("reports.idart.lrda");
    static String REPORT_CONFIRMACA_ABANDONOS_OPENMRS = Messages.getString("reports.confirmaabandonos");
    static String REPORT_INDICADORES_MENSAIS = Messages.getString("reports.indicadoresmensais");
    static String REPORT_LISTA_PACIENTES_SEMDT = Messages.getString("reports.pacientesSemDT");
    static String REPORT_COHORT_DISPENSA_TRIMESTRAL = Messages.getString("reports.cohortDT");
    static String REPORT_DISPENSA_TRIMESTRAL = Messages.getString("reports.DispensaTrimestral");
    static String REPORT_DISPENSA_SEMESTRAL = Messages.getString("reports.DispensaSemestral");
    static String REPORT_MISSED_APPOINTMENTS_NEW = Messages.getString("reports.missedAppointmentsNew");  //$NON-NLS-1$
    static String REPORT_MISSED_APPOINTMENTS_CHAMADAS = Messages.getString("reports.missedAppointmentsChamadas");  //$NON-NLS-1$
    static String REPORT_MISSED_APPOINTMENTS_APSS = Messages.getString("reports.missedAppointmentsApss");  //$NON-NLS-1$

}
