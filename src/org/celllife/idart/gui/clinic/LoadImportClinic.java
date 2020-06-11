package org.celllife.idart.gui.clinic;

import model.manager.AdministrationManager;
import org.celllife.idart.commonobjects.CentralizationProperties;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.database.hibernate.Clinic;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class LoadImportClinic extends GenericFormGui {


    private Group grpClinicSearch;

    private Group grpClinics;

    public CCombo cmbFacilityType;

    public CCombo cmbProvince;

    public CCombo cmbDistrict;

    private Link lnkSelectAllColumns;

    private CheckboxTableViewer tblColumns;

    private Button btnSearch;

    private Label lblColumnTableHeader;

    private Label lblClinicTableHeader;

    List<Clinic> restClinics = null;


    /**
     * Constructor for GenericFormGui.
     *
     * @param parent Shell
     */
    public LoadImportClinic(Shell parent) {
        super(parent, HibernateUtil.getNewSession());
    }

    /**
     * This method initializes newClinic
     */
    @Override
    protected void createShell() {

        String shellTxt = "Importar Farmácias";
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
        String headerTxt = "Importar Farmácias";
        iDartImage icoImage = iDartImage.CLINIC;
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

        // grpClinicInfo
        grpClinicSearch = new Group(getShell(), SWT.NONE);
        grpClinicSearch.setBounds(new Rectangle(73, 70, 500, 410));
        // grpDrugInfo = new Group(getShell(), SWT.NONE);
        // grpDrugInfo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        // grpDrugInfo.setText("Drug Details");
        // grpDrugInfo.setBounds(new Rectangle(18, 110, 483, 293));

        Label lblInstructions = new Label(grpClinicSearch, SWT.CENTER);
        lblInstructions.setBounds(new org.eclipse.swt.graphics.Rectangle(70,
                15, 260, 20));
        lblInstructions.setText("Todos campos marcados com * são obrigatorios");
        lblInstructions.setFont(ResourceUtils
                .getFont(iDartFont.VERASANS_10_ITALIC));

        // lblProvince & cmdProvince
        Label lblProvince = new Label(grpClinicSearch, SWT.NONE);
        lblProvince.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 80, 100, 20));
        lblProvince.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblProvince.setText("* Província:");
        cmbProvince = new CCombo(grpClinicSearch, SWT.BORDER);
        cmbProvince.setBounds(new Rectangle(157, 80, 160, 20));
        cmbProvince.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        cmbProvince.setEditable(false);
        cmbProvince.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        CommonObjects.populateProvinces(getHSession(), cmbProvince);

        cmbProvince.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                populateDistrict();
            }
        });

        // lblDistrict & cmbDistrict
        Label lblDistrict = new Label(grpClinicSearch, SWT.NONE);
        lblDistrict.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 110, 100, 20));
        lblDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblDistrict.setText("* Distrito:");
        cmbDistrict = new CCombo(grpClinicSearch, SWT.BORDER);
        cmbDistrict.setBounds(new Rectangle(157, 110, 160, 20));
        cmbDistrict.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        cmbDistrict.setEditable(false);
        cmbDistrict.setText("Seleccione o Distrito");
        cmbDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        cmbDistrict.setEnabled(false);

        // lblClinicFacilityType & cmbClinicFacilityType
        Label lblClinicFacilityType = new Label(grpClinicSearch, SWT.NONE);
        lblClinicFacilityType.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 140, 100, 20));
        lblClinicFacilityType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblClinicFacilityType.setText("* Tipo de Farmácia:");
        cmbFacilityType = new CCombo(grpClinicSearch, SWT.BORDER);
        cmbFacilityType.setBounds(new Rectangle(157, 140, 160, 20));
        cmbFacilityType.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        cmbFacilityType.setEditable(false);
        cmbFacilityType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        CommonObjects.populateFacilityTypes(getHSession(), cmbFacilityType);

        // btnSearch
        btnSearch = new Button(grpClinicSearch, SWT.NONE);
        btnSearch.setBounds(new Rectangle(157, 170, 152, 30));
        btnSearch.setText("Procurar");
        btnSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(
                    org.eclipse.swt.events.SelectionEvent e) {
                populateRestClinics();
            }
        });
        btnSearch.setToolTipText("Pressione para procurar uma Farmácia existente .");
    }

    @Override
    protected void clearForm() {
        cmbDistrict.setText("Seleccione o Distrito");
        cmbDistrict.setEnabled(false);
        cmbFacilityType.setText("Selecione ...");
        cmbProvince.setText("Seleccione a Província");
        tblColumns.setAllChecked(false);

    }

    @Override
    protected boolean submitForm() {
        return false;
    }

    @Override
    protected boolean fieldsOk() {
        boolean fieldsOkay = true;

        if (cmbProvince.getText().trim().equals("") || cmbProvince.getText().trim().equals("Seleccione a Província")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("O campo provincia da farmacia nao pode estar em branco.");
            b.setText("Campos em branco");
            b.open();
            cmbProvince.setFocus();
            fieldsOkay = false;
        } else if (cmbDistrict.getText().trim().equals("") || cmbDistrict.getText().trim().equals("Seleccione o Distrito")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("O campo distrito da farmacia nao pode estar em branco.");
            b.setText("Campos em branco");
            b.open();
            cmbDistrict.setFocus();
            fieldsOkay = false;
        } else if (cmbFacilityType.getText().trim().equals("")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage(" O campo tipo de farmacia nao pode estar em branco.");
            b.setText("Campos em branco");
            b.open();
            cmbDistrict.setFocus();
            fieldsOkay = false;
        } else if(tblColumns.getCheckedElements().length <= 0){
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage(" Nenhuma farmacia seleccionada na lista ");
            b.setText("Nenhuma farmacia seleccionada na lista");
            b.open();
            cmbDistrict.setFocus();
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
                    if (obj[i] instanceof Clinic) {

                        Clinic newClinic = (Clinic) obj[i];

                        AdministrationManager.saveClinic(getHSession(), newClinic);
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
        cmbProvince.setEnabled(enable);
        cmbDistrict.setEnabled(enable);
        cmbFacilityType.setEnabled(enable);

        cmbProvince.setBackground(enable ? ResourceUtils
                .getColor(iDartColor.WHITE) : ResourceUtils
                .getColor(iDartColor.WIDGET_BACKGROUND));
        btnSave.setEnabled(enable);
    }

    @Override
    protected void setLogger() {

    }


    private void populateDistrict() {
        if (!cmbProvince.getText().equals("Seleccione a Província")) {
            cmbDistrict.setEnabled(true);
            cmbDistrict.removeAll();
            List<String> ncList = new ArrayList<String>();
            ncList = AdministrationManager.getDistrict(getHSession(),
                    cmbProvince.getText().trim());
            if (ncList != null) {
                for (String s : ncList) {
                    if (s != null) {
                        cmbDistrict.add(s);
                    }
                }
            }
            if (cmbDistrict.getItemCount() > 0) {
                // Set the default to the first item in the combo box
                cmbDistrict.setText("Seleccione o Distrito");
            }
        }
    }


    private void createGrpClinicColumnsSelection() {

        lnkSelectAllColumns = new Link(getShell(), SWT.NONE);
        lnkSelectAllColumns.setBounds(new Rectangle(130, 300, 350, 20));
        lnkSelectAllColumns
                .setText("Por favor, seleccione as farmácias que pretende importar " +
                        "ou <A>Seleccionar todas</A> colunas");
        lnkSelectAllColumns
                .setFont(ResourceUtils.getFont(iDartFont.VERASANS_8_ITALIC));
        lnkSelectAllColumns.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                tblColumns.setAllChecked(true);
            }
        });

        createTblClinic();
    }

    private void createTblClinic() {

        lblClinicTableHeader = new Label(getShell(), SWT.BORDER);
        lblClinicTableHeader.setBounds(new Rectangle(200, 280, 200, 20));
        lblClinicTableHeader.setText("Lista de Farmácias");
        lblClinicTableHeader.setAlignment(SWT.CENTER);
        lblClinicTableHeader.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        tblColumns = CheckboxTableViewer.newCheckList(getShell(), SWT.BORDER);
        tblColumns.getTable().setBounds(
                new org.eclipse.swt.graphics.Rectangle(115, 320, 420, 150));
        tblColumns.getTable().setFont(
                ResourceUtils.getFont(iDartFont.VERASANS_8));
        tblColumns.setContentProvider(new ArrayContentProvider());
    }

    private void populateRestClinics() {

        String url = CentralizationProperties.centralized_server_url;
        String province = cmbProvince.getText().replace(" ", "%20");
        String district = cmbDistrict.getText().replace(" ", "%20");
        String facilitytype = cmbFacilityType.getText().replace(" ", "%20");
        restClinics = RestFarmac.restGeAllClinicByProvinceAndDistrictAndFacilityType(url, province, district, facilitytype, hSession);
        tblColumns.setInput(restClinics);

        if(restClinics.isEmpty()){
            btnSave.setEnabled(false);
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage(" Nenhuma resultado foi encontrado ");
            b.setText("Nenhuma resultado foi encontrado");
            b.open();
        }else
            btnSave.setEnabled(true);
    }

}
