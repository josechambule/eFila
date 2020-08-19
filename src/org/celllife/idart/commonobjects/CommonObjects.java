/*
 * iDART: The Intelligent Dispensing of Antiretroviral Treatment
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

package org.celllife.idart.commonobjects;

import model.manager.AdministrationManager;
import org.celllife.idart.database.hibernate.*;
import org.celllife.idart.messages.Messages;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 */

public class CommonObjects {

	// Declare constants that will be used to identify different forms
	public static final int CLINIC = 1;

	public static final int DOCTOR = 2;

	public static final int DRUG = 4;

	public static final int USER = 5;

	public static final int REGIMEN = 6;

	public static final int STOCK = 7;

	public static final int STOCK_TAKE = 8;

	public static final int STOCK_CENTER = 9;

	public static final int NATION = 10;

	public static final int ATC = 11;

	public static final int REGIMESACTIVOS = 12;

	public static final int ASSOCIATEDDRUGS = 13;

	public static String timesPerDayLanguage1 = "times per day";

	public static String timesPerDayLanguage2 = "ngemini";

	public static String timesPerDayLanguage3 = "keur per dag";


	public static final String NEXT_APPOINTMENT_KEY = "NEXT_APPOINTMENT";

	/**
	 * Private constructor to prevent instantiation
	 */
	private CommonObjects() {
	}

	public static void loadLanguages() {
		timesPerDayLanguage1 = iDartProperties.timesPerDayLanguage1;
		timesPerDayLanguage2 = iDartProperties.timesPerDayLanguage2;
		timesPerDayLanguage3 = iDartProperties.timesPerDayLanguage3;
	}

	/**
	 * Method populateStockCenters.
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 *            CCombo
	 */
	public static void populateStockCenters(Session sess, CCombo combo) {

		List<StockCenter> stockCenterList = AdministrationManager
				.getStockCenters(sess);

		if (stockCenterList != null) {
			String preferredName = "";
			for (StockCenter sc : stockCenterList) {
				combo.add(sc.getStockCenterName());
				if (sc.isPreferred()) {
					preferredName = sc.getStockCenterName();
				}
			}

			if (combo.getItemCount() > 0) {
				if (!"".equalsIgnoreCase(preferredName)) {
					combo.setText(preferredName);
				} else {
					combo.select(0);
				}
			}

		}
	}

	/**
	 * Method populateClinics.
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 *            CCombo
	 */
	public static void populateClinics(Session sess, CCombo combo) {
		populateClinics(sess, combo, true);
	}

	/**
	 * Method populateClinics.
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 *            CCombo
	 * @param includeMainClinic
	 *            boolean
	 */
	public static void populateClinics(Session sess, CCombo combo,
									   boolean includeMainClinic) {

		for (Clinic c : AdministrationManager.getClinics(sess)) {
			String str = c.getClinicName();
			if (str != null) {
				combo.add(str);
			}

		}

		if (!includeMainClinic) {
			// shouldn't be able to access the mainclinic here
			String mainClinicName = AdministrationManager
					.getDefaultClinicName(sess);
			if (combo.indexOf(mainClinicName) != -1) {
				combo.remove(mainClinicName);
			}
			if (combo.getItemCount() > 0) {
				combo.setText(combo.getItem(0));
			}
		} else {

			String defaultClinicName = AdministrationManager
					.getDefaultClinicName(sess);

			if (combo.indexOf(defaultClinicName) != -1) {
				combo.setText(defaultClinicName);
			} else if (combo.getItemCount() > 0) {
				// Set the default to the first item in the combo box
				combo.setText(combo.getItem(0));
			}
		}

		// set the selected clinic to the one logged into
		// only if the user is logged into a down referral clinic
		if (LocalObjects.currentClinic != null
				&& (LocalObjects.currentClinic != LocalObjects.mainClinic)) {
			combo.setText(LocalObjects.currentClinic.getClinicName());
		}

	}

	/**
	 * This method is used whenever a combo box of the available provinces is
	 * shown on a GUI. It first checks if the LocalObjects.provinces list can be
	 * used (to ResourceUtils.getColor(iDartColor.RED)uce hits to the database)
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 */

