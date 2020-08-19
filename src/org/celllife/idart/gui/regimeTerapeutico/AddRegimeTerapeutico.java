/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.celllife.idart.gui.regimeTerapeutico;

import model.manager.DeletionsManager;
import model.manager.DrugManager;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.database.hibernate.Drug;
import org.celllife.idart.database.hibernate.Form;
import org.celllife.idart.database.hibernate.RegimeTerapeutico;
import org.celllife.idart.database.hibernate.RegimenDrugs;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.gui.platform.GenericFormGui;
import org.celllife.idart.gui.prescription.PrescriptionObject;
import org.celllife.idart.gui.search.Search;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartColor;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.celllife.idart.messages.Messages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author colaco.nhango
 */
public class AddRegimeTerapeutico extends GenericFormGui {

    private Button btnAddDrug;

    private Button btnSearch;

    private Button btnSaveDrugGroup;

    private TableColumn clmAmt;

    private TableColumn clmDrugName;

    private TableColumn clmSpace;

    private TableColumn clmTake;

    private Composite compButtonsMiddle;

    private Group grpDrugs;

    private Group grpRegimen;

    private int intDrugTableSize = 1;

    private Label lblInstructions;

    private Label lblDrugGroupName;

    private Label lblRegimeEspecifico;

    private Label lblCodigoRegime;

    private Label lblEstadoRegime;

    private Button rdBtnActive;

    private Button rdBtnInactive;

    private RegimeTerapeutico localRegimen;

    private TableColumn tblDescription;

    private Table tblDrugs;

    private TableColumn tblPerDay;

    private TableColumn tblTPD;

    private Text txtDrugGroupName;

    private Text txtRegimeEspecifico;

    private Text txtCodigoRegime;

    private Text txtRegimeEsquemaIdart;

    private Composite compInstructions;

    private Label lblPicAddDrug;

    private boolean isAddnotUpdate;

    private Button btnRemoveDrug;

    /**
     * Constructor
     *
     * @param parent Shell
     */
    public AddRegimeTerapeutico(Shell parent) {
        super(parent, HibernateUtil.getNewSession());
    }

    /**
     * This method initializes newAddRegimen
     */
    @Override
    protected void createShell() {
        isAddnotUpdate = ((Boolean) getInitialisationOption(OPTION_isAddNotUpdate))
                .booleanValue();
        // The GenericFormsGui class needs
        // Header text, icon URL, shell bounds
        String shellTxt = isAddnotUpdate ? Messages.getString("adddruggroup.icon.header") //$NON-NLS-1$
                : Messages.getString("adddruggroup.url.header"); //$NON-NLS-1$
        Rectangle bounds = new Rectangle(25, 0, 800, 700);
        // Parent Generic Methods ------
        buildShell(shellTxt, bounds); // generic shell build
    }

    @Override
    protected void createContents() {
        createCompInstructions();
        createCompButtonsMiddle();
        createRegimenGroup();
        createGrpDrugs();

        enableFields(true);

    }

    /**
     * This method initializes compHeader
     */
    @Override
    protected void createCompHeader() {
        String headerTxt = (isAddnotUpdate ? Messages.getString("adddruggroup.icon.header") //$NON-NLS-1$
                : Messages.getString("adddruggroup.url.header")); //$NON-NLS-1$
        iDartImage icoImage = iDartImage.DRUGGROUP;
        buildCompHeader(headerTxt, icoImage);
    }

    @Override
    protected void createCompButtons() {
        // Parent Class generic call
        buildCompButtons();
        btnSaveDrugGroup = btnSave;
        btnSaveDrugGroup.setText(Messages.getString("adddruggroup.group.savedrug.title")); //$NON-NLS-1$
    }

    /**
     * This method initializes compInstructions
     */
    private void createCompInstructions() {
        compInstructions = new Composite(getShell(), SWT.NONE);
        compInstructions.setBounds(new Rectangle(250, 80, 300, 20));

        lblInstructions = new Label(compInstructions, SWT.CENTER);
        lblInstructions.setBounds(new org.eclipse.swt.graphics.Rectangle(0, 1, 300, 18));
        lblInstructions.setText(Messages.getString("adddruggroup.label.instructions.title")); //$NON-NLS-1$
        lblInstructions.setFont(ResourceUtils
                .getFont(iDartFont.VERASANS_10_ITALIC));
    }

