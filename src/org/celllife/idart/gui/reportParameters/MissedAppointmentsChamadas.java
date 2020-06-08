/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.celllife.idart.gui.reportParameters;

import model.manager.reports.AbsenteeForSupportCall;
import model.manager.reports.MissedAppointmentsReportChamadas;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.celllife.idart.gui.platform.GenericReportGui;
import org.celllife.idart.gui.platform.GenericReportGuiInterface;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartColor;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.*;
import org.vafada.swtcalendar.SWTCalendar;
import org.vafada.swtcalendar.SWTCalendarListener;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author colaco
 */
public class MissedAppointmentsChamadas extends GenericReportGui {

	private Group grpClinicSelection;

	private Label lblClinic;

	private CCombo cmbClinic;
        
    private Button chkBtnDT;

    private Button chkBtnRET;

	private Label lblMinimumDaysLate;

	private Text txtMinimumDaysLate;

	private Text txtMaximumDaysLate;

	private Label lblMaximumDaysLate;

	private Group grpDateRange;

	private SWTCalendar swtCal;

	private List<AbsenteeForSupportCall> supportCallListQuartelyDispensation;

	private List<AbsenteeForSupportCall> supportCallListHold;
	
	private final Shell parent;
	
    private FileOutputStream out = null; 

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Shell
	 * @param activate
	 *            boolean
	 */
	public MissedAppointmentsChamadas(Shell parent, boolean activate) {
		super(parent, GenericReportGuiInterface.REPORTTYPE_CLINICMANAGEMENT,activate);
		this.parent = parent;
	}

	/**
	 * This method initializes newMonthlyStockOverview
	 */
	@Override
	protected void createShell() {
		buildShell(REPORT_MISSED_APPOINTMENTS_CHAMADAS, new Rectangle(100, 50, 600,
				510));
		// create the composites
		createMyGroups();
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
		iDartImage icoImage = iDartImage.REPORT_PATIENTDEFAULTERS;
		buildCompdHeader(REPORT_MISSED_APPOINTMENTS_CHAMADAS, icoImage);
	}

