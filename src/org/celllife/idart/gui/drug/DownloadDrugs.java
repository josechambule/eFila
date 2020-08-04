package org.celllife.idart.gui.drug;

import model.manager.DrugManager;
import org.celllife.idart.commonobjects.CentralizationProperties;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.database.hibernate.Drug;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.gui.platform.GenericFormGui;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartColor;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.celllife.idart.rest.utils.RestFarmac;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.util.List;

public class DownloadDrugs extends GenericFormGui {


    private Group grpDrugInfo;

    public CCombo cmbDeseaseType;

    private Label lblStatusDrug;

    private Button rdBtnActive;

    private Button rdBtnInactive;

    private Link lnkSelectAllColumns;

    private CheckboxTableViewer tblColumns;

    private Label lblColumnTableHeader;

    private Label lblClinicTableHeader;

    private Button btnSearch;



    List<Drug> restDrugs = null;


    /**
     * Constructor for GenericFormGui.
     *
     * @param parent Shell
     */
    public DownloadDrugs(Shell parent) {
        super(parent, HibernateUtil.getNewSession());
    }

    /**
     * This method initializes newClinic
     */
    @Override
    protected void createShell() {

        String shellTxt = "Importar Medicamentos";
        Rectangle bounds = new Rectangle(100, 100, 600, 560);

        // Parent Generic Methods ------
        buildShell(shellTxt, bounds); // generic shell build
    }

    @Override
    protected void createContents() {
        createGrpClinicSearch();
        createGrpClinicColumnsSelection();
    }

    @Override
    protected void createCompHeader() {
        String headerTxt = "Importar Medicamentos";
        iDartImage icoImage = iDartImage.DRUG;
        buildCompHeader(headerTxt, icoImage);
        Rectangle bounds = getCompHeader().getBounds();
        bounds.width = 720;
        bounds.x -= 40;
        getCompHeader().setBounds(bounds);
    }

    @Override
    protected void createCompButtons() {
        buildCompButtons();
    }