    /**
     * This method initializes grpParticulars
     */
    private void createRegimenGroup() {

        // grpParticulars
        grpRegimen = new Group(getShell(), SWT.NONE);
        grpRegimen.setBounds(new Rectangle(100, 100, 600, 150));
        grpRegimen.setText(Messages.getString("adddruggroup.label.regimen.title")); //$NON-NLS-1$
        grpRegimen.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        // Codigo do Regime FNM
        lblCodigoRegime = new Label(grpRegimen, SWT.NONE);
        lblCodigoRegime.setBounds(new Rectangle(30, 20, 110, 20));
        lblCodigoRegime.setText(Messages.getString("adddruggroup.label.codigo.regime.title")); //$NON-NLS-1$
        lblCodigoRegime.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtCodigoRegime = new Text(grpRegimen, SWT.BORDER);
        txtCodigoRegime.setBounds(new Rectangle(160, 20, 220, 20));
        txtCodigoRegime.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        txtCodigoRegime.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.character == SWT.CR) {
                    //cmdSearchWidgetSelected();
                }
            }
        });

//        txtCodigoRegime.setFocus();

        // Regime Terapeutico
        lblDrugGroupName = new Label(grpRegimen, SWT.NONE);
        lblDrugGroupName.setBounds(new Rectangle(30, 45, 110, 20));
        lblDrugGroupName.setText(Messages.getString("adddruggroup.label.drugname.title")); //$NON-NLS-1$
        lblDrugGroupName.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtDrugGroupName = new Text(grpRegimen, SWT.BORDER);
        txtDrugGroupName.setBounds(new Rectangle(160, 45, 220, 20));
        txtDrugGroupName.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
