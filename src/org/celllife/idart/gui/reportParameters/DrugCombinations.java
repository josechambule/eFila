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

package org.celllife.idart.gui.reportParameters;

import model.manager.AdministrationManager;
import model.manager.reports.DrugCombinationReport;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.gui.platform.GenericReportGui;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartColor;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.celllife.idart.misc.SafeSaveDialog;
import org.celllife.idart.misc.SafeSaveDialog.FileType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.vafada.swtcalendar.SWTCalendar;
import org.vafada.swtcalendar.SWTCalendarListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 */
public class DrugCombinations extends GenericReportGui {

	private Group grpClinicSelection;

	private Label lblClinic;

	private CCombo cmbClinic;

	private Label lblYoungCutoffAge;

	private Text txtYoungCutoffAge;

	private Text txtCutoffAge;

	private Label lblCutoffAge;

	private Label lblCuttoffYears;

	private Label lblYoungCutoffYears;

	private SWTCalendar calendarStart;

	private SWTCalendar calendarEnd;

	private Group grpDateRange;

	private Label lblStartDate;

	private Label lblEndDate;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Shell
	 * @param activate
	 *            boolean
	 */

	public DrugCombinations(Shell parent, boolean activate) {
		super(parent, REPORTTYPE_MONITORINGANDEVALUATION, activate);
	}

	/**
	 * This method initializes newMonthlyStockOverview
	 */
	@Override
	protected void createShell() {
		buildShell(REPORT_DRUG_COMBINATIONS, new Rectangle(70, 50,
				700, 600));
		// create the composites
		createMyGroups();

		Label lblWaitWhileLoading = new Label(getShell(), SWT.CENTER);
		lblWaitWhileLoading.setBounds(new Rectangle(120, 490, 448, 21));
		lblWaitWhileLoading
		.setText("Este relatório pode levar algum tempo para executar, por favor seja paciente.");
		lblWaitWhileLoading
		.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
	}

	private void createMyGroups() {
		createGrpClinicSelection();
		createGrpDateRange();
	}

	/**
	 * This method initializes compHeader
	 * 
	 */
	@Override
	protected void createCompHeader() {
		iDartImage icoImage = iDartImage.REPORT_ACTIVEPATIENTS;
		buildCompdHeader(REPORT_DRUG_COMBINATIONS, icoImage);
	}

	/**
	 * This method initializes grpClinicSelection
	 * 
	 */
	private void createGrpClinicSelection() {

		grpClinicSelection = new Group(getShell(), SWT.NONE);
		grpClinicSelection.setText("Unidade Sanitária");
		grpClinicSelection.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grpClinicSelection.setBounds(new Rectangle(151, 83, 386, 123));

		lblClinic = new Label(grpClinicSelection, SWT.NONE);
		lblClinic.setBounds(new Rectangle(9, 25, 151, 20));
		lblClinic.setText("Seleccione a US:");
		lblClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		cmbClinic = new CCombo(grpClinicSelection, SWT.BORDER);
		cmbClinic.setBounds(new Rectangle(169, 25, 176, 20));
		cmbClinic.setEditable(false);
		cmbClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbClinic.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		CommonObjects.populateClinics(getHSession(), cmbClinic);

		lblYoungCutoffAge = new Label(grpClinicSelection, SWT.NONE);
		lblYoungCutoffAge.setBounds(new Rectangle(10, 57, 230, 21));
		lblYoungCutoffAge.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblYoungCutoffAge.setText("Idade Mínima para Pacientes Pediátricos:");

		txtYoungCutoffAge = new Text(grpClinicSelection, SWT.BORDER);
		txtYoungCutoffAge.setBounds(new Rectangle(242, 57, 45, 20));
		txtYoungCutoffAge.setText("0");
		txtYoungCutoffAge.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblYoungCutoffYears = new Label(grpClinicSelection, SWT.NONE);
		lblYoungCutoffYears.setBounds(new Rectangle(295, 58, 50, 20));
		lblYoungCutoffYears.setText("Ano(s)");
		lblYoungCutoffYears
		.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblCutoffAge = new Label(grpClinicSelection, SWT.NONE);
		lblCutoffAge.setBounds(new Rectangle(10, 86, 227, 20));
		lblCutoffAge.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblCutoffAge.setText("Idade Máxima  para Pacientes Pediátricos:");

		txtCutoffAge = new Text(grpClinicSelection, SWT.BORDER);
		txtCutoffAge.setBounds(new Rectangle(243, 87, 43, 19));
		txtCutoffAge.setText("14");
		txtCutoffAge.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblCuttoffYears = new Label(grpClinicSelection, SWT.NONE);
		lblCuttoffYears.setBounds(new Rectangle(295, 87, 50, 20));
		lblCuttoffYears.setText("Ano(s)");
		lblCuttoffYears.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

	}