	public static void populateProvinces(Session sess, CCombo combo) {

		List<String> sdList = AdministrationManager.getProvinces(sess);

		if (sdList != null) {
			for (String s : sdList) {
				if(s != null){
					combo.add(s);
				}

			}
		}

		combo.add("");
		if (combo.getItemCount() > 0) {
			// Set the default to the property
			combo.setText("Seleccione a Província");
		}

	}


	public static void populateFacilityTypes(Session sess, CCombo combo) {

		List<String> sdList = AdministrationManager.getAllFacilityType(sess);

		if (sdList != null) {
			for (String s : sdList) {
				if(s != null){
					combo.add(s);
				}

			}
		}

		combo.add("");
		if (combo.getItemCount() > 0) {
			// Set the default to the property
			combo.setText("Selecione ...");
		}

	}

	public static void populateDeseases(Session sess, CCombo combo) {

		List<String> sdList = AdministrationManager.getDeseases(sess);

		if (sdList != null) {
			for (String s : sdList) {
				if(s != null){
					combo.add(s);
				}

			}
		}

		combo.add("");
		if (combo.getItemCount() > 0) {
			// Set the default to the property
			combo.setText("Seleccione a Doença");
		}

	}

	public static String getReportParameter(Session sess, String name) {

		List<SimpleDomain> sdList = AdministrationManager
				.getReportParameters(sess);

		if (sdList != null) {
			for (SimpleDomain s : sdList) {
				if (s.getName().equalsIgnoreCase(name))
					return s.getValue();
			}
		}

		return "";

	}

	/**
	 * Method populatePrescriptionDuration.
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 *            CCombo
	 */
	public static void populatePrescriptionDuration(Session sess, CCombo combo) {

		List<SimpleDomain> sdList = AdministrationManager.getPrescriptionDurations(sess);

		if (sdList != null) {
			for (SimpleDomain s : sdList) {
				combo.add(s.getValue());
			}
		}

		if (combo.getItemCount() > 0) {
			// Set the default to the first item in the combo box
			 combo.setText(combo.getItem(2));
//			combo.setText("1 mes");
		}

	}

	/**
	 * Method populateClinicalStage.
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 *            CCombo
	 */
	public static void populateClinicalStage(Session sess, CCombo combo) {

		List<SimpleDomain> sdList = AdministrationManager
				.getClinicalStages(sess);

		if (sdList != null) {
			for (SimpleDomain s : sdList) {
				combo.add(s.getValue());
			}
		}

		if (combo.getItemCount() > 0) {
			// Set the default to the first item in the combo box
			combo.setText(combo.getItem(0));
		}

	}

	/**
	 * Method populatePrescriptionUpdateReasons.
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 *            CCombo
	 */
	public static void populatePrescriptionUpdateReasons(Session sess,
														 CCombo combo) {

		List<SimpleDomain> sdList = AdministrationManager.getReasonForUpdate(sess);

		if (sdList != null) {
			for (SimpleDomain s : sdList) {
				combo.add(s.getValue());
			}
		}

		if (combo.getItemCount() > 0) {
			// Set the default to the first item in the combo box
			System.out.println(combo.getItem(2));
		}

	}

	/**
	 * This method is used whenever a combo box of the activationReasons is
	 * shown on a GUI. It first checks if the LocalObjects.activationReasons
	 * list can be used (to ResourceUtils.getColor(iDartColor.RED)uce hits to
	 * the database)
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 * @param
	 */
	public static void populateActivationReasons(Session sess, CCombo combo) {

		List<SimpleDomain> sdList = AdministrationManager
				.getActivationReasons(sess);

		if (sdList != null) {
			for (SimpleDomain s : sdList) {
				combo.add(s.getValue());
			}
		}
		combo.add("");

		if (combo.getItemCount() > 0) {
			// Set the default to the first item in the combo box
			combo.setText(Episode.REASON_NEW_PATIENT);
		}

		combo.setVisibleItemCount(combo.getItemCount());
		combo.setEditable(false);

	}