    /**
     * This method initializes grpClinicInfo
     */
    private void createGrpClinicSearch() {

        // grpDrugInfo
        grpDrugInfo = new Group(getShell(), SWT.NONE);
        grpDrugInfo.setBounds(new Rectangle(33, 70, 500, 180));
        // grpDrugInfo = new Group(getShell(), SWT.NONE);
        // grpDrugInfo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        // grpDrugInfo.setText("Drug Details");
        // grpDrugInfo.setBounds(new Rectangle(18, 110, 483, 293));

        Label lblInstructions = new Label(grpDrugInfo, SWT.CENTER);
        lblInstructions.setBounds(new org.eclipse.swt.graphics.Rectangle(70,
                15, 260, 20));
        lblInstructions.setText("Todos campos marcados com * são obrigatorios");
        lblInstructions.setFont(ResourceUtils
                .getFont(iDartFont.VERASANS_8_ITALIC));

        // lblDeseaseType & cmbDeseaseType
        Label lblDeseaseType = new Label(grpDrugInfo, SWT.NONE);
        lblDeseaseType.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 80, 100, 20));
        lblDeseaseType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblDeseaseType.setText("* Medicamento para :");
        cmbDeseaseType = new CCombo(grpDrugInfo, SWT.BORDER);
        cmbDeseaseType.setBounds(new Rectangle(157, 80, 160, 20));
        cmbDeseaseType.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        cmbDeseaseType.setEditable(false);
        cmbDeseaseType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        CommonObjects.populateDeseases(getHSession(), cmbDeseaseType);

        // lblEstadoDrug, rdBtnActive & rdBtnInactive
        Label lblEstadoDrug = new Label(grpDrugInfo, SWT.NONE);
        lblEstadoDrug.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 110, 80, 20));
        lblEstadoDrug.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblEstadoDrug.setText("* Estado :");

        rdBtnActive = new Button(grpDrugInfo, SWT.RADIO);
        rdBtnActive.setBounds(new Rectangle(150, 110, 50, 20));
        rdBtnActive.setText("Activo");
        rdBtnActive.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        rdBtnActive.setSelection(true);

        rdBtnInactive = new Button(grpDrugInfo, SWT.RADIO);
        rdBtnInactive.setBounds(new Rectangle(257, 110, 80, 20));
        rdBtnInactive.setText("Inactivo");
        rdBtnInactive.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        rdBtnInactive.setSelection(false);

        // btnSearch
        btnSearch = new Button(grpDrugInfo, SWT.NONE);
        btnSearch.setBounds(new Rectangle(157, 140, 152, 30));
        btnSearch.setText("Procurar");
        btnSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(
                    org.eclipse.swt.events.SelectionEvent e) {
                populateRestDrugs();
            }
        });
        btnSearch.setToolTipText("Pressione para procurar Medicamentos.");
    }

    @Override
    protected void clearForm() {
        cmbDeseaseType.setText("Seleccione a Doença");
      rdBtnActive.setSelection(true);
      rdBtnInactive.setSelection(false);
        tblColumns.setAllChecked(false);

    }

    @Override
    protected boolean submitForm() {
        return false;
    }

    @Override
    protected boolean fieldsOk() {
        boolean fieldsOkay = true;

        if (cmbDeseaseType.getText().trim().equals("") || cmbDeseaseType.getText().trim().equals("Seleccione a Doença")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("O campo Medicamento de nao pode estar em branco.");
            b.setText("Campos em branco");
            b.open();
            cmbDeseaseType.setFocus();
            fieldsOkay = false;
        } else if(tblColumns.getCheckedElements().length <= 0){
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage(" Nenhum medicamento foi seleccionado na lista ");
            b.setText("Nenhum medicamento foi seleccionado na lista");
            b.open();
            cmbDeseaseType.setFocus();
            fieldsOkay = false;
        }

        return fieldsOkay;
    }

    @Override
    protected void cmdSaveWidgetSelected() {

        if (fieldsOk()) {

            Transaction tx = null;

            try {
                tx = getHSession().beginTransaction();

                Object[] obj = tblColumns.getCheckedElements();
                for (int i = 0; i < obj.length; i++) {
                    if (obj[i] instanceof Drug) {

                        Drug newDrug = (Drug) obj[i];
                        Drug newDrugSave = DrugManager.getDrug(getHSession(),newDrug.getName());

                        if(newDrugSave != null) {
                            newDrugSave.setAtccode(newDrug.getAtccode());
                            newDrugSave.setActive(newDrug.isActive());
                            DrugManager.updateDrug(getHSession(), newDrugSave);
                        }else
                            DrugManager.saveDrug(getHSession(), newDrug);
                    }
                }
                getHSession().flush();
                tx.commit();
                showMessage( MessageDialog.INFORMATION, "Carregamento com Sucesso",
                        "O processo de carregamento ocorreu com Sucesso.");

            } catch (HibernateException he) {
                showMessage(
                        MessageDialog.ERROR,
                        "Problemas ao gravar a informação",
                        "Problemas ao gravar a informação. Por favor, tente novamente.");
                if (tx != null) {
                    tx.rollback();
                }
                getLog().error(he);
            }
            cmdCancelWidgetSelected();
        }
    }

    @Override
    protected void cmdClearWidgetSelected() {
        clearForm();
    }

    @Override
    protected void cmdCancelWidgetSelected() {
        cmdCloseSelected();
    }

    @Override
    protected void enableFields(boolean enable) {
        cmbDeseaseType.setEnabled(enable);
        rdBtnActive.setEnabled(enable);
        rdBtnInactive.setEnabled(enable);

        cmbDeseaseType.setBackground(enable ? ResourceUtils
                .getColor(iDartColor.WHITE) : ResourceUtils
                .getColor(iDartColor.WIDGET_BACKGROUND));
        btnSave.setEnabled(enable);
    }

    @Override
    protected void setLogger() {

    }

    private void createGrpClinicColumnsSelection() {

        lblClinicTableHeader = new Label(getShell(), SWT.BORDER);
        lblClinicTableHeader.setBounds(new Rectangle(200, 270, 200, 20));
        lblClinicTableHeader.setText("Lista de Medicementos");
        lblClinicTableHeader.setAlignment(SWT.CENTER);
        lblClinicTableHeader.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        lnkSelectAllColumns = new Link(getShell(), SWT.NONE);
        lnkSelectAllColumns.setBounds(new Rectangle(70, 300, 450, 20));
        lnkSelectAllColumns
                .setText("Por favor, seleccione os medicamntos que pretende importar " +
                        "ou <A>Seleccionar todas</A> colunas");
        lnkSelectAllColumns
                .setFont(ResourceUtils.getFont(iDartFont.VERASANS_8_ITALIC));
        lnkSelectAllColumns.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                tblColumns.setAllChecked(true);
            }
        });

        createTblDrug();
    }

    private void createTblDrug() {

      
        tblColumns = CheckboxTableViewer.newCheckList(getShell(), SWT.BORDER);
        tblColumns.getTable().setBounds(
                new org.eclipse.swt.graphics.Rectangle(85, 320, 420, 150));
        tblColumns.getTable().setFont(
                ResourceUtils.getFont(iDartFont.VERASANS_8));
        tblColumns.setContentProvider(new ArrayContentProvider());
    }

    private void populateRestDrugs() {

        String url = CentralizationProperties.centralized_server_url;
        String deseaseType = cmbDeseaseType.getText().replace(" ", "%20");
        boolean status = rdBtnActive.getSelection();
        restDrugs = RestFarmac.restGeAllDrugsByDeseaseTypeAndStatus(url,deseaseType,status, hSession);
        tblColumns.setInput(restDrugs);

        if(restDrugs.isEmpty()){
            btnSave.setEnabled(false);
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage(" Nenhum resultado foi encontrado ");
            b.setText("Nenhum resultado foi encontrado");
            b.open();
        }else
            btnSave.setEnabled(true);
    }

}