//        txtDrugGroupName.addFocusListener(new FocusListener() {
//            public void focusGained(FocusEvent e) {
//            }
//
//            @Override
//            public void focusLost(FocusEvent focusEvent) {
//                cmdSearchWidgetSelected();
//            }
//        });

        btnSearch = new Button(grpRegimen, SWT.NONE);
        btnSearch.setBounds(new Rectangle(390, 45, 160, 25));
        btnSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(
                    org.eclipse.swt.events.SelectionEvent e) {
                cmdSearchWidgetSelected();
            }
        });

        // if this is adding a new regimen, set
        if (!isAddnotUpdate) {
            btnSearch.setEnabled(true);
            btnSearch.setText(Messages.getString("adddruggroup.button.drugadd.title")); //$NON-NLS-1$
            btnSearch.setToolTipText(Messages.getString("adddruggroup.button.drugadd.tooltip")); //$NON-NLS-1$
        }else {
            btnSearch.setVisible(false);
        }

        // Estado do Regime Terapeutico
        lblEstadoRegime = new Label(grpRegimen, SWT.NONE);
        lblEstadoRegime.setBounds(new org.eclipse.swt.graphics.Rectangle(30, 70, 110, 20));
        lblEstadoRegime.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblEstadoRegime.setText(Messages.getString("adddruggroup.estado.regime.title")); //$NON-NLS-1$

        rdBtnActive = new Button(grpRegimen, SWT.RADIO);
        rdBtnActive.setBounds(new Rectangle(160, 70, 80, 20));
        rdBtnActive.setText("Activo");
        rdBtnActive.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        rdBtnActive.setSelection(true);

        rdBtnInactive = new Button(grpRegimen, SWT.RADIO);
        rdBtnInactive.setBounds(new Rectangle(270, 70, 80, 20));
        rdBtnInactive.setText("Inactivo");
        rdBtnInactive.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        rdBtnInactive.setSelection(false);

        // Mapeamento OpenMRS
        lblRegimeEspecifico = new Label(grpRegimen, SWT.NONE);
        lblRegimeEspecifico.setBounds(new Rectangle(30, 95, 110, 20));
        lblRegimeEspecifico.setText(Messages.getString("adddruggroup.label.regime.especifico.title")); //$NON-NLS-1$
        lblRegimeEspecifico.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtRegimeEspecifico = new Text(grpRegimen, SWT.BORDER);
        txtRegimeEspecifico.setBounds(new Rectangle(160, 95, 220, 20));
        txtRegimeEspecifico.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        txtRegimeEspecifico.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.character == SWT.CR) {
                    //	cmdSearchWidgetSelected();
                }
            }
        });
    }

    /**
     * This method initializes compButtonsMiddle
     */
    private void createCompButtonsMiddle() {

        // compButtonsMiddle
        compButtonsMiddle = new Composite(getShell(), SWT.NONE);
        compButtonsMiddle.setBounds(new Rectangle(182, 295, 472, 50));

        // Add Drug button and icon
        lblPicAddDrug = new Label(compButtonsMiddle, SWT.NONE);
        lblPicAddDrug.setBounds(new Rectangle(0, 0, 50, 43));
        lblPicAddDrug.setImage(ResourceUtils.getImage(iDartImage.PRESCRIPTIONADDDRUG));

        btnAddDrug = new Button(compButtonsMiddle, SWT.NONE);
        btnAddDrug.setBounds(new Rectangle(60, 12, 185, 27));
        btnAddDrug.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        btnAddDrug.setText(Messages.getString("adddruggroup.button.adddruggroup.title")); //$NON-NLS-1$
        btnAddDrug
                .setToolTipText(Messages.getString("adddruggroup.button.adddruggroup.tooltip")); //$NON-NLS-1$
        btnAddDrug.setEnabled(true);
        btnAddDrug.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(
                    org.eclipse.swt.events.SelectionEvent e) {
                cmdAddDrugWidgetSelected();
            }
        });
        btnAddDrug.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        btnRemoveDrug = new Button(compButtonsMiddle, SWT.NONE);
        btnRemoveDrug.setBounds(new Rectangle(268, 12, 185, 28));
        btnRemoveDrug.setText(Messages.getString("adddruggroup.button.drugremove.title")); //$NON-NLS-1$
        lblPicAddDrug.setEnabled(true);
        btnRemoveDrug.setEnabled(true);
        btnAddDrug.setToolTipText(Messages.getString("adddruggroup.button.drugremove.tooltip")); //$NON-NLS-1$
        btnRemoveDrug.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(
                    org.eclipse.swt.events.SelectionEvent e) {
                cmdRemoveDrug();
            }
        });
        btnRemoveDrug.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

    }

    /**
     * This method initializes grpDrugs
     */
    private void createGrpDrugs() {

        grpDrugs = new Group(getShell(), SWT.NONE);
        grpDrugs.setText(Messages.getString("adddruggroup.group.druggrp.title")); //$NON-NLS-1$
        grpDrugs.setBounds(new org.eclipse.swt.graphics.Rectangle(50, 361, 700, 199));
        grpDrugs.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        createDrugsTable();
    }

    /**
     * This method initializes tblDrugs
     */
    private void createDrugsTable() {

        tblDrugs = new Table(grpDrugs, SWT.FULL_SELECTION);
        tblDrugs.setLinesVisible(true);
        tblDrugs.setBounds(new org.eclipse.swt.graphics.Rectangle(22, 25, 655, 160));
        tblDrugs.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        tblDrugs.setHeaderVisible(true);

        // clmSpace
        clmSpace = new TableColumn(tblDrugs, SWT.NONE);
        clmSpace.setWidth(28);
        clmSpace.setText(Messages.getString("adddruggroup.table.clmspace.title")); //$NON-NLS-1$

        // clmDrugName
        clmDrugName = new TableColumn(tblDrugs, SWT.NONE);
        clmDrugName.setText(Messages.getString("adddruggroup.table.clmdrugname.title")); //$NON-NLS-1$
        clmDrugName.setWidth(235);
        clmDrugName.setResizable(false);

        // clmTake
        clmTake = new TableColumn(tblDrugs, SWT.NONE);
        clmTake.setWidth(40);
        // clmTake.setText("Directions");
        clmTake.setResizable(false);

        // clmAmt
        clmAmt = new TableColumn(tblDrugs, SWT.NONE);
        // clmAmt.setText("Amount");
        clmAmt.setWidth(40);
        clmAmt.setResizable(false);

        // tblDescription
        tblDescription = new TableColumn(tblDrugs, SWT.NONE);
        tblDescription.setWidth(70);
        tblDescription.setResizable(false);

        // tblPerDay
        tblPerDay = new TableColumn(tblDrugs, SWT.NONE);
        tblPerDay.setWidth(30);
        tblPerDay.setResizable(false);

        // tblTPD
        tblTPD = new TableColumn(tblDrugs, SWT.NONE);
        tblTPD.setWidth(95);
        tblTPD.setResizable(false);

    }

    /**
     * This method initializes compButtons
     *
     * @return Regimen
     */
    private RegimeTerapeutico getLocalRegimen() {
        RegimeTerapeutico regToSave = new RegimeTerapeutico();
        // NOT NULL only upon regimeTrapeutico UPDATE.
        // Therefore, id on NEW regimeTerapeutico not needed.
        if (localRegimen != null) {
            regToSave.setRegimeid(localRegimen.getRegimeid());
        }

        if (rdBtnActive.getSelection())
            regToSave.setActive(true);
        else
            regToSave.setActive(false);

        regToSave.setRegimeesquema(txtDrugGroupName.getText().trim());
        regToSave.setRegimenomeespecificado(txtRegimeEspecifico.getText());
        regToSave.setCodigoregime(txtCodigoRegime.getText());
        regToSave.setRegimeesquemaidart(txtDrugGroupName.getText().trim());

        List<RegimenDrugs> regimenDrugsList = new ArrayList<RegimenDrugs>();
        // Save the regimen Drugs
        for (int i = 0; i < tblDrugs.getItemCount(); i++) {
            TableItem tmpItem = tblDrugs.getItem(i);
            RegimenDrugs oldRD = (RegimenDrugs) tmpItem.getData();
            RegimenDrugs newRD = new RegimenDrugs();
            newRD.setAmtPerTime(oldRD.getAmtPerTime());
            newRD.setDrug(oldRD.getDrug());
            newRD.setModified(oldRD.getModified());
            newRD.setRegimen(regToSave);
            newRD.setTimesPerDay(oldRD.getTimesPerDay());
            regimenDrugsList.add(newRD);
        }
        regToSave.setRegimenDrugs(regimenDrugsList);
        return regToSave;
    }

    @Override
    protected void cmdSaveWidgetSelected() {

        Transaction tx = null;
        tx = getHSession().beginTransaction();

        RegimeTerapeutico regToSave = getLocalRegimen();

        if (!isAddnotUpdate && !changesMade(regToSave)) {
            MessageBox mSave = new MessageBox(getShell(), SWT.ICON_INFORMATION
                    | SWT.OK);
            mSave.setText(Messages.getString("adddruggroup.label.msave.title")); //$NON-NLS-1$
            mSave
                    .setMessage(Messages.getString("adddruggroup.msave.message")); //$NON-NLS-1$
            mSave.open();

            return;
        }

        // Check if all the fields are filled in
        if (fieldsOk()) {

            MessageBox mSave = new MessageBox(getShell(), SWT.ICON_QUESTION
                    | SWT.YES | SWT.NO);
            mSave.setText(isAddnotUpdate ? Messages.getString("adddruggroup.msave.title") //$NON-NLS-1$
                    : Messages.getString("adddruggroup.msave.text")); //$NON-NLS-1$
            mSave.setMessage(isAddnotUpdate ? Messages.getString("adddruggroup.msave.ifmessage") //$NON-NLS-1$
                    : Messages.getString("adddruggroup.msave.errormessage")); //$NON-NLS-1$
            switch (mSave.open()) {

                case SWT.YES: {
                    try {

                        boolean isDrugIdentical = DrugManager
                                .regimenDrugsIdentical(getHSession(), regToSave);

                        boolean isDrugDuplicated = DrugManager
                                .regimenDrugsDuplicated(regToSave);

                        if (isDrugIdentical) {
                            MessageBox m = new MessageBox(getShell(), SWT.OK
                                    | SWT.ICON_ERROR);
                            m.setText(Messages.getString("adddruggroup.m.message.title")); //$NON-NLS-1$
                            m.setMessage(Messages.getString("adddruggroup.m.message.label")); //$NON-NLS-1$
                            m.open();
                            if (tx != null) {
                                tx.rollback();
                            }

                        } else if (isDrugDuplicated) {
                            MessageBox m = new MessageBox(getShell(), SWT.OK
                                    | SWT.ICON_ERROR);
                            m.setText(Messages.getString("adddruggroup.m.message.title")); //$NON-NLS-1$
                            m.setMessage(Messages.getString("adddruggroup.m.message.duplicate")); //$NON-NLS-1$
                            m.open();
                            if (tx != null) {
                                tx.rollback();
                            }
                        } else {
                            // Remove old copy of localRegimen, so proper update is
                            // possible.
                            if (tx.isActive()) {
                                if (localRegimen != null) {
                                    if (!localRegimen.getRegimenDrugs().isEmpty())
                                        DeletionsManager.removeRegimeTerapeuicoDrugs(getHSession(), localRegimen);
                                    DrugManager.updateRegimeTerapeutico(getHSession(), regToSave);
                                }
                            }

                            if (localRegimen != null)
                                localRegimen = null;
                            else
                                DrugManager.saveRegimeTerapeutico(getHSession(), regToSave);

                            tx.commit();
                            getHSession().flush();

                            MessageBox m = new MessageBox(getShell(), SWT.OK
                                    | SWT.ICON_INFORMATION);
                            m.setText(Messages.getString("adddruggroup.m.message.update")); //$NON-NLS-1$
                            m.setMessage(MessageFormat.format(Messages.getString("adddruggroup.m.messagegrp.update"), regToSave.getRegimeesquema()));
                            m.open();
                            cmdCancelWidgetSelected();
                        }

                    } catch (HibernateException he) {
                        MessageBox m = new MessageBox(getShell(), SWT.OK
                                | SWT.ICON_ERROR);
                        m.setText(Messages.getString("adddruggroup.m.message.title")); //$NON-NLS-1$
                        m
                                .setMessage(Messages.getString("adddruggroup.m.messageregimen.title")); //$NON-NLS-1$
                        m.open();
                        if (tx != null) {
                            tx.rollback();
                        }

                        getLog().error(Messages.getString("adddruggroup.m.message.title"), he); //$NON-NLS-1$
                        clearForm();
                    }
                    break;
                }
                case SWT.NO:
                    // Close the form
                    cmdCancelWidgetSelected();
                    break;
            }
        }
    }

    /**
     * Method to displose screen when cancel button is pressed
     */
    @Override
    protected void cmdCancelWidgetSelected() {
        closeShell(true);

    }

    @Override
    protected void cmdClearWidgetSelected() {

        clearForm();

    }

    @Override
    protected void clearForm() {

        txtDrugGroupName.setText(EMPTY); //$NON-NLS-1$
        txtDrugGroupName.setFocus();

        txtCodigoRegime.setText(EMPTY);
        txtRegimeEspecifico.setText(EMPTY);

        rdBtnActive.setSelection(true);
        rdBtnInactive.setSelection(false);

        tblDrugs.clearAll();
        tblDrugs.removeAll();
        tblDrugs.setItemCount(0);
        intDrugTableSize = 1;

        localRegimen = null;
//        enableFields(false);

    }

    /**
     * checks the form for valid fields entries
     *
     * @return true if the requiResourceUtils.getColor(iDartColor.RED) fields
     * are filled in
     */
    @Override
    protected boolean fieldsOk() {

        boolean result = true;
// Controla se regime esta preenhido
//        if (!isAddNotUpdate() && (cmbRegimenLinha.getText() == null || EMPTY.equals(cmbRegimenLinha.getText()))) { //$NON-NLS-1$
//            MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
//                    | SWT.OK);
//            missing.setText(Messages.getString("adddruggroup.drug.group.error")); //$NON-NLS-1$
//            missing.setMessage(Messages.getString("adddruggroup.drug.group.update")); //$NON-NLS-1$
//            missing.open();
//            txtDrugGroupName.setFocus();
//            result = false;
//            return result;
//        }

        if (txtDrugGroupName.getText().trim().equals(EMPTY)) { //$NON-NLS-1$
            MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
                    | SWT.OK);
            missing.setText(Messages.getString("adddruggroup.drug.group.reenter")); //$NON-NLS-1$
            missing
                    .setMessage(Messages.getString("adddruggroup.drug.group.blank")); //$NON-NLS-1$
            missing.open();
            txtDrugGroupName.setFocus();
            result = false;
        } else if (isAddnotUpdate && DrugManager.regimeTerapeuticoNameExists(getHSession(),
                txtDrugGroupName.getText().trim()) && !isAddnotUpdate ) {
            MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            mb.setText(Messages.getString("adddruggroup.drug.grp.exists")); //$NON-NLS-1$
            mb
                    .setMessage(MessageFormat.format(Messages.getString("adddruggroup.drug.grp.name"), txtDrugGroupName.getText().trim())); //$NON-NLS-1$
            mb.open();
            txtDrugGroupName.setFocus();
            result = false;
        } else if ((!isAddnotUpdate && !(localRegimen.getRegimeesquema().equalsIgnoreCase(txtDrugGroupName.getText().trim()))) && DrugManager.regimenNameExists(getHSession(), txtDrugGroupName
                .getText().trim())) {
            MessageBox duplicate = new MessageBox(getShell(), SWT.ICON_ERROR
                    | SWT.OK);
            duplicate.setText(Messages.getString("adddruggroup.duplicate.blank")); //$NON-NLS-1$
            duplicate
                    .setMessage(Messages.getString("adddruggroup.duplicate.update")); //$NON-NLS-1$
            duplicate.open();
            txtDrugGroupName.setFocus();
            result = false;

        } else if (txtCodigoRegime.getText().isEmpty()) {
            MessageBox noFNM = new MessageBox(getShell(), SWT.ICON_ERROR
                    | SWT.OK);
            noFNM.setText(Messages.getString("adddruggroup.codigo.regime.blank")); //$NON-NLS-1$
            noFNM.setMessage(Messages.getString("adddruggroup.codigo.regime.error")); //$NON-NLS-1$
            noFNM.open();
            txtCodigoRegime.setFocus();
            result = false;
        } else if (txtRegimeEspecifico.getText().isEmpty()) {
            MessageBox noRegimeOpenMRS = new MessageBox(getShell(), SWT.ICON_ERROR
                    | SWT.OK);
            noRegimeOpenMRS.setText(Messages.getString("adddruggroup.regime.openmrs.blank")); //$NON-NLS-1$
            noRegimeOpenMRS.setMessage(Messages.getString("adddruggroup.regime.openmrs.error")); //$NON-NLS-1$
            noRegimeOpenMRS.open();
            txtRegimeEspecifico.setFocus();
            result = false;
        } else if (tblDrugs.getItemCount() == 0) {
            MessageBox noDrugsAdded = new MessageBox(getShell(), SWT.ICON_ERROR
                    | SWT.OK);
            noDrugsAdded.setText(Messages.getString("adddruggroup.drug.added")); //$NON-NLS-1$
            noDrugsAdded
                    .setMessage(Messages.getString("adddruggroup.drug.update")); //$NON-NLS-1$
            noDrugsAdded.open();
            btnAddDrug.setFocus();
            result = false;

        }
        return result;
    }

    /**
     * This method is called when the user presses the "Add Drug to this
     * Regimen" button.
     */
    private void cmdAddDrugWidgetSelected() {

        // Add a new table item
        TableItem ti = new TableItem(tblDrugs, SWT.NONE);
        PrescriptionObject myPrescriptionObject = new PrescriptionObject(
                getHSession(), ti, true, getShell());
        ti.setText(0, (Integer.toString(tblDrugs.getItemCount())));
        myPrescriptionObject.setTableItem(ti);
        intDrugTableSize = tblDrugs.getItemCount();

    }

    /**
     * This method is called if the user clicks on a row in the drugs table. The
     * user is then asked if they want to delete the drug that they've selected.
     */
    private void cmdRemoveDrug() {

        TableItem[] ti = tblDrugs.getSelection();

        if (tblDrugs.getItemCount() == 0) {
            showMessage(MessageDialog.ERROR, "Nenhum medicamento seleccionado",
                    "Nenhm medicamento foi seleccionado nesta prescrição.");
        } else if (ti != null) {
            String drug = ti[0].getText(1);
            boolean questionResponse = showMessage(MessageDialog.QUESTION,
                    MessageFormat.format(Messages.getString("adddruggroup.drug.remove"), drug), //$NON-NLS-1$
                    MessageFormat.format(Messages.getString("adddruggroup.comfirmation.drug.remove"), drug)); //$NON-NLS-1$
            if (questionResponse) {
                // Delete from Regimen
                int index = tblDrugs.getSelectionIndex();
                tblDrugs.remove(index);
                for (int i = index; i < tblDrugs.getItemCount(); i++) {
                    TableItem oti = tblDrugs.getItem(i);
                    int number = Integer.parseInt(oti.getText(0));
                    number--;
                    oti.setText(0, String.valueOf(number)); //$NON-NLS-1$
                }
                intDrugTableSize--;
            }

        }
    }

    /**
     * indicates if this GUI is being used to add a new regimen or used to
     * update an existing regimen
     *
     * @return boolean
     */
    public boolean isAddNotUpdate() {
        return isAddnotUpdate;
    }

    private void cmdSearchWidgetSelected() {

        // if user is adding a new drug group, then check the drug group name
        if (isAddnotUpdate) {

            if (txtDrugGroupName.getText().trim().equals(EMPTY)) { //$NON-NLS-1$
                MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR  | SWT.OK);
                mb.setText(Messages.getString("adddruggroup.drug.entered")); //$NON-NLS-1$
                mb.setMessage(Messages.getString("adddruggroup.drug.enetered.error")); //$NON-NLS-1$
                mb.open();
                txtDrugGroupName.setFocus();
            } else if (DrugManager.regimeTerapeuticoNameExists(getHSession(),
                    txtDrugGroupName.getText().trim())) {
                MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
                mb.setText(Messages.getString("adddruggroup.drug.grp.exists")); //$NON-NLS-1$
                mb
                        .setMessage(MessageFormat.format(Messages.getString("adddruggroup.drug.grp.name"), txtDrugGroupName.getText().trim())); //$NON-NLS-1$
                mb.open();
                txtDrugGroupName.setFocus();
            } else {
                enableFields(true);
                txtDrugGroupName.setFocus();
            }

        } // else, we're updating an existing drug group, so open up a search GUI
        else {

            Search regimenSearch = new Search(getHSession(), getShell(), CommonObjects.REGIMEN);

            if (regimenSearch.getValueSelected() != null) {
                clearForm();
                localRegimen = DrugManager.getRegimeTerapeutico(getHSession(), regimenSearch.getValueSelected()[0]);
                loadRegimenDetails();
                loadRegimenDrugs();
                enableFields(true);
            } // if we've returned from the search GUI with the user having
            // pressed "cancel", enable the search button
            else {
                enableFields(false);
                txtDrugGroupName.setFocus();
            }

        }

    }

    /**
     * This method loads the details of the regimen into the GUI. It used the
     * localRegimen object to get the values
     */
    public void loadRegimenDetails() {
        txtDrugGroupName.setText(localRegimen.getRegimeesquema());
        txtCodigoRegime.setText(localRegimen.getCodigoregime());
        txtRegimeEspecifico.setText(localRegimen.getRegimenomeespecificado());

        if (localRegimen.isActive()) {
            rdBtnActive.setSelection(true);
            rdBtnInactive.setSelection(false);
        } else {
            rdBtnActive.setSelection(false);
            rdBtnInactive.setSelection(true);
        }

    }

    /**
     * This method loads the GUI with details from the Prescription object
     * passed to it.
     */
    private void loadRegimenDrugs() {

        String tempAmtPerTime = EMPTY; //$NON-NLS-1$

        List<RegimenDrugs> drugs = localRegimen.getRegimenDrugs();
        for (Iterator<RegimenDrugs> iter = drugs.iterator(); iter.hasNext(); ) {

            RegimenDrugs rd = iter.next();
            Drug theDrug = rd.getDrug();
            Form theForm = theDrug.getForm();

            if (theForm.getFormLanguage1().equals(EMPTY)) //$NON-NLS-1$
            // is a cream - no amnt per time
            {
                tempAmtPerTime = EMPTY;
            } else {
                if (new BigDecimal(rd.getAmtPerTime()).scale() == 0) {
                    tempAmtPerTime = String.valueOf(new BigDecimal(rd.getAmtPerTime()).unscaledValue().intValue());
                } else {
                    tempAmtPerTime = String.valueOf(rd.getAmtPerTime());
                }
            }

            TableItem ti = new TableItem(tblDrugs, SWT.NONE);
            String[] temp = new String[8];
            temp[0] = String.valueOf(intDrugTableSize);
            // temp[1] = "0";
            temp[1] = theDrug.getName();
            temp[2] = theForm.getActionLanguage1();
            temp[3] = tempAmtPerTime;
            temp[4] = theForm.getFormLanguage1();
            temp[5] = String.valueOf(rd.getTimesPerDay());
            temp[6] = Messages.getString("adddruggroup.drug.times"); //$NON-NLS-1$

            ti.setText(temp);
            ti.setData(rd);
            intDrugTableSize += 1;

        }
    }

    /**
     * Method enableFields.
     *
     * @param enable boolean
     */
    @Override
    protected void enableFields(boolean enable) {

        lblPicAddDrug.setEnabled(enable);
        btnAddDrug.setEnabled(enable);
        btnRemoveDrug.setEnabled(enable);
        btnSaveDrugGroup.setEnabled(enable);
        rdBtnActive.setEnabled(enable);
        rdBtnInactive.setEnabled(enable);
        txtCodigoRegime.setEnabled(enable);
        txtRegimeEspecifico.setEnabled(enable);

        if (enable) {
            tblDrugs.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        } else {
            tblDrugs.setBackground(ResourceUtils
                    .getColor(iDartColor.WIDGET_LIGHT_SHADOW_BACKGROUND));
        }
    }

    /**
     * This method closes and reopens a session. It should be used in screens
     * that don't close after a write
     */
    public void closeAndReopenSession() {

        try {
            if (getHSession() != null) {
                getHSession().close();
            }
            setHSession(HibernateUtil.getNewSession());
        } catch (HibernateException he) {
            getLog().error(he);
            he.printStackTrace();
        }
    }

    /**
     * Method submitForm.
     *
     * @return boolean
     */
    @Override
    protected boolean submitForm() {
        return false;
    }

    @Override
    protected void setLogger() {
        Logger log = Logger.getLogger(this.getClass());
        setLog(log);
    }

    /**
     * @param newRegimen
     * @return
     */
    private boolean changesMade(RegimeTerapeutico newRegimen) {
        if(newRegimen !=null && localRegimen !=null) {
            if (!newRegimen.getRegimeesquema().equalsIgnoreCase(localRegimen.getRegimeesquema())) {
                return true;
            } else if (newRegimen.isActive() != localRegimen.isActive()) {
                return true;
            } else if (!newRegimen.getCodigoregime().equalsIgnoreCase(localRegimen.getCodigoregime())) {
                return true;
            } else if (!newRegimen.getRegimenomeespecificado().equalsIgnoreCase(localRegimen.getRegimenomeespecificado())) {
                return true;
            } else if (!newRegimen.getRegimenDrugs().containsAll(localRegimen.getRegimenDrugs())
                    && newRegimen.getRegimenDrugs().size() != localRegimen.getRegimenDrugs().size()) {
                return true;
            }
        }
            return false;

    }

}
