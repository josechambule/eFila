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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 */
public class AddClinic extends GenericFormGui {

	private Group grpClinicInfo;

	private Group grplocationInfo;

	public Text txtClinic;

	public Text txtTelephone;

	public Text txtClinicNotes;

	public CCombo cmbCountry;

	public CCombo cmbProvince;

	public CCombo cmbDistrict;

	public CCombo cmbSubDistrict;

	public CCombo cmbFacility;

	public Text cmbFacilityType;

	public Text txtFacilityType;

	public CCombo txtProvince;

	public CCombo txtDistrict;

	public CCombo txtSubDistrict;

	public CCombo txtFacility;

	private Button btnSearch;

	private Button btnSearchNational;

	boolean isAddnotUpdate;

	private Clinic localClinic;

	/**
	 * Use true if you want to add a new clinic, use false if you are updating
	 * an existing clinic
	 *
	 * @param parent
	 *            Shell
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
	 *
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
	 *
	 */
	@Override
	protected void createCompButtons() {
		buildCompButtons();
	}

	/**
	 * This method initializes grpClinicInfo
	 *
	 */
	private void createGrpClinicInfo() {

		// grpClinicInfo
		grpClinicInfo = new Group(getShell(), SWT.NONE);
		grpClinicInfo.setBounds(new Rectangle(73, 90, 500, 380));
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

		// lblClinic & txtClinic
		Label lblClinic = new Label(grpClinicInfo, SWT.NONE);
		lblClinic.setBounds(new Rectangle(40, 50, 111, 20));
		lblClinic.setText("* Nome:");
		lblClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		txtClinic = new Text(grpClinicInfo, SWT.BORDER);
		txtClinic.setBounds(new Rectangle(157, 50, 130, 20));
		txtClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		txtClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		txtClinic.setFocus();
		// btnSearch
		btnSearch = new Button(grpClinicInfo, SWT.NONE);
		btnSearch.setBounds(new Rectangle(293, 47, 152, 30));
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

		// lblCountry & cmbCountry
		Label lblCountry = new Label(grplocationInfo, SWT.NONE);
		lblCountry.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 30,
				100, 20));
		lblCountry.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblCountry.setText("País:");
		cmbCountry = new CCombo(grplocationInfo, SWT.BORDER);
		cmbCountry.setBounds(new Rectangle(140, 30, 130, 20));
		cmbCountry.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		cmbCountry.setEditable(false);
		cmbCountry.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbCountry.setText(iDartProperties.country);
		cmbCountry.add(iDartProperties.country);

		// lblProvince & cmdProvince
		Label lblProvince = new Label(grplocationInfo, SWT.NONE);
		lblProvince.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 60,
				100, 20));
		lblProvince.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblProvince.setText("Província:");
		cmbProvince = new CCombo(grplocationInfo, SWT.BORDER);
		cmbProvince.setBounds(new Rectangle(140, 60, 150, 20));
		cmbProvince.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		cmbProvince.setEditable(false);
		// cmbProvince.setText("select a province");
		// cmbProvince.setEnabled(true);
		cmbProvince.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		// populate the province combo box
		CommonObjects.populateProvinces(getHSession(), cmbProvince);

		cmbProvince.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				populateDistrict();
			}
		});

		// lblDistrict & cmbDistrict
		Label lblDistrict = new Label(grplocationInfo, SWT.NONE);
		lblDistrict.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 90,
				100, 20));
		lblDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblDistrict.setText("Distrito:");
		cmbDistrict = new CCombo(grplocationInfo, SWT.BORDER);
		cmbDistrict.setBounds(new Rectangle(140, 90, 160, 20));
		cmbDistrict.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		cmbDistrict.setEditable(false);
		cmbDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbDistrict.setEnabled(false);
		cmbDistrict.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				populateSubDistrict();
			}
		});

		// lblSubdistrict & cmbSubDistrict
		Label lblSubdistrict = new Label(grplocationInfo, SWT.NONE);
		lblSubdistrict.setBounds(new org.eclipse.swt.graphics.Rectangle(40,
				120, 100, 20));
		lblSubdistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblSubdistrict.setText("Posto Administrativo:");
		cmbSubDistrict = new CCombo(grplocationInfo, SWT.BORDER);
		cmbSubDistrict.setBounds(new Rectangle(140, 120, 160, 20));
		cmbSubDistrict.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		cmbSubDistrict.setEditable(false);
		cmbSubDistrict.setEnabled(false);
		cmbSubDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbSubDistrict.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				populateFacilityname();

			}
		});

		// lblTelephone & txtTelephone
		Label lblTelephone = new Label(grpClinicInfo, SWT.NONE);
		lblTelephone.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 80,
				100, 20));
		lblTelephone.setText("Telefone:");
		lblTelephone.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		txtTelephone = new Text(grpClinicInfo, SWT.BORDER);
		txtTelephone.setBounds(new Rectangle(157, 80, 130, 20));
		txtTelephone.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		// lblClinicNotes & txtClinicNotes
		Label lblClinicNotes = new Label(grpClinicInfo, SWT.NONE);
		lblClinicNotes.setBounds(new org.eclipse.swt.graphics.Rectangle(40,
				110, 100, 20));
		lblClinicNotes.setText("Notas:");
		lblClinicNotes.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		txtClinicNotes = new Text(grpClinicInfo, SWT.BORDER);
		txtClinicNotes.setBounds(new Rectangle(157, 110, 130, 20));
		txtClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		grplocationInfo = new Group(grpClinicInfo, SWT.NONE);
		grplocationInfo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grplocationInfo.setText("Esta Farmacia esta filiada à:");
		grplocationInfo.setBounds(new Rectangle(18, 150, 470, 210));


		// btnSearch
		btnSearchNational = new Button(grplocationInfo, SWT.NONE);
		btnSearchNational.setBounds(new Rectangle(320, 60, 130, 30));
		btnSearchNational.setText("Procurar na Lista ");
		btnSearchNational.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		if(!isAddnotUpdate){
			btnSearchNational.setEnabled(false);
		}

		btnSearchNational.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				cmdSearchNationalSelected();
			}
		});
		btnSearchNational
				.setToolTipText("Pressione para procurar uma US existente .");

		// lblSubdistrict & cmbSubDistrict
		Label lblFacility = new Label(grplocationInfo, SWT.NONE);
		lblFacility.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 150,
				100, 20));
		lblFacility.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblFacility.setText("Nome da US:");
		cmbFacility = new CCombo(grplocationInfo, SWT.BORDER);
		cmbFacility.setBounds(new Rectangle(140, 150, 160, 20));
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
		lblFacilityType.setBounds(new org.eclipse.swt.graphics.Rectangle(40,
				180, 100, 20));
		lblFacilityType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblFacilityType.setText("Tipo de US:");
		txtFacilityType = new Text(grplocationInfo, SWT.BORDER);
		txtFacilityType.setBounds(new Rectangle(140, 180, 150, 20));
		txtFacilityType.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		txtFacilityType.setEditable(false);
		txtFacilityType.setEnabled(false);
		txtFacilityType.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		// lblProvince & cmdProvince
		Label lblProvince = new Label(grplocationInfo, SWT.NONE);
		lblProvince.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 60,
				100, 20));
		lblProvince.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblProvince.setText("Província:");
		txtProvince = new CCombo(grplocationInfo, SWT.BORDER);
		txtProvince.setBounds(new Rectangle(140, 60, 150, 20));
		txtProvince.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		txtProvince.setEditable(false);
		txtProvince.setText("select a province");

		// lblDistrict & cmbDistrict
		Label lblDistrict = new Label(grplocationInfo, SWT.NONE);
		lblDistrict.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 90,
				100, 20));
		lblDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblDistrict.setText("Distrito:");
		txtDistrict = new CCombo(grplocationInfo, SWT.BORDER);
		txtDistrict.setBounds(new Rectangle(140, 90, 160, 20));
		txtDistrict.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		txtDistrict.setEditable(false);
		txtDistrict.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		txtDistrict.setEnabled(false);

	}

	private void populateDistrict() {
		if (!cmbProvince.getText().equals("Seleccione a Província")) {
			cmbDistrict.setEnabled(true);
			cmbDistrict.removeAll();
			cmbSubDistrict.removeAll();
			cmbFacility.removeAll();
			txtFacilityType.setText("");
			List<String> ncList = new ArrayList<String>();
			ncList = AdministrationManager.getDistrict(getHSession(),
					cmbProvince.getText().trim());
			if (ncList != null) {
				for (String s : ncList) {
					if(s!=null){
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

				NationalClinics nclinic = AdministrationManager.getNationalClinic(getHSession(), cmbProvince.getText(), cmbDistrict.getText(), cmbSubDistrict.getText(), cmbFacility.getText());

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

				localClinic.setNotes(txtClinicNotes.getText().trim());
				localClinic.setClinicName(txtClinic.getText().trim());
				localClinic.setTelephone(txtTelephone.getText().trim());
				localClinic.setClinicDetails(nclinic);

				AdministrationManager.saveClinic(getHSession(), localClinic);
				getHSession().flush();
				tx.commit();

				showMessage(
						MessageDialog.INFORMATION,"Actualização com Sucesso",
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
			cmbCountry.setText(iDartProperties.country);
			cmbProvince.setText("Seleccione a Província");
			cmbDistrict.setText("");
			cmbDistrict.setEnabled(false);

			cmbFacility.setText("");
			cmbFacility.setEnabled(false);
			cmbSubDistrict.setText("");
			cmbSubDistrict.setEnabled(false);
			txtFacilityType.setText("");
			txtFacilityType.setEnabled(false);
			txtTelephone.setText("");
			txtClinicNotes.setText("");
			txtClinic.setFocus();
			if(!isAddnotUpdate){
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
				CommonObjects.CLINIC) {};

		if (clinicSearch.getValueSelected() != null) {

			txtClinic.setText(clinicSearch.getValueSelected()[0]);
			localClinic = AdministrationManager.getClinic(getHSession(),
					txtClinic.getText());
			loadClinicDetails();
			enableFields(true);
			btnSearch.setEnabled(false);
			// txtLocation.setFocus();
			btnSave.setEnabled(true);
			btnSearchNational.setEnabled(true);
		}

	}

	private void cmdSearchNationalSelected() {

		Search nationalSearch = new Search(getHSession(), getShell(),
				CommonObjects.NATION) {};

		if (nationalSearch.getValueSelected() != null) {

			NationalClinics natclinic = AdministrationManager.getSearchDetails(getHSession(),
					nationalSearch.getValueSelected()[0],nationalSearch.getValueSelected()[1]);
			loadNationalClinicDetails(natclinic);
		}

	}

	private void loadNationalClinicDetails(NationalClinics natclinic) {

		cmbProvince.setText(natclinic.getProvince());
		//populateDistrict();
		cmbProvince.setEnabled(true);

		cmbDistrict.setText(natclinic.getDistrict());
		//populateSubDistrict();
		cmbDistrict.setEnabled(true);

		cmbSubDistrict.setText(natclinic.getSubDistrict());
		//populateFacilityname();
		cmbSubDistrict.setEnabled(true);

		cmbFacility.setText(natclinic.getFacilityName());
		//populateFacilitytype();
		cmbFacility.setEnabled(true);

		txtFacilityType.setEnabled(true);
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
	 * @param enable
	 *            boolean
	 */
	@Override
	protected void enableFields(boolean enable) {

		// txtLocation.setEnabled(enable);
		// txtStreet.setEnabled(enable);
		cmbProvince.setEnabled(enable);
		cmbDistrict.setEnabled(enable);
		cmbFacility.setEnabled(enable);
		cmbSubDistrict.setEnabled(enable);
		txtFacilityType.setEnabled(enable);
		// txtCity.setEnabled(enable);
		// txtPostal.setEnabled(enable);
		txtTelephone.setEnabled(enable);
		txtClinicNotes.setEnabled(enable);

		cmbProvince.setBackground(enable ? ResourceUtils
				.getColor(iDartColor.WHITE) : ResourceUtils
				.getColor(iDartColor.WIDGET_BACKGROUND));
		btnSave.setEnabled(enable);
	}

	private void loadClinicDetails() {
		txtClinic.setText(localClinic.getClinicName());
		txtTelephone.setText(localClinic.getTelephone());
		txtClinicNotes.setText(localClinic.getNotes());

		NationalClinics natClinic = localClinic.getClinicDetails();
		if(natClinic != null){
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

	private void populateSubDistrict() {
		if (!cmbDistrict.getText().equals("Seleccione o Distrito")) {

			cmbSubDistrict.setEnabled(true);
			cmbSubDistrict.removeAll();
			cmbFacility.removeAll();
			txtFacilityType.clearSelection();

			List<String> ncList = new ArrayList<String>();
			ncList = AdministrationManager.getSubDistrict(
					getHSession(), cmbDistrict.getText().trim());

			if (ncList != null) {
				for (String s : ncList) {
					cmbSubDistrict.add(s);
				}
				ncList = null;
			}

			if (cmbSubDistrict.getItemCount() > 0) {
				// Set the default to the first item in the combo box
				cmbSubDistrict.setText("Seleccione o Posto Administrativo");
			}
		}
	}

	private void populateFacilityname() {
		if (!cmbSubDistrict.getText().equals(
				"Seleccione o Posto Administrativo")) {

			cmbFacility.setEnabled(true);
			cmbFacility.removeAll();
			txtFacilityType.clearSelection();
			List<String> ncList = new ArrayList<String>();
			ncList = AdministrationManager.getFacilityName(
					getHSession(), cmbSubDistrict.getText().trim());

			if (ncList != null) {
				for (String s : ncList) {
					cmbFacility.add(s);
				}
				ncList = null;
			}

			if (cmbFacility.getItemCount() > 0) {
				// Set the default to the first item in the combo box
				cmbFacility.setText("Seleccione o Nome da US");
			}
		}
	}

	private void populateFacilitytype() {
		if (!cmbSubDistrict.getText().equals(
				"Seleccione o Nome da US")) {

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