	/**
	 * This method is used whenever a combo box of the activationReasons is
	 * shown on a GUI. It first checks if the LocalObjects.activationReasons
	 * list can be used (to ResourceUtils.getColor(iDartColor.RED)uce hits to
	 * the database)
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 */
	public static void populateDeactivationReasons(Session sess, CCombo combo) {

		List<SimpleDomain> sdList = AdministrationManager
				.getDeactivationReasons(sess);

		if (sdList != null) {
			for (SimpleDomain s : sdList) {
				combo.add(s.getValue());
			}
		}
		combo.add("");

		combo.setVisibleItemCount(combo.getItemCount());
		combo.setEditable(false);
	}

	/**
	 * This method is used whenever a combo box of the regimens is shown on a
	 * GUI.
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 */
	public static void populateDrugGroups(Session sess, CCombo combo) {

		List<Regimen> regList = AdministrationManager.getDrugGroups(sess);
		combo.add("");

		for (Regimen reg : regList) {
			combo.add(reg.getRegimenName());
		}

		combo.setText("");
	}

	/**
	 * Este metodo popula o ccombo linha terapeutica
	 * @param sess
	 * @param combo
	 * @param inclueTodasLinhas
	 */

	public static void populateLinha(Session sess, CCombo combo,
									 boolean inclueTodasLinhas) {

		List<LinhaT> linhas= AdministrationManager.getAllLinhas(sess);

		for (LinhaT l : linhas) {

			if (inclueTodasLinhas) {
				combo.add(l.getLinhanome());
			} else {
				if (l.isActive()) {
					combo.add(l.getLinhanome());
				}
			}
		}

		if (combo.getItemCount() > 0) {
			// Set the combo box to blank -> ensures that user
			// is forced to enter the information
			combo.setText("");
		}

	}

	public static void populateMotivosPrescricao(Session sess, CCombo combo) {

		List<SimpleDomain> sdList = AdministrationManager
				.getAllMotivoPrescricao(sess);

		if (sdList != null) {
			for (SimpleDomain s : sdList) {
				combo.add(s.getValue());
			}
		}
		combo.add("");

		combo.setVisibleItemCount(combo.getItemCount());
		combo.setEditable(false);

	}


	/**
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 */
	public static void populateForms(Session sess, Combo combo) {

		List<Form> formList = AdministrationManager.getForms(sess);

		for (Form f : formList) {
			combo.add(f.getForm());

		}

		Form defaultForm = AdministrationManager.getForm(sess, "Comprimido");
		if (combo.getItemCount() > 0) {
			if (combo.indexOf(defaultForm.getForm()) != -1) {
				combo.setText(defaultForm.getForm());
			} else {
				combo.setText(combo.getItem(0));
			}
		}

	}

	/**
	 * This method is used whenever a combo box of the available doctors is
	 * shown on a GUI. It first checks if the LocalObjects.doctors list can be
	 * used (to ResourceUtils.getColor(iDartColor.RED)uce hits to the database)
	 *
	 * @param sess
	 *            Session
	 * @param combo
	 * @param includeAllDoctors
	 *            boolean
	 */
	public static void populateDoctors(Session sess, CCombo combo,
									   boolean includeAllDoctors) {

		List<Doctor> doctors = AdministrationManager.getAllDoctors(sess);

		for (Doctor d : doctors) {

			if (includeAllDoctors) {
				combo.add(d.getFullname());
			} else {
				if (d.isActive()) {
					combo.add(d.getFullname());
				}
			}
		}

		if (combo.getItemCount() > 0) {
			// Set the combo box to blank -> ensures that user
			// is forced to enter the information
			combo.setText("");
		}

	}

	/*
	 *
	 *
	 * Popula regimes no formulario de prescricao de medicamentos
	 */


	public static void populateRegimesTerapeuticos(Session sess, CCombo combo,
												   boolean inclueTodosRegimes) {

		List<RegimeTerapeutico> regimes = AdministrationManager.getAllRegimes(sess);

		for (RegimeTerapeutico r : regimes) {

			if (inclueTodosRegimes) {
				combo.add(r.getRegimeesquema());
			} else {
				if (r.isActive()) {
					combo.add(r.getRegimeesquema());
				}
			}
		}

		if (combo.getItemCount() > 0) {
			// Set the combo box to blank -> ensures that user
			// is forced to enter the information
			combo.setText("");
		}

	}

