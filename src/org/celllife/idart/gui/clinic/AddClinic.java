/*
 * iDART: The Intelligent Dispensing of Anti-retroviral Treatment
 * Copyright (C) 2006 Cell-Life
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version
 * 2 for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.celllife.idart.gui.clinic;

import model.manager.AdministrationManager;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.commonobjects.iDartProperties;
import org.celllife.idart.database.hibernate.Clinic;
import org.celllife.idart.database.hibernate.NationalClinics;
import org.celllife.idart.database.hibernate.User;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.gui.platform.GenericFormGui;
import org.celllife.idart.gui.search.Search;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartColor;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.celllife.idart.messages.Messages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import sun.jvm.hotspot.runtime.StubRoutines;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class AddClinic extends GenericFormGui {

    private Group grpClinicInfo;

    private Group grplocationInfo;

    public Text txtClinicCode;

    public Text txtClinic;

    public Text txtTelephone;

    public Text txtClinicNotes;

    public CCombo cmbFacilityType;

    public CCombo cmbProvince;

    public CCombo cmbDistrict;

//	public CCombo cmbSubDistrict;

    public CCombo cmbFacility;

    public Text txtFacilityType;

    private Button btnSearch;

    private Button btnSearchNational;

    private Button rdBtnyes;

    private Button rdBtnno;

    boolean isAddnotUpdate;

    private Clinic localClinic;

    /**
     * Use true if you want to add a new clinic, use false if you are updating
     * an existing clinic
     *
     * @param parent Shell
     */
    public AddClinic(Shell parent) {
        super(parent, HibernateUtil.getNewSession());

        if (!isAddnotUpdate) {
            enableFields(false);
        }
    }

    /**
     * This method initializes newClinic
     */
    @Override
    protected void createShell() {
        isAddnotUpdate = ((Boolean) getInitialisationOption(OPTION_isAddNotUpdate))
                .booleanValue();
        String shellTxt = isAddnotUpdate ? "Adicionar US"
                : "Actualizar uma US existente";
        Rectangle bounds = new Rectangle(100, 100, 600, 560);

        // Parent Generic Methods ------
        buildShell(shellTxt, bounds); // generic shell build
    }

    @Override
    protected void createContents() {
        createGrpClinicInfo();
    }

    /**
     * This method initializes compHeader
     */
    @Override
    protected void createCompHeader() {

        String headerTxt = (isAddnotUpdate ? "Adicionar Farmácia"
                : "Actualizar uma Farmácia existente");
        iDartImage icoImage = iDartImage.CLINIC;
        buildCompHeader(headerTxt, icoImage);
        Rectangle bounds = getCompHeader().getBounds();
        bounds.width = 720;
        bounds.x -= 40;
        getCompHeader().setBounds(bounds);

    }

    /**
     * This method initializes compButtons
     */
    @Override
    protected void createCompButtons() {
        buildCompButtons();
    }

    /**
     * This method initializes grpClinicInfo
     */
    private void createGrpClinicInfo() {

        // grpClinicInfo
        grpClinicInfo = new Group(getShell(), SWT.NONE);
        grpClinicInfo.setBounds(new Rectangle(73, 70, 500, 410));
        // grpDrugInfo = new Group(getShell(), SWT.NONE);
        // grpDrugInfo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        // grpDrugInfo.setText("Drug Details");
        // grpDrugInfo.setBounds(new Rectangle(18, 110, 483, 293));

        Label lblInstructions = new Label(grpClinicInfo, SWT.CENTER);
        lblInstructions.setBounds(new org.eclipse.swt.graphics.Rectangle(70,
                15, 260, 20));
        lblInstructions.setText("Todos campos marcados com * são obrigatorios");
        lblInstructions.setFont(ResourceUtils
                .getFont(iDartFont.VERASANS_10_ITALIC));

        // lblClinicCode & txtClinicCode
        Label lblClinicCode = new Label(grpClinicInfo, SWT.NONE);
        lblClinicCode.setBounds(new Rectangle(40, 50, 111, 20));
        lblClinicCode.setText("* Codigo:");
        lblClinicCode.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtClinicCode = new Text(grpClinicInfo, SWT.BORDER);
        txtClinicCode.setBounds(new Rectangle(157, 50, 160, 20));
        txtClinicCode.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        txtClinicCode.setFocus();

        // lblClinic & txtClinic
        Label lblClinic = new Label(grpClinicInfo, SWT.NONE);
        lblClinic.setBounds(new Rectangle(40, 80, 111, 20));
        lblClinic.setText("* Nome:");
        lblClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtClinic = new Text(grpClinicInfo, SWT.BORDER);
        txtClinic.setBounds(new Rectangle(157, 80, 160, 20));
        txtClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        // btnSearch
        btnSearch = new Button(grpClinicInfo, SWT.NONE);
        btnSearch.setBounds(new Rectangle(320, 47, 152, 30));
        btnSearch.setText("Procurar");
        btnSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        btnSearch.setVisible(!isAddnotUpdate);
        btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(
                    org.eclipse.swt.events.SelectionEvent e) {
                cmdSearchWidgetSelected();
            }
        });
        btnSearch.setToolTipText("Pressione para procurar uma US existente .");

        // lblProvince & cmdProvince
        Label lblProvince = new Label(grpClinicInfo, SWT.NONE);
        lblProvince.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 110, 100, 20));
        lblProvince.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblProvince.setText("* Província:");
        cmbProvince = new CCombo(grpClinicInfo, SWT.BORDER);
        cmbProvince.setBounds(new Rectangle(157, 110, 160, 20));
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
        Label lblDistrict = new Label(grpClinicInfo, SWT.NONE);
        lblDistrict.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 140, 100, 20));
        lblDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblDistrict.setText("* Distrito:");
        cmbDistrict = new CCombo(grpClinicInfo, SWT.BORDER);
        cmbDistrict.setBounds(new Rectangle(157, 140, 160, 20));
        cmbDistrict.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        cmbDistrict.setEditable(false);
        cmbDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        cmbDistrict.setEnabled(false);