	/**
	 * This method initializes grpClinicSelection
	 * 
	 */
	private void createGrpClinicSelection() {

		grpClinicSelection = new Group(getShell(), SWT.NONE);
		grpClinicSelection.setText("Configuração do Relatório de Faltosos ao Levantamento de ARV em DT");
		grpClinicSelection.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grpClinicSelection.setBounds(new Rectangle(60, 79, 465, 114));

		lblClinic = new Label(grpClinicSelection, SWT.NONE);
		lblClinic.setBounds(new Rectangle(30, 25, 167, 20));
		lblClinic.setText("US");
		lblClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		cmbClinic = new CCombo(grpClinicSelection, SWT.BORDER);
		cmbClinic.setBounds(new Rectangle(202, 25, 160, 20));
		cmbClinic.setEditable(false);
		cmbClinic.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbClinic.setBackground(ResourceUtils.getColor(iDartColor.WHITE));

		CommonObjects.populateClinics(getHSession(), cmbClinic);

		lblMinimumDaysLate = new Label(grpClinicSelection, SWT.NONE);
		lblMinimumDaysLate.setBounds(new Rectangle(31, 57, 147, 21));
		lblMinimumDaysLate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblMinimumDaysLate.setText("Dias Mínimos de Atraso:");

		txtMinimumDaysLate = new Text(grpClinicSelection, SWT.BORDER);
		txtMinimumDaysLate.setBounds(new Rectangle(201, 56, 45, 20));
		txtMinimumDaysLate.setText("4");
                txtMinimumDaysLate.setEditable(true);
		txtMinimumDaysLate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblMaximumDaysLate = new Label(grpClinicSelection, SWT.NONE);
		lblMaximumDaysLate.setBounds(new Rectangle(31, 86, 150, 21));
		lblMaximumDaysLate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblMaximumDaysLate.setText("Dias Máxímos de Atraso:");

		txtMaximumDaysLate = new Text(grpClinicSelection, SWT.BORDER);
		txtMaximumDaysLate.setBounds(new Rectangle(202, 86, 43, 19));
		txtMaximumDaysLate.setText("11");
                txtMaximumDaysLate.setEditable(true);
		txtMaximumDaysLate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
                
                chkBtnDT = new Button(grpClinicSelection, SWT.CHECK);
                chkBtnDT.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 1));
                chkBtnDT.setBounds(new Rectangle(270, 56, 50, 21));
                chkBtnDT.setText("DT");
                chkBtnDT.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
                chkBtnDT.setSelection(false);
                
                chkBtnRET = new Button(grpClinicSelection, SWT.CHECK);
                chkBtnRET.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 1));
                chkBtnRET.setBounds(new Rectangle(270, 86, 150, 21));
                chkBtnRET.setText("Retenção 1 Mês");
                chkBtnRET.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
                chkBtnRET.setSelection(false);

                chkBtnDT.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                    // TODO Auto-generated method stub
                    if (chkBtnDT.getSelection()) 
                        chkBtnRET.setEnabled(false);
                     else 
                        chkBtnRET.setEnabled(true);     
                }
                @Override
                public void widgetDefaultSelected(SelectionEvent arg0) {
                }
            });
                 chkBtnRET.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                    // TODO Auto-generated method stub
                    if (chkBtnRET.getSelection()) 
                        chkBtnDT.setEnabled(false);
                     else 
                        chkBtnDT.setEnabled(true);     
                }
                @Override
                public void widgetDefaultSelected(SelectionEvent arg0) {
                }
            });
                
                
	}

	/**
	 * This method initializes grpDateRange
	 * 
	 */
	private void createGrpDateRange() {

		grpDateRange = new Group(getShell(), SWT.NONE);
		grpDateRange.setText("Seleccione a data de reporte:");
		grpDateRange.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grpDateRange.setBounds(new Rectangle(142, 214, 309, 211));

		swtCal = new SWTCalendar(grpDateRange);
		swtCal.setBounds(40, 40, 220, 160);

	}

	/**
	 * Method getCalendarDate.
	 * 
	 * @return Calendar
	 */
	public Calendar getCalendarDate() {
		return swtCal.getCalendar();
	}

	/**
	 * Method setCalendarDate.
	 * 
	 * @param date
	 *            Date
	 */
	public void setCalendarDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		swtCal.setCalendar(calendar);
	}

	/**
	 * Method addDateChangedListener.
	 * 
	 * @param listener
	 *            SWTCalendarListener
	 */
	public void addDateChangedListener(SWTCalendarListener listener) {

		swtCal.addSWTCalendarListener(listener);
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
		int max = 0;
		int min = 0;

		if (cmbClinic.getText().equals("")) {

			MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
					| SWT.OK);
			missing.setText("Nenhuma US selecionada");
			missing
			.setMessage("Nenhuma US selecionada. Por favor selecione a Unidade Sanitaria.");
			missing.open();
			viewReport = false;

		}
                
                if(!chkBtnDT.getSelection() && !chkBtnRET.getSelection()){
                    MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
					| SWT.OK);
			missing.setText("Nenhum tipo de relatorio foi seleccionado");
			missing
			.setMessage("Nenhum tipo de relatorio foi seleccionado. Por favor selecione a DT ou Retenção 1 Mês.");
			missing.open();
			viewReport = false;
                }

		if (txtMinimumDaysLate.getText().equals("")
				|| txtMaximumDaysLate.getText().equals("")) {
			MessageBox incorrectData = new MessageBox(getShell(),
					SWT.ICON_ERROR | SWT.OK);
			incorrectData.setText("Informacao Invalida");
			incorrectData
			.setMessage("Os dias Maximo e Minimo devem ser numeros.");
			incorrectData.open();
			txtMinimumDaysLate.setText("");
			txtMinimumDaysLate.setFocus();
			viewReport = false;
		} else if (!txtMinimumDaysLate.getText().equals("")
				&& !txtMaximumDaysLate.getText().equals("")) {
			try {
				min = Integer.parseInt(txtMinimumDaysLate.getText());
				max = Integer.parseInt(txtMaximumDaysLate.getText());

				if ((min < 0) || (max < 0)) {
					MessageBox incorrectData = new MessageBox(getShell(),
							SWT.ICON_ERROR | SWT.OK);
					incorrectData.setText("Informacao Invalida");
					incorrectData
					.setMessage("Os dias Maximo e Minimo devem ser numeros.");
					incorrectData.open();
					txtMinimumDaysLate.setText("");
					txtMinimumDaysLate.setFocus();

					viewReport = false;
				}

				if (min >= max) {
					MessageBox incorrectData = new MessageBox(getShell(),
							SWT.ICON_ERROR | SWT.OK);
					incorrectData.setText("Informacao Invalida");
					incorrectData
					.setMessage("O Minimo dia deve ser menor que o maximo dia.");
					incorrectData.open();
					txtMinimumDaysLate.setFocus();

					viewReport = false;
				}

			} catch (NumberFormatException nfe) {
				MessageBox incorrectData = new MessageBox(getShell(),
						SWT.ICON_ERROR | SWT.OK);
				incorrectData.setText("Informacao Invalida");
				incorrectData
				.setMessage("Os dias Maximo e Minimo devem ser numeros.");
				incorrectData.open();
				txtMinimumDaysLate.setText("");
				txtMinimumDaysLate.setFocus();

				viewReport = false;

			}
		}

		if (viewReport) {
                    
                       MissedAppointmentsReportChamadas report = new MissedAppointmentsReportChamadas(
                               getShell(),cmbClinic.getText(),
                               Integer.parseInt(txtMinimumDaysLate.getText()),
                               Integer.parseInt(txtMaximumDaysLate.getText()),
                               swtCal.getCalendar().getTime(),
                               chkBtnDT.getSelection());
			viewReport(report);
		}

	}

	@Override
	protected void cmdViewReportXlsWidgetSelected() {
		
		boolean viewReport = true;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
				
		ConexaoJDBC con = new ConexaoJDBC();
		
		int max = 0;
		int min = 0;
		
		if (cmbClinic.getText().equals("")) {

			MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			missing.setText("Nenhuma US selecionada");
			missing.setMessage("Nenhuma US selecionada. Por favor selecione a Unidade Sanitaria.");
			missing.open();
			viewReport = false;
		}
		
        if(!chkBtnDT.getSelection() && !chkBtnRET.getSelection()) {
            MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            missing.setText("Nenhum tipo de relatorio foi seleccionado");
            missing.setMessage("Nenhum tipo de relatorio foi seleccionado. Por favor selecione a DT ou Retenção 1 Mês.");
            missing.open();
            viewReport = false;
        }
        

		if (txtMinimumDaysLate.getText().equals("") || txtMaximumDaysLate.getText().equals("")) {
			MessageBox incorrectData = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			incorrectData.setText("Informacao Invalida");
			incorrectData.setMessage("Os dias Maximo e Minimo devem ser numeros.");
			incorrectData.open();
			txtMinimumDaysLate.setText("");
			txtMinimumDaysLate.setFocus();
			viewReport = false;
		} else if (!txtMinimumDaysLate.getText().equals("") && !txtMaximumDaysLate.getText().equals("")) {
			
			try {
				
				min = Integer.parseInt(txtMinimumDaysLate.getText());
				max = Integer.parseInt(txtMaximumDaysLate.getText());

				if ((min < 0) || (max < 0)) {
					MessageBox incorrectData = new MessageBox(getShell(),
							SWT.ICON_ERROR | SWT.OK);
					incorrectData.setText("Informacao Invalida");
					incorrectData.setMessage("Os dias Maximo e Minimo devem ser numeros.");
					incorrectData.open();
					txtMinimumDaysLate.setText("");
					txtMinimumDaysLate.setFocus();
					viewReport = false;
				}

				if (min >= max) {
					MessageBox incorrectData = new MessageBox(getShell(),SWT.ICON_ERROR | SWT.OK);
					incorrectData.setText("Informacao Invalida");
					incorrectData.setMessage("O Minimo dia deve ser menor que o maximo dia.");
					incorrectData.open();
					txtMinimumDaysLate.setFocus();
					viewReport = false;
				}

			} catch (NumberFormatException nfe) {
				MessageBox incorrectData = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
				incorrectData.setText("Informacao Invalida");
				incorrectData.setMessage("Os dias Maximo e Minimo devem ser numeros.");
				incorrectData.open();
				txtMinimumDaysLate.setText("");
				txtMinimumDaysLate.setFocus();
				viewReport = false;
			}
		}
		
		if (viewReport) {
			if (chkBtnDT.getSelection()) { 
		        
				try {
					supportCallListQuartelyDispensation = con.getAbsenteeForSupportCallQuartelyDispensation(txtMinimumDaysLate.getText(), txtMaximumDaysLate.getText(), 
							sdf.format(swtCal.getCalendar().getTime()), String.valueOf(LocalObjects.mainClinic.getId()));
					
					if(supportCallListQuartelyDispensation.size() > 0) {
						
						FileInputStream currentXls = new FileInputStream("Reports/ChamadasFaltososDT.xls");
						
						HSSFWorkbook workbook = new HSSFWorkbook(currentXls);
						
						HSSFSheet sheet = workbook.getSheetAt(0);
						
						HSSFCellStyle cellStyle = workbook.createCellStyle();
						cellStyle.setBorderBottom(BorderStyle.THIN);
						cellStyle.setBorderTop(BorderStyle.THIN);
						cellStyle.setBorderLeft(BorderStyle.THIN);
						cellStyle.setBorderRight(BorderStyle.THIN);
						cellStyle.setAlignment(HorizontalAlignment.CENTER);
						
						HSSFCellStyle cellFontStyle = workbook.createCellStyle();
						HSSFFont font = workbook.createFont();
						font.setFontHeightInPoints((short) 14); 
						cellFontStyle.setFont(font );
												
						HSSFRow healthFacility = sheet.getRow(10); 
						HSSFCell healthFacilityCell = healthFacility.createCell(2); 
						healthFacilityCell.setCellValue(LocalObjects.currentClinic.getClinicName());
						healthFacilityCell.setCellStyle(cellStyle); 
						
						HSSFRow reportPeriod = sheet.getRow(10);
						HSSFCell reportPeriodCell = reportPeriod.createCell(9);
						reportPeriodCell.setCellValue(sdf.format(swtCal.getCalendar().getTime()));
						reportPeriodCell.setCellStyle(cellStyle); 

						HSSFRow reportYear = sheet.getRow(11);
						HSSFCell reportYearCell = reportYear.createCell(9);
						reportYearCell.setCellValue(sdfYear.format(swtCal.getCalendar().getTime()));
						reportYearCell.setCellStyle(cellStyle);
						
						HSSFRow minMax = sheet.getRow(8);
						HSSFCell minMaxCell = minMax.createCell(4);
						minMaxCell.setCellValue("Este relatório mostra os pacientes que faltaram entre " + txtMinimumDaysLate.getText() + " e " + txtMaximumDaysLate.getText() + " dias");
						minMaxCell.setCellStyle(cellFontStyle); 

						  for(int i=14; i<= sheet.getLastRowNum(); i++) 
						  { 
							HSSFRow row = sheet.getRow(i);
						  	deleteRow(sheet,row);  
						  }
						 
						  out = new FileOutputStream(new File("Reports/ChamadasFaltososDT.xls"));
						  workbook.write(out); 
						
						int rowNum = 14;
						
						for (AbsenteeForSupportCall xls : supportCallListQuartelyDispensation) { 
							
							HSSFRow row = sheet.createRow(rowNum++);
							
							HSSFCell createCellNid = row.createCell(1);
							createCellNid.setCellValue(xls.getPatientIdentifier());
							createCellNid.setCellStyle(cellStyle); 
							
							HSSFCell createCellNome = row.createCell(2);
							createCellNome.setCellValue(xls.getNome());
							createCellNome.setCellStyle(cellStyle);

							HSSFCell createCellDataQueFaltouLevantamento = row.createCell(3);
							createCellDataQueFaltouLevantamento.setCellValue(xls.getDataQueFaltouLevantamento());
							createCellDataQueFaltouLevantamento.setCellStyle(cellStyle);

							HSSFCell createCellDataIdentificouAbandonoTarv = row.createCell(4); 
							createCellDataIdentificouAbandonoTarv.setCellValue(xls.getDataIdentificouAbandonoTarv());
							createCellDataIdentificouAbandonoTarv.setCellStyle(cellStyle);
							
							HSSFCell createCellEfectuouLigacao = row.createCell(5); 
							createCellEfectuouLigacao.setCellValue("");
							createCellEfectuouLigacao.setCellStyle(cellStyle);

							HSSFCell createCellDataRegressoUnidadeSanitaria = row.createCell(6); 
							createCellDataRegressoUnidadeSanitaria.setCellValue(xls.getDataRegressoUnidadeSanitaria());
							createCellDataRegressoUnidadeSanitaria.setCellStyle(cellStyle);
							
							HSSFCell createCellChamadaEfectuada = row.createCell(7); 
							createCellChamadaEfectuada.setCellValue("");
							createCellChamadaEfectuada.setCellStyle(cellStyle);

							HSSFCell createCellContacto = row.createCell(8); 
							createCellContacto.setCellValue(xls.getContacto());
							createCellContacto.setCellStyle(cellStyle);
							
							HSSFCell createCellFaltososSemana = row.createCell(9); 
							createCellFaltososSemana.setCellValue("");
							createCellFaltososSemana.setCellStyle(cellStyle);
						}
						
						currentXls.close();
						
						FileOutputStream outputStream = new FileOutputStream(new File("Reports/ChamadasFaltososDT.xls")); 
						workbook.write(outputStream);
						workbook.close();
						
						Desktop.getDesktop().open(new File("Reports/ChamadasFaltososDT.xls"));
						
					} else {
						MessageBox mNoPages = new MessageBox(parent,SWT.ICON_ERROR | SWT.OK);
						mNoPages.setText("O relatório não possui páginas");
						mNoPages.setMessage("O relatório que estás a gerar não contém nenhum dado. \\ n \\ n Verifique os valores de entrada que inseriu (como datas) para este relatório e tente novamente.");
						mNoPages.open();
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} else {
				try {
					supportCallListHold = con.getAbsenteeForSupportCallHold(txtMinimumDaysLate.getText(), txtMaximumDaysLate.getText(), sdf.format(swtCal.getCalendar().getTime()), String.valueOf(LocalObjects.mainClinic.getId()));
					
					if(supportCallListHold.size() > 0) {
						
						FileInputStream currentXls = new FileInputStream("Reports/ChamadasFaltososRET.xls"); 
						
						HSSFWorkbook workbook = new HSSFWorkbook(currentXls);
						
						HSSFSheet sheet = workbook.getSheetAt(0);
						
						HSSFCellStyle cellStyle = workbook.createCellStyle();
						cellStyle.setBorderBottom(BorderStyle.THIN);
						cellStyle.setBorderTop(BorderStyle.THIN);
						cellStyle.setBorderLeft(BorderStyle.THIN);
						cellStyle.setBorderRight(BorderStyle.THIN);
						cellStyle.setAlignment(HorizontalAlignment.CENTER);

						HSSFCellStyle cellFontStyle = workbook.createCellStyle();
						HSSFFont font = workbook.createFont();
						font.setFontHeightInPoints((short) 14); 
						cellFontStyle.setFont(font );
												
						HSSFRow healthFacility = sheet.getRow(10); 
						HSSFCell healthFacilityCell = healthFacility.createCell(2); 
						healthFacilityCell.setCellValue(LocalObjects.currentClinic.getClinicName());
						healthFacilityCell.setCellStyle(cellStyle); 
						
						HSSFRow reportPeriod = sheet.getRow(10);
						HSSFCell reportPeriodCell = reportPeriod.createCell(9);
						reportPeriodCell.setCellValue(sdf.format(swtCal.getCalendar().getTime()));
						reportPeriodCell.setCellStyle(cellStyle); 

						HSSFRow reportYear = sheet.getRow(11);
						HSSFCell reportYearCell = reportYear.createCell(9);
						reportYearCell.setCellValue(sdfYear.format(swtCal.getCalendar().getTime()));
						reportYearCell.setCellStyle(cellStyle);
						
						HSSFRow minMax = sheet.getRow(8);
						HSSFCell minMaxCell = minMax.createCell(4);
						minMaxCell.setCellValue("Este relatório mostra os pacientes que faltaram entre " + txtMinimumDaysLate.getText() + " e " + txtMaximumDaysLate.getText() + " dias");
						minMaxCell.setCellStyle(cellFontStyle); 

						  for(int i=14; i<= sheet.getLastRowNum(); i++) 
						  { 
							HSSFRow row = sheet.getRow(i);
						  	deleteRow(sheet,row);  
						  }
						 
						  out = new FileOutputStream(new File("Reports/ChamadasFaltososRET.xls"));
						  workbook.write(out); 
						
						int rowNum = 14;
						
						for (AbsenteeForSupportCall xls : supportCallListHold) { 
							
							HSSFRow row = sheet.createRow(rowNum++);
							
							HSSFCell createCellNid = row.createCell(1);
							createCellNid.setCellValue(xls.getPatientIdentifier());
							createCellNid.setCellStyle(cellStyle); 
							
							HSSFCell createCellNome = row.createCell(2);
							createCellNome.setCellValue(xls.getNome());
							createCellNome.setCellStyle(cellStyle);

							HSSFCell createCellDataQueFaltouLevantamento = row.createCell(3);
							createCellDataQueFaltouLevantamento.setCellValue(xls.getDataQueFaltouLevantamento());
							createCellDataQueFaltouLevantamento.setCellStyle(cellStyle);

							HSSFCell createCellDataIdentificouAbandonoTarv = row.createCell(4); 
							createCellDataIdentificouAbandonoTarv.setCellValue(xls.getDataIdentificouAbandonoTarv());
							createCellDataIdentificouAbandonoTarv.setCellStyle(cellStyle);
							
							HSSFCell createCellEfectuouLigacao = row.createCell(5); 
							createCellEfectuouLigacao.setCellValue("");
							createCellEfectuouLigacao.setCellStyle(cellStyle);

							HSSFCell createCellDataRegressoUnidadeSanitaria = row.createCell(6); 
							createCellDataRegressoUnidadeSanitaria.setCellValue(xls.getDataRegressoUnidadeSanitaria());
							createCellDataRegressoUnidadeSanitaria.setCellStyle(cellStyle);
							
							HSSFCell createCellChamadaEfectuada = row.createCell(7); 
							createCellChamadaEfectuada.setCellValue("");
							createCellChamadaEfectuada.setCellStyle(cellStyle);

							HSSFCell createCellContacto = row.createCell(8); 
							createCellContacto.setCellValue(xls.getContacto());
							createCellContacto.setCellStyle(cellStyle);
							
							HSSFCell createCellFaltososSemana = row.createCell(9); 
							createCellFaltososSemana.setCellValue("");
							createCellFaltososSemana.setCellStyle(cellStyle);
						}
						
						currentXls.close();
						
						FileOutputStream outputStream = new FileOutputStream(new File("Reports/ChamadasFaltososRET.xls"));  
						workbook.write(outputStream);
						workbook.close();
						
						Desktop.getDesktop().open(new File("Reports/ChamadasFaltososRET.xls"));
						
					} else {
						MessageBox mNoPages = new MessageBox(parent,SWT.ICON_ERROR | SWT.OK);
						mNoPages.setText("O relatório não possui páginas");
						mNoPages.setMessage("O relatório que estás a gerar não contém nenhum dado. \\ n \\ n Verifique os valores de entrada que inseriu (como datas) para este relatório e tente novamente.");
						mNoPages.open();
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
	
	private void deleteRow(HSSFSheet sheet, Row row) {
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum > 0) {
			int rowIndex = row.getRowNum();
			HSSFRow removingRow = sheet.getRow(rowIndex);
			if (removingRow != null) {
				sheet.removeRow(removingRow);
			}
		}
	}
}