	public static void populateRegimesTerapeuticosCombo(Session sess, Combo combo,
														boolean inclueTodosRegimes) {

		List<RegimeTerapeutico> regimes = AdministrationManager.getAllRegimes(sess);

		for (RegimeTerapeutico r : regimes) {

			if (inclueTodosRegimes) {
				combo.add(r.getRegimeesquema());
			} else {
				if (r.isActive()) {
					combo.add(r.getRegimeesquema());
				}
			}
		}

		if (combo.getItemCount() > 0) {
			// Set the combo box to blank -> ensures that user
			// is forced to enter the information
			combo.setText("");
		}

	}


	/**
	 * Method findMonth.
	 *
	 * @param strMonth
	 *            String
	 * @return String
	 */
	public static String findMonth(String strMonth) {

		String month = "";
		if (strMonth.equals("01")) {
			month = "January";
		} else if (strMonth.equals("02")) {
			month = "February";
		} else if (strMonth.equals("03")) {
			month = "March";
		} else if (strMonth.equals("04")) {
			month = "April";
		} else if (strMonth.equals("05")) {
			month = "May";
		} else if (strMonth.equals("06")) {
			month = "June";
		} else if (strMonth.equals("07")) {
			month = "July";
		} else if (strMonth.equals("08")) {
			month = "August";
		} else if (strMonth.equals("09")) {
			month = "September";
		} else if (strMonth.equals("10")) {
			month = "October";
		} else if (strMonth.equals("11")) {
			month = "November";
		} else if (strMonth.equals("12")) {
			month = "December";
		}

		return month;

	}

	/**
	 * checks if the given date is valid
	 *
	 * @param day
	 * @param month
	 * @param year
	 * @return true if the date is valid else false
	 */
	public static boolean checkDate(int day, int month, int year) {

		boolean result = false;

		month--;

		try {
			switch (month) {

				case Calendar.FEBRUARY:
					if (day >= 1 & day <= 29) {
						GregorianCalendar greg = new GregorianCalendar();
						if (day == 29 & greg.isLeapYear(year)) {
							result = true;
						} else {
							if (day == 29) {
								result = false;
							} else {
								result = true;
							}
						}
					} else {
						result = false;
					}
					break;

				case Calendar.SEPTEMBER:
				case Calendar.APRIL:
				case Calendar.JUNE:
				case Calendar.NOVEMBER:

					if (day >= 1 & day <= 30) {
						result = true;
					} else {
						result = false;
					}
					break;

				case Calendar.JANUARY:
				case Calendar.MARCH:
				case Calendar.MAY:
				case Calendar.JULY:
				case Calendar.AUGUST:
				case Calendar.OCTOBER:
				case Calendar.DECEMBER:
					if (day >= 1 & day <= 31) {
						result = true;
					} else {
						result = false;
					}
					break;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			result = false;
		}

		return result;

	}

	public static void populateRegimens(Session session, CCombo cmbRegimen) {
		List<SimpleDomain> regimens = AdministrationManager
				.getRegimens(session);
		String[] items = new String[regimens.size()];
		for (int i = 0; i < regimens.size(); i++) {
			items[i] = regimens.get(i).getValue();
		}
		cmbRegimen.setItems(items);
	}

	/*
	 *
	 * Este metodo popula o ccombo linha terapeutica  - Idart antigo
	 * Modified by : Colaco Nhongo
	 * Modifica date: 14/01/2017
	 */

	public static void populateLinhas(Session session, CCombo cmbLine) {
		List<LinhaT> lines = AdministrationManager
				.getAllLinhas(session);
		String[] items = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			items[i] = lines.get(i).getLinhanome();
		}
		cmbLine.setItems(items);

	}

	public static void populateDiseases (Session session, CCombo cmbDisease) {
		List<SimpleDomain> sdList = AdministrationManager
				.getAllDiseases(session);

		if (sdList != null) {
			for (SimpleDomain s : sdList) {
				cmbDisease.add(s.getValue());
			}
		}

	}