//		cmbDistrict.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				populateSubDistrict();
//			}
//		});

//		// lblSubdistrict & cmbSubDistrict
//		Label lblSubdistrict = new Label(grpClinicInfo, SWT.NONE);
//		lblSubdistrict.setBounds(new org.eclipse.swt.graphics.Rectangle(40,170, 100, 20));
//		lblSubdistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
//		lblSubdistrict.setText("Posto Administrativo:");
//		cmbSubDistrict = new CCombo(grpClinicInfo, SWT.BORDER);
//		cmbSubDistrict.setBounds(new Rectangle(157, 170, 160, 20));
//		cmbSubDistrict.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
//		cmbSubDistrict.setEditable(false);
//		cmbSubDistrict.setEnabled(false);
//		cmbSubDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
//		cmbSubDistrict.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				populateFacilityname();
//
//			}
//		});

        // lblTelephone & txtTelephone
        Label lblTelephone = new Label(grpClinicInfo, SWT.NONE);
        lblTelephone.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 170, 100, 20));
        lblTelephone.setText("Telefone:");
        lblTelephone.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtTelephone = new Text(grpClinicInfo, SWT.BORDER);
        txtTelephone.setBounds(new Rectangle(157, 170, 160, 20));
        txtTelephone.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        // lblClinicNotes & txtClinicNotes
        Label lblClinicNotes = new Label(grpClinicInfo, SWT.NONE);
        lblClinicNotes.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 200, 100, 20));
        lblClinicNotes.setText("Notas:");
        lblClinicNotes.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        txtClinicNotes = new Text(grpClinicInfo, SWT.BORDER);
        txtClinicNotes.setBounds(new Rectangle(157, 200, 160, 20));
        txtClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));


        // lblClinicFacilityType & cmbClinicFacilityType
        Label lblClinicFacilityType = new Label(grpClinicInfo, SWT.NONE);
        lblClinicFacilityType.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 230, 100, 20));
        lblClinicFacilityType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblClinicFacilityType.setText("Tipo de Farmácia:");
        cmbFacilityType = new CCombo(grpClinicInfo, SWT.BORDER);
        cmbFacilityType.setBounds(new Rectangle(157, 230, 160, 20));
        cmbFacilityType.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        cmbFacilityType.setEditable(false);
        cmbFacilityType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        CommonObjects.populateFacilityTypes(getHSession(), cmbFacilityType);

        // Dependencia da Farmacia
        Label lblClinicLink = new Label(grpClinicInfo, SWT.NONE);
        lblClinicLink.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 260, 130, 20));
        lblClinicLink.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblClinicLink.setText("Proveniente de uma Farmácia?"); //$NON-NLS-1$

        rdBtnyes = new Button(grpClinicInfo, SWT.RADIO);
        rdBtnyes.setBounds(new Rectangle(190, 260, 80, 20));
        rdBtnyes.setText("Sim");
        rdBtnyes.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        rdBtnyes.setSelection(true);
        rdBtnyes.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {

                btnSearchNational.setEnabled(true);
            }
        });

        rdBtnno = new Button(grpClinicInfo, SWT.RADIO);
        rdBtnno.setBounds(new Rectangle(257, 260, 80, 20));
        rdBtnno.setText("Não");
        rdBtnno.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        rdBtnno.setSelection(false);
        rdBtnno.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {

                btnSearchNational.setEnabled(false);
                if (isAddnotUpdate) {
                    cmbFacility.setText("");
                    txtFacilityType.setText("");
                }
            }
        });

        grplocationInfo = new Group(grpClinicInfo, SWT.NONE);
        grplocationInfo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        grplocationInfo.setText("Esta Farmacia esta afiliada à:");
        grplocationInfo.setBounds(new Rectangle(18, 320, 470, 80));

        // btnSearch
        btnSearchNational = new Button(grplocationInfo, SWT.NONE);
        btnSearchNational.setBounds(new Rectangle(320, 10, 130, 30));
        btnSearchNational.setText("Procurar na Lista ");
        btnSearchNational.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        if (!isAddnotUpdate) {
            btnSearchNational.setEnabled(false);
        }

        btnSearchNational.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                cmdSearchNationalSelected();
            }
        });

        btnSearchNational.setToolTipText("Pressione para procurar uma US existente .");

        // lblSubdistrict & cmbSubDistrict
        Label lblFacility = new Label(grplocationInfo, SWT.NONE);
        lblFacility.setBounds(new org.eclipse.swt.graphics.Rectangle(20, 10, 100, 20));
        lblFacility.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblFacility.setText("Nome da Farmácia:");
        cmbFacility = new CCombo(grplocationInfo, SWT.BORDER);
        cmbFacility.setBounds(new Rectangle(157, 10, 160, 20));
        cmbFacility.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        cmbFacility.setEditable(false);
        cmbFacility.setEnabled(false);
        cmbFacility.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        cmbFacility.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                populateFacilitytype();
            }
        });

        // lblSubdistrict & cmbSubDistrict
        Label lblFacilityType = new Label(grplocationInfo, SWT.NONE);
        lblFacilityType.setBounds(new org.eclipse.swt.graphics.Rectangle(20, 40, 100, 20));
        lblFacilityType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblFacilityType.setText("Tipo de Farmácia:");
        txtFacilityType = new Text(grplocationInfo, SWT.BORDER);
        txtFacilityType.setBounds(new Rectangle(157, 40, 160, 20));
        txtFacilityType.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
        txtFacilityType.setEditable(false);
        txtFacilityType.setEnabled(false);
        txtFacilityType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

    }

    private void populateDistrict() {
        if (!cmbProvince.getText().equals("Seleccione a Província")) {
            cmbDistrict.setEnabled(true);
            cmbDistrict.removeAll();
            //cmbSubDistrict.removeAll();
            cmbFacility.removeAll();
            txtFacilityType.setText("");
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

    @Override
    protected void cmdSaveWidgetSelected() {

        if (fieldsOk()) {
            Transaction tx = null;

            try {
                tx = getHSession().beginTransaction();

                NationalClinics nclinic = AdministrationManager.getNationalClinic(getHSession(), txtFacilityType.getText(),cmbFacility.getText());

                if(rdBtnno.getSelection() && isAddnotUpdate) {
                    nclinic = new NationalClinics();

                    nclinic.setFacilityName(txtClinic.getText());
                    nclinic.setFacilityType(cmbFacilityType.getText());
                    nclinic.setProvince(cmbProvince.getText());
                    nclinic.setDistrict(cmbDistrict.getText());
                    nclinic.setSubDistrict("");
                    AdministrationManager.saveNacionalClinic(getHSession(), nclinic);
                }

                if (localClinic == null && isAddnotUpdate) {
                    localClinic = new Clinic();

                    List<User> users = AdministrationManager
                            .getUsers(getHSession());
                    for (User usr : users) {
                        if (usr.getClinics() == null) {
                            usr.setClinics(new HashSet<Clinic>());
                        }

                        usr.getClinics().add(localClinic);
                    }
                }

                localClinic.setUuid(UUID.randomUUID().toString());
                localClinic.setCode(txtClinicCode.getText());
                localClinic.setProvince(cmbProvince.getText());
                localClinic.setDistrict(cmbDistrict.getText());
                localClinic.setNotes(txtClinicNotes.getText().trim());
                localClinic.setClinicName(txtClinic.getText().trim());
                localClinic.setTelephone(txtTelephone.getText().trim());
                localClinic.setSubDistrict("");
                localClinic.setFacilityType(cmbFacilityType.getText());
                localClinic.setClinicDetails(nclinic);

                AdministrationManager.saveClinic(getHSession(), localClinic);
                getHSession().flush();
                tx.commit();

                showMessage(
                        MessageDialog.INFORMATION, "Actualização com Sucesso",
                        "A Farmacia '" + localClinic.getClinicName() + "' foi adicionada.");

            } catch (HibernateException he) {
                showMessage(
                        MessageDialog.ERROR,
                        "Problemas ao gravar a informação",
                        "Problemas ao gravar a informação da farmacia. Por favor, tente novamente.");
                if (tx != null) {
                    tx.rollback();
                }
                getLog().error(he);
            }
            cmdCancelWidgetSelected();
        }

    }

    /**
     * Clears the form
     */
    @Override
    public void clearForm() {
        try {
            txtClinic.setText("");
            //cmbCountry.setText(iDartProperties.country);
            cmbProvince.setText("Seleccione a Província");
            cmbDistrict.setText("");
            cmbDistrict.setEnabled(false);
            cmbFacility.setText("");
            cmbFacility.setEnabled(false);
//			cmbSubDistrict.setText("");
//			cmbSubDistrict.setEnabled(false);
            txtFacilityType.setText("");
            txtFacilityType.setEnabled(false);
            txtTelephone.setText("");
            txtClinicNotes.setText("");
            txtClinicCode.setText("");
            cmbFacilityType.setText("");
            cmbFacilityType.setEnabled(false);
            txtClinic.setFocus();
            if (!isAddnotUpdate) {
                btnSearchNational.setEnabled(false);
            }

            enableFields(isAddnotUpdate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void cmdCancelWidgetSelected() {
        cmdCloseSelected();

    }

    private void cmdSearchWidgetSelected() {

        Search clinicSearch = new Search(getHSession(), getShell(),
                CommonObjects.CLINIC) {
        };

        if (clinicSearch.getValueSelected() != null) {

            txtClinic.setText(clinicSearch.getValueSelected()[0]);
            localClinic = AdministrationManager.getClinic(getHSession(), clinicSearch.getValueSelected()[0]);
            loadClinicDetails();
            enableFields(true);
            btnSearch.setEnabled(false);
            // txtLocation.setFocus();
            btnSave.setEnabled(true);
            //	btnSearchNational.setEnabled(true);
        }

    }

    private void cmdSearchNationalSelected() {

        Search nationalSearch = new Search(getHSession(), getShell(),
                CommonObjects.NATION) {
        };

        if (nationalSearch.getValueSelected() != null) {

            NationalClinics natclinic = AdministrationManager.getSearchDetails(getHSession(),
                    nationalSearch.getValueSelected()[0], nationalSearch.getValueSelected()[1]);
            loadNationalClinicDetails(natclinic);
        }

    }

    private void loadNationalClinicDetails(NationalClinics natclinic) {

//        cmbProvince.setText(natclinic.getProvince());
//        //populateDistrict();
//        cmbProvince.setEnabled(true);
//
//        cmbDistrict.setText(natclinic.getDistrict());
//        //populateSubDistrict();
//        cmbDistrict.setEnabled(true);
//
////		cmbSubDistrict.setText(natclinic.getSubDistrict());
//        //populateFacilityname();
////		cmbSubDistrict.setEnabled(true);

        cmbFacility.setText(natclinic.getFacilityName());
        //populateFacilitytype();
        cmbFacility.setEnabled(false);

        txtFacilityType.setEnabled(false);
        txtFacilityType.setText(natclinic.getFacilityType());
    }

    @Override
    protected void cmdClearWidgetSelected() {

        clearForm();
        btnSearch.setEnabled(true);

    }

    /**
     * Method enableFields.
     *
     * @param enable boolean
     */
    @Override
    protected void enableFields(boolean enable) {

        cmbProvince.setEnabled(enable);
        cmbDistrict.setEnabled(enable);
        cmbFacility.setEnabled(enable);
//		cmbSubDistrict.setEnabled(enable);
        cmbFacilityType.setEnabled(enable);
        txtTelephone.setEnabled(enable);
        txtClinicNotes.setEnabled(enable);
        txtClinicCode.setEnabled(enable);

        cmbProvince.setBackground(enable ? ResourceUtils
                .getColor(iDartColor.WHITE) : ResourceUtils
                .getColor(iDartColor.WIDGET_BACKGROUND));
        btnSave.setEnabled(enable);
    }

    private void loadClinicDetails() {
        txtClinic.setText(localClinic.getClinicName());
        txtTelephone.setText(localClinic.getTelephone());
        txtClinicCode.setText(localClinic.getCode());
        txtClinicNotes.setText(localClinic.getNotes());
        cmbDistrict.setText(localClinic.getDistrict());
        cmbProvince.setText(localClinic.getProvince());
        cmbFacilityType.setText(localClinic.getFacilityType());
        rdBtnyes.setSelection(false);
        rdBtnno.setSelection(true);
        btnSearchNational.setEnabled(false);
        NationalClinics natClinic = localClinic.getClinicDetails();

        if (natClinic != null) {

            if (!localClinic.getClinicName().equalsIgnoreCase(natClinic.getFacilityName())) {
                rdBtnyes.setSelection(true);
                rdBtnno.setSelection(false);
                btnSearchNational.setEnabled(true);
            }

            loadNationalClinicDetails(natClinic);
        }

    }

    /**
     * Check if the necessary field names are filled in. Returns true if there
     * are fields missing
     *
     * @return boolean
     */
    @Override
    protected boolean fieldsOk() {

        boolean fieldsOkay = true;

        if (txtClinic.getText().trim().equals("")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("O campo nome da farmacia nao pode estar em branco.");
            b.setText("Campos em branco");
            b.open();
            txtClinic.setFocus();
            fieldsOkay = false;
        } else if ((AdministrationManager.clinicExists(getHSession(), txtClinic
                .getText().trim()) && isAddnotUpdate)
                || (AdministrationManager.clinicExists(getHSession(), txtClinic
                .getText().trim())
                && localClinic != null && !localClinic.getClinicName()
                .equalsIgnoreCase(txtClinic.getText().trim()))) {
            MessageBox m = new MessageBox(getShell(), SWT.OK
                    | SWT.ICON_INFORMATION);
            m.setText("Problemas ao gravar a informação");
            m
                    .setMessage("Ja existe uma farmacia com este nome! Por favor escolha outro nome");
            m.open();

            txtClinic.setFocus();
            fieldsOkay = false;

        }else  if (txtClinicCode.getText().trim().equals("")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("O campo codigo da farmacia nao pode estar em branco.");
            b.setText("Campos em branco");
            b.open();
            txtClinicCode.setFocus();
            fieldsOkay = false;
        } else if (cmbProvince.getText().trim().equals("") || cmbProvince.getText().trim().equals("Seleccione a Província")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("O campo provincia da farmacia nao pode estar em branco.");
            b.setText("Campos em branco");
            b.open();
            cmbProvince.setFocus();
            fieldsOkay = false;
        }else if (cmbDistrict.getText().trim().equals("") || cmbDistrict.getText().trim().equals("Seleccione o Distrito")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("O campo distrito da farmacia nao pode estar em branco.");
            b.setText("Campos em branco");
            b.open();
            cmbDistrict.setFocus();
            fieldsOkay = false;
        }else if (rdBtnyes.getSelection() && cmbFacility.getText().trim().equals("")) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("O campo Nome da farmacia de proveniencia nao pode estar em branco apos selecionar [Sim] no campo [Proveniente de uma Farmácia].");
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
        }

        return fieldsOkay;
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

//	private void populateSubDistrict() {
//		if (!cmbDistrict.getText().equals("Seleccione o Distrito")) {
//
//			cmbSubDistrict.setEnabled(true);
//			cmbSubDistrict.removeAll();
//			cmbFacility.removeAll();
//			txtFacilityType.clearSelection();
//
//			List<String> ncList = new ArrayList<String>();
//			ncList = AdministrationManager.getSubDistrict(
//					getHSession(), cmbDistrict.getText().trim());
//
//			if (ncList != null) {
//				for (String s : ncList) {
//					cmbSubDistrict.add(s);
//				}
//				ncList = null;
//			}
//
//			if (cmbSubDistrict.getItemCount() > 0) {
//				// Set the default to the first item in the combo box
//				cmbSubDistrict.setText("Seleccione o Posto Administrativo");
//			}
//		}
//	}

//	private void populateFacilityname() {
//		if (!cmbSubDistrict.getText().equals(
//				"Seleccione o Posto Administrativo")) {
//
//			cmbFacility.setEnabled(true);
//			cmbFacility.removeAll();
//			txtFacilityType.clearSelection();
//			List<String> ncList = new ArrayList<String>();
//			ncList = AdministrationManager.getFacilityName(
//					getHSession(), cmbSubDistrict.getText().trim());
//
//			if (ncList != null) {
//				for (String s : ncList) {
//					cmbFacility.add(s);
//				}
//				ncList = null;
//			}
//
//			if (cmbFacility.getItemCount() > 0) {
//				// Set the default to the first item in the combo box
//				cmbFacility.setText("Seleccione o Nome da US");
//			}
//		}
//	}

    private void populateFacilitytype() {
        if (!cmbFacility.getText().equals("Seleccione o Nome da US")) {

            txtFacilityType.setEnabled(true);
            txtFacilityType.clearSelection();

            List<String> ncList = new ArrayList<String>();
            ncList = AdministrationManager.getFacilityType(
                    getHSession(), cmbFacility.getText().trim());

            if (ncList != null && !ncList.isEmpty()) {
                txtFacilityType.setText(ncList.get(0));
            }
        }
    }

}