	/**
	 * This method initializes grpDateRange
	 * 
	 */
	private void createGrpDateRange() {

		grpDateRange = new Group(getShell(), SWT.NONE);
		grpDateRange.setText("Período:");
		grpDateRange.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grpDateRange.setBounds(new Rectangle(68, 231, 520, 201));
		grpDateRange.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblStartDate = new Label(grpDateRange, SWT.CENTER | SWT.BORDER);
		lblStartDate.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 30,
				180, 20));
		lblStartDate.setText("Seleccione Data Início:");
		lblStartDate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblEndDate = new Label(grpDateRange, SWT.CENTER | SWT.BORDER);
		lblEndDate.setBounds(new org.eclipse.swt.graphics.Rectangle(300, 30,
				180, 20));
		lblEndDate.setText("Seleccione Data Fim:");
		lblEndDate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		calendarStart = new SWTCalendar(grpDateRange);
		calendarStart.setBounds(20, 55, 220, 140);

		calendarEnd = new SWTCalendar(grpDateRange);
		calendarEnd.setBounds(280, 55, 220, 140);

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
	 * Method getCalendarEnd.
	 * 
	 * @return Calendar
	 */
	public Calendar getCalendarEnd() {
		return calendarEnd.getCalendar();
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
	 * Method setEndDate.
	 * 
	 * @param date
	 *            Date
	 */
	public void setEndDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendarEnd.setCalendar(calendar);
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
	 * This method initializes compButtons
	 * 
	 */
	@Override
	protected void createCompButtons() {
	}

	@Override
	protected void cmdViewReportWidgetSelected() {

		boolean viewReport = true;

		if (cmbClinic.getText().equals("")) {

			MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
					| SWT.OK);
			missing.setText("A US não foi seleccionada");
			missing
			.setMessage("Por favor, seleccione uma US apresentada na lista.");
			missing.open();
			viewReport = false;

		}

		if (txtYoungCutoffAge.getText().equals("")) {
			MessageBox incorrectData = new MessageBox(getShell(),
					SWT.ICON_ERROR | SWT.OK);
			incorrectData.setText("Número incorreto");
			incorrectData
			.setMessage("Por favor introduza um número mínimo correcto.");
			incorrectData.open();
			txtYoungCutoffAge.setText("");
			txtYoungCutoffAge.setFocus();

			viewReport = false;
		}

		if (!txtYoungCutoffAge.getText().equals("")) {
			try {
				Integer.parseInt(txtYoungCutoffAge.getText());
			} catch (NumberFormatException nfe) {
				MessageBox incorrectData = new MessageBox(getShell(),
						SWT.ICON_ERROR | SWT.OK);
				incorrectData.setText("Número incorreto");
				incorrectData
				.setMessage("Por favor introduza um número mínimo correcto.");
				incorrectData.open();
				txtYoungCutoffAge.setText("");
				txtYoungCutoffAge.setFocus();

				viewReport = false;

			}
		}

		if (txtCutoffAge.getText().equals("")) {
			MessageBox incorrectData = new MessageBox(getShell(),
					SWT.ICON_ERROR | SWT.OK);
			incorrectData.setText("Número incorreto");
			incorrectData
			.setMessage("Por favor introduza um número máximo correcto.");
			incorrectData.open();
			txtCutoffAge.setText("");
			txtCutoffAge.setFocus();

			viewReport = false;
		}

		if (!txtCutoffAge.getText().equals("")) {
			try {
				Integer.parseInt(txtCutoffAge.getText());
			} catch (NumberFormatException nfe) {
				MessageBox incorrectData = new MessageBox(getShell(),
						SWT.ICON_ERROR | SWT.OK);
				incorrectData.setText("Número incorreto");
				incorrectData
				.setMessage("Por favor introduza um número máximo correcto.");
				incorrectData.open();
				txtCutoffAge.setText("");
				txtCutoffAge.setFocus();

				viewReport = false;

			}
		}

		if (calendarStart.getCalendar().getTime().after(
				calendarEnd.getCalendar().getTime())) {

			MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR);
			mb.setText("Data Fim Inválida");
			mb.setMessage("Por favor seleccione uma data fim que é depois da data inicio");
			mb.open();

			viewReport = false;
		}

		if (viewReport) {
			SafeSaveDialog sd = new SafeSaveDialog(getShell(), FileType.CSV);
			sd.setText("Selecione o ficheiro a ser gravado.");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sd.setFileName(sdf.format(new Date()) + "-regimenCVSReport.csv");
			final String filename = sd.open();
			if (filename != null) {
				DrugCombinationReport report = new DrugCombinationReport(
						getShell(), calendarStart.getCalendar().getTime(),
						calendarEnd.getCalendar().getTime(),
						AdministrationManager.getClinic(getHSession(),
								cmbClinic.getText()), Integer
								.parseInt(txtYoungCutoffAge.getText()), Integer
								.parseInt(txtCutoffAge.getText()), filename);
				viewReport(report);
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

	@Override
	protected void setLogger() {
		setLog(Logger.getLogger(this.getClass()));
	}

	@Override
	protected void cmdViewReportXlsWidgetSelected() {
		// TODO Auto-generated method stub
		
	}

}
