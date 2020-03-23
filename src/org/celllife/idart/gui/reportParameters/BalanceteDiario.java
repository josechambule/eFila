package org.celllife.idart.gui.reportParameters;

import model.manager.AdministrationManager;
import model.manager.DrugManager;
import model.manager.reports.BalanceteDiarioReport;
import model.manager.reports.FichaStockReport;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.database.hibernate.Drug;
import org.celllife.idart.database.hibernate.StockCenter;
import org.celllife.idart.gui.platform.GenericReportGui;
import org.celllife.idart.gui.search.Search;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartColor;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.celllife.idart.misc.iDARTUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.vafada.swtcalendar.SWTCalendar;
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BalanceteDiario extends GenericReportGui {

    private Group grpDateRange;

    private SWTCalendar calendarStart;

    private SWTCalendar calendarEnd;

    private Group grpPharmacySelection;

    private CCombo cmbStockCenter;

    private Group grpDrugInfo;

    private Label lblDrugSearch;

    private Text txtDrugSearch;

    private Text txtDrugForm;

    private Text txtPackSize;

    private Button btnSearch;

    private Label lblPackSize;

    private Drug localDrug;

    private List<Drug> drugList;


    /**
     * Constructor
     *
     * @param parent
     *            Shell
     * @param activate
     *            boolean
     */
    public BalanceteDiario(Shell parent, boolean activate) {
        super(parent, REPORTTYPE_STOCK, activate);
    }

    /**
     * This method initializes newMonthlyStockOverview
     */
    @Override
    protected void createShell() {
        Rectangle bounds = new Rectangle(100, 50, 600, 510);
        buildShell(REPORT_ARV_FICHA_STOCK, bounds);
        // create the composites
        createMyGroups();
    }

    private void createMyGroups() {
        createGrpClinicSelection();
        createGrpDateInfo();
        createGrpDrugInfo();

    }

    /**
     * This method initializes compHeader
     *
     */
    @Override
    protected void createCompHeader() {
        iDartImage icoImage = iDartImage.REPORT_STOCKCONTROLPERCLINIC;
        buildCompdHeader(REPORT_ARV_FICHA_STOCK, icoImage);
    }

    /**
     * This method initializes grpClinicSelection
     *
     */
    private void createGrpClinicSelection() {

        grpPharmacySelection = new Group(getShell(), SWT.NONE);
        grpPharmacySelection.setText("Farmácia");
        grpPharmacySelection.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        grpPharmacySelection.setBounds(new org.eclipse.swt.graphics.Rectangle(140, 40, 320, 65));

        Label lblPharmacy = new Label(grpPharmacySelection, SWT.NONE);
        lblPharmacy.setBounds(new Rectangle(10, 25, 140, 20));
        lblPharmacy.setText("Seleccione a Farmácia");
        lblPharmacy.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        cmbStockCenter = new CCombo(grpPharmacySelection, SWT.BORDER);
        cmbStockCenter.setBounds(new Rectangle(156, 24, 160, 20));
        cmbStockCenter.setEditable(false);
        cmbStockCenter.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        cmbStockCenter.setBackground(ResourceUtils.getColor(iDartColor.WHITE));

        CommonObjects.populateStockCenters(getHSession(), cmbStockCenter);

    }

    /**
     * This method initializes grpDateInfo
     *
     */
    private void createGrpDateInfo() {

        createGrpDateRange();
    }

    /**
     * This method initializes compButtons
     *
     */
    @Override
    protected void createCompButtons() {
    }

    @SuppressWarnings("unused")
    @Override
    protected void cmdViewReportWidgetSelected() {

        StockCenter pharm = AdministrationManager.getStockCenter(getHSession(),cmbStockCenter.getText());

        if (cmbStockCenter.getText().equals("")) {

            MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            missing.setText("No Pharmacy Was Selected");
            missing.setMessage("No pharmacy was selected. Please select a pharmacy by looking through the list of available pharmacies.");
            missing.open();

        } else if (pharm == null) {

            MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            missing.setText("Pharmacy not found");
            missing.setMessage("There is no pharmacy called '"
                    + cmbStockCenter.getText()
                    + "' in the database. Please select a pharmacy by looking through the list of available pharmacies.");
            missing.open();

        }else if (localDrug == null) {

            MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            missing.setText("O medicamento nao foi seleccionado");
            missing.setMessage("Nao existe nehum medicamento seleccionado '");
            missing.open();

        }

        else

        if (iDARTUtil.before(calendarEnd.getCalendar().getTime(), calendarStart.getCalendar().getTime())){
            showMessage(MessageDialog.ERROR, "End date before start date","You have selected an end date that is before the start date.\nPlease select an end date after the start date.");
            return;
        }

        else {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");

                Date theStartDate = calendarStart.getCalendar().getTime();

                Date theEndDate=  calendarEnd.getCalendar().getTime();

                BalanceteDiarioReport report = new BalanceteDiarioReport(getShell(), pharm, theStartDate, theEndDate,localDrug.getId());
                viewReport(report);
            } catch (Exception e) {
                getLog().error("Exception while running Monthly Receipts and Issues report",e);
            }
        }

    }

    /**
     * This method is called when the user presses "Close" button
     *
     */
    @Override
    protected void cmdCloseWidgetSelected() {
        cmdCloseSelected();
    }

    /**
     * Method getMonthName.
     *
     * @param intMonth
     *            int
     * @return String
     */
    private String getMonthName(int intMonth) {

        String strMonth = "unknown";

        SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");

        try {
            Date theDate = sdf2.parse(intMonth + "");
            strMonth = sdf1.format(theDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return strMonth;

    }

    @Override
    protected void setLogger() {
        setLog(Logger.getLogger(this.getClass()));
    }


    private void createGrpDateRange() {
        grpDateRange = new Group(getShell(), SWT.NONE);
        grpDateRange.setText("Período:");
        grpDateRange.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        grpDateRange.setBounds(new Rectangle(55, 100, 520, 201));
        grpDateRange.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        Label lblStartDate = new Label(grpDateRange, SWT.CENTER | SWT.BORDER);
        lblStartDate.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 30,180, 20));
        lblStartDate.setText("Data Início:");
        lblStartDate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        Label lblEndDate = new Label(grpDateRange, SWT.CENTER | SWT.BORDER);
        lblEndDate.setBounds(new org.eclipse.swt.graphics.Rectangle(300, 30,180, 20));
        lblEndDate.setText("Data Fim:");
        lblEndDate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        calendarStart = new SWTCalendar(grpDateRange);
        calendarStart.setBounds(20, 55, 220, 140);

        calendarEnd = new SWTCalendar(grpDateRange);
        calendarEnd.setBounds(280, 55, 220, 140);
        calendarEnd.addSWTCalendarListener(new SWTCalendarListener() {
            @Override
            public void dateChanged(SWTCalendarEvent calendarEvent) {
                Date date = calendarEvent.getCalendar().getTime();
            }
        });
    }

    /**
     * This method initialises grpDrugInfo
     *
     */
    private void createGrpDrugInfo() {

        if (grpDrugInfo != null) {
            grpDrugInfo.dispose();
        }

        grpDrugInfo = new Group(getShell(), SWT.NONE);
        grpDrugInfo.setText("Detalhes do Medicamento");
        grpDrugInfo.setBounds(new Rectangle(55, 300, 520, 90));
        grpDrugInfo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        lblDrugSearch = new Label(grpDrugInfo, SWT.NONE);
        lblDrugSearch.setBounds(new Rectangle(15, 20, 130, 20));
        lblDrugSearch.setText("Medicamento:");
        lblDrugSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtDrugSearch = new Text(grpDrugInfo, SWT.BORDER);
        txtDrugSearch.setBounds(new Rectangle(155, 18, 200, 20));
        txtDrugSearch.setEditable(false);
        txtDrugSearch.setEnabled(false);
        txtDrugSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        btnSearch = new Button(grpDrugInfo, SWT.NONE);
        btnSearch.setBounds(new Rectangle(365, 12, 100, 28));
        btnSearch.setText("Procurar");
        btnSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        btnSearch
                .setToolTipText("Pressione este botão para procurar por um medicamento.");

        btnSearch.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                cmdDrugSearchWidgetSelected();
            }
        });


        btnSearch.setEnabled(true);
        lblDrugSearch.setEnabled(true);
        txtDrugSearch.setEnabled(true);

        lblPackSize = new Label(grpDrugInfo, SWT.NONE);
        lblPackSize.setBounds(new Rectangle(15, 45, 130, 20));
        lblPackSize.setText("Conteúdo/Frasco:");
        lblPackSize.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtPackSize = new Text(grpDrugInfo, SWT.BORDER);
        txtPackSize.setBounds(new Rectangle(155, 44, 200, 20));
        txtPackSize.setEditable(false);
        txtPackSize.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtDrugForm = new Text(grpDrugInfo, SWT.BORDER);
        txtDrugForm.setBounds(new Rectangle(365, 44, 100, 20));
        txtDrugForm.setEditable(false);
        txtDrugForm.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

    }


    private void cmdDrugSearchWidgetSelected() {


        Search drugSearch = new Search(getHSession(), getShell(),
                CommonObjects.DRUG, false);

        if (drugSearch.getValueSelected() != null) {

            // load Drugs
            localDrug = DrugManager.getDrug(getHSession(), drugSearch
                    .getValueSelected()[0]);
            txtDrugSearch.setText(localDrug.getName());
            txtDrugForm.setText(localDrug.getForm().getFormLanguage1());
            txtPackSize.setText(String.valueOf(localDrug.getPackSize()));

        }

    }

    /**
     * Method getCalendarEnd.
     *
     * @return Calendar
     */
    public Calendar getCalendarEnd() {
        return calendarEnd.getCalendar();
    }

    /**
     * Method setEndDate.
     *
     * @param date
     *            Date
     */
    public void setEndtDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendarEnd.setCalendar(calendar);
    }

    /**
     * Method addEndDateChangedListener.
     *
     * @param listener
     *            SWTCalendarListener
     */
    public void addEndDateChangedListener(SWTCalendarListener listener) {

        calendarEnd.addSWTCalendarListener(listener);
    }

    /**
     * Method getCalendarStart.
     *
     * @return Calendar
     */
    public Calendar getCalendarStart() {
        return calendarStart.getCalendar();
    }

    /**
     * Method setStartDate.
     *
     * @param date
     *            Date
     */
    public void setStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendarStart.setCalendar(calendar);
    }

    /**
     * Method addStartDateChangedListener.
     *
     * @param listener
     *            SWTCalendarListener
     */
    public void addStartDateChangedListener(SWTCalendarListener listener) {

        calendarStart.addSWTCalendarListener(listener);
    }

    @Override
    protected void cmdViewReportXlsWidgetSelected() {
        // TODO Auto-generated method stub

    }
}