	public static void populateComboRegimenStatus(Session session, CCombo cmbLine) {
		String[] items = new String[2];
		items[0] =Messages.getString("addRegimen.field.active");
		items[1] = Messages.getString("addRegimen.field.inactive");
		cmbLine.setItems(items);

	}

	public static void populateComboDrugStatus(Session session, CCombo cmbLine) {
		String[] items = new String[2];
		items[0] =Messages.getString("addRegimen.field.active");
		items[1] = Messages.getString("addRegimen.field.inactive");
		cmbLine.setItems(items);

	}

	public static void populateYesNo(Session hSession, CCombo genericYesNo) {
		genericYesNo.add("Sim");
		genericYesNo.add("Nao");
	}

	/**
	 * Este metodo popula o ccombo de motivo de mudanca de ARV
	 * @param hSession
	 * @param cmbMotivoMudanca
	 * @param b
	 */
	public static void populateMotivoMudanca(Session hSession, CCombo cmbMotivoMudanca, boolean b) {


		List<Motivomudanca> motivos = AdministrationManager.getAllMotivos(hSession);

		for (Motivomudanca m : motivos) {

			if (b) {
				cmbMotivoMudanca.add(m.getMotivo());
			} else {
				if (m.isActive()) {
					cmbMotivoMudanca.add(m.getMotivo());
				}
			}
		}

		if (cmbMotivoMudanca.getItemCount() > 0) {
			// Set the combo box to blank -> ensures that user
			// is forced to enter the information
			cmbMotivoMudanca.setText("");
		}



	}

	public static void populateDispensaTrimestral(Session hSession, CCombo cmbDispensaTrimestral) {
		cmbDispensaTrimestral.add("Sim");
		cmbDispensaTrimestral.add("Nao");
	}

	public static void populateDispensaSemestral(Session hSession, CCombo cmbDispensaSemestral) {
		cmbDispensaSemestral.add("Sim");
		cmbDispensaSemestral.add("Nao");
	}

	public static void populateTipoDispensaTrimestral(Session hSession, CCombo cmbDispensaTrimestral){
		cmbDispensaTrimestral.add("Novo");
		cmbDispensaTrimestral.add("Manuntencao");
	}

	public static void populateTipoDispensaSemestral(Session hSession, CCombo cmbTipoDispensaSemestral){
		cmbTipoDispensaSemestral.add("Novo");
		cmbTipoDispensaSemestral.add("Manuntencao");
	}

	// Autocomplete for Tex Field and Combobox
	public static  void enableContentProposal(Control control)
	{
		String[] items =  null;
		SimpleContentProposalProvider proposalProvider = null;
		ContentProposalAdapter proposalAdapter = null;
		if (control instanceof Combo)
		{
			Combo combo = (Combo) control;
			proposalProvider = new SimpleContentProposalProvider(combo.getItems());
			proposalAdapter = new ContentProposalAdapter(
					combo,
					new ComboContentAdapter(),
					proposalProvider,
					getActivationKeystroke(),
					getAutoactivationChars());
		}
		else if (control instanceof Text)
		{

			Text text = (Text) control;
			proposalProvider = new SimpleContentProposalProvider(items);
			proposalAdapter = new ContentProposalAdapter(
					text,
					new TextContentAdapter(),
					proposalProvider,
					getActivationKeystroke(),
					getAutoactivationChars());
		}
		proposalProvider.setFiltering(true);
		proposalAdapter.setPropagateKeys(true);
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);

	}



	// this logic is  from swt addons project
	static char[] getAutoactivationChars() {
		String LCL = "abcdefghijklmnopqrstuvwxyz";
		String UCL = LCL.toUpperCase();
		String NUMS = "0123456789";
		// To enable content proposal on deleting a char

		String delete = new String(new char[] { 8 });
		String allChars = LCL + UCL + NUMS + delete;
		return allChars.toCharArray();
	}

	static KeyStroke getActivationKeystroke() {
		KeyStroke instance = KeyStroke.getInstance(
				new Integer(SWT.CTRL).intValue(), new Integer(' ').intValue());
		return instance;
	}


}
