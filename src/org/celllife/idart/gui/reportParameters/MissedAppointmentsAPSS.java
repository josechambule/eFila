/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.celllife.idart.gui.reportParameters;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.vafada.swtcalendar.SWTCalendar;
import org.vafada.swtcalendar.SWTCalendarListener;

import model.manager.reports.MissedAppointmentsAPSSReport;
import model.manager.reports.RegistoChamadaTelefonicaXLS;

/**
 *
 * @author colaco
 */
public class MissedAppointmentsAPSS extends GenericReportGui {

	private Group grpClinicSelection;

	private Label lblClinic;

	private CCombo cmbClinic;

	private Label lblMinimumDaysLate;

	private Text txtMinimumDaysLate;

	private Text txtMaximumDaysLate;

	private Label lblMaximumDaysLate;

	private Group grpDateRange;

	private SWTCalendar swtCal;
        
    private Button chkBtnALL;

    private Button chkBtnPTV;
        
    private Button chkBtnTB;
    
    private List<RegistoChamadaTelefonicaXLS> chamadaTelefonicaXLSs;
    
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
	public MissedAppointmentsAPSS(Shell parent, boolean activate) {
		super(parent, GenericReportGuiInterface.REPORTTYPE_CLINICMANAGEMENT,
				activate);
		this.parent = parent;
	}

	/**
	 * This method initializes newMonthlyStockOverview
	 */
	@Override
	protected void createShell() {
		buildShell(REPORT_MISSED_APPOINTMENTS_APSS, new Rectangle(100, 50, 600,
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
		buildCompdHeader(REPORT_MISSED_APPOINTMENTS_APSS, icoImage);
	}

	/**
	 * This method initializes grpClinicSelection
	 * 
	 */
	private void createGrpClinicSelection() {

		grpClinicSelection = new Group(getShell(), SWT.NONE);
		grpClinicSelection.setText("Configuração do Relatório de Faltosos e/ou Abandonos");
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
		txtMinimumDaysLate.setText("5");
                txtMinimumDaysLate.setEditable(true);
		txtMinimumDaysLate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblMaximumDaysLate = new Label(grpClinicSelection, SWT.NONE);
		lblMaximumDaysLate.setBounds(new Rectangle(31, 86, 150, 21));
		lblMaximumDaysLate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblMaximumDaysLate.setText("Dias Máxímos de Atraso:");

		txtMaximumDaysLate = new Text(grpClinicSelection, SWT.BORDER);
		txtMaximumDaysLate.setBounds(new Rectangle(202, 86, 43, 19));
		txtMaximumDaysLate.setText("9");
                txtMaximumDaysLate.setEditable(true);
		txtMaximumDaysLate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
                
                chkBtnALL = new Button(grpClinicSelection, SWT.CHECK);
                chkBtnALL.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 1));
                chkBtnALL.setBounds(new Rectangle(320, 56, 135, 21));
                chkBtnALL.setText("ADULTO E PEDIATRIA");
                chkBtnALL.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
                chkBtnALL.setSelection(false);
                
                chkBtnPTV = new Button(grpClinicSelection, SWT.CHECK);
                chkBtnPTV.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 1));
                chkBtnPTV.setBounds(new Rectangle(270, 86, 150, 21));
                chkBtnPTV.setText("SMI");
                chkBtnPTV.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
                chkBtnPTV.setSelection(false);

                chkBtnTB = new Button(grpClinicSelection, SWT.CHECK);
                chkBtnTB.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false, 1, 1));
                chkBtnTB.setBounds(new Rectangle(270, 56, 150, 21));
                chkBtnTB.setText("TB");
                chkBtnTB.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
                chkBtnTB.setSelection(false);
                
                chkBtnALL.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                    // TODO Auto-generated method stub
                    if (chkBtnALL.getSelection()){ 
                        chkBtnPTV.setEnabled(false);
                        chkBtnTB.setEnabled(false);
                    }else{ 
                        chkBtnPTV.setEnabled(true); 
                        chkBtnTB.setEnabled(true);
                    }
                }
                @Override
                public void widgetDefaultSelected(SelectionEvent arg0) {
                }
            });
                 chkBtnTB.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                    // TODO Auto-generated method stub
                    if (chkBtnTB.getSelection()){ 
                        chkBtnALL.setEnabled(false);
                        chkBtnPTV.setEnabled(false);
                    }else{ 
                        chkBtnALL.setEnabled(true);
                        chkBtnPTV.setEnabled(true);
                    }
                }
                @Override
                public void widgetDefaultSelected(SelectionEvent arg0) {
                }
            });
                 
            chkBtnPTV.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                    // TODO Auto-generated method stub
                    if (chkBtnPTV.getSelection()){ 
                        chkBtnALL.setEnabled(false);
                        chkBtnTB.setEnabled(false);
                    }else{ 
                        chkBtnALL.setEnabled(true);
                        chkBtnTB.setEnabled(true);
                    }
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
			missing.setText("No Clinic Was Selected");
			missing
			.setMessage("No clinic was selected. Please select a clinic by looking through the list of available clinics.");
			missing.open();
			viewReport = false;

		}

                  if(!chkBtnALL.getSelection() && !chkBtnPTV.getSelection() && !chkBtnTB.getSelection()){
                    MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
					| SWT.OK);
			missing.setText("Nenhum tipo de relatorio foi seleccionado");
			missing
			.setMessage("Nenhum tipo de relatorio foi seleccionado. Por favor selecione ALL, TB ou PTV.");
			missing.open();
			viewReport = false;
                }
                
		if (txtMinimumDaysLate.getText().equals("")
				|| txtMaximumDaysLate.getText().equals("")) {
			MessageBox incorrectData = new MessageBox(getShell(),
					SWT.ICON_ERROR | SWT.OK);
			incorrectData.setText("Invalid Information");
			incorrectData
			.setMessage("The minimum and maximum days late must both be numbers.");
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
					incorrectData.setText("Invalid Information");
					incorrectData
					.setMessage("The minimum and maximum days late must both be positive numbers.");
					incorrectData.open();
					txtMinimumDaysLate.setText("");
					txtMinimumDaysLate.setFocus();

					viewReport = false;
				}

				if (min >= max) {
					MessageBox incorrectData = new MessageBox(getShell(),
							SWT.ICON_ERROR | SWT.OK);
					incorrectData.setText("Invalid Information");
					incorrectData
					.setMessage("The minimum days late must be smaller than the maximum days late.");
					incorrectData.open();
					txtMinimumDaysLate.setFocus();

					viewReport = false;
				}

			} catch (NumberFormatException nfe) {
				MessageBox incorrectData = new MessageBox(getShell(),
						SWT.ICON_ERROR | SWT.OK);
				incorrectData.setText("Invalid Information");
				incorrectData
				.setMessage("The minimum and maximum days late must both be whole numbers.");
				incorrectData.open();
				txtMinimumDaysLate.setText("");
				txtMinimumDaysLate.setFocus();

				viewReport = false;

			}
		}

		if (viewReport) {
			    MissedAppointmentsAPSSReport report = new MissedAppointmentsAPSSReport(getShell(),cmbClinic.getText(),
					Integer.parseInt(txtMinimumDaysLate.getText()),
					Integer.parseInt(txtMaximumDaysLate.getText()),
					swtCal.getCalendar().getTime(),chkBtnALL.getSelection(),chkBtnPTV.getSelection(),chkBtnTB.getSelection());
			viewReport(report);
		}

	}

	@Override
	protected void cmdViewReportXlsWidgetSelected() {

		boolean viewReport = true;
		int max = 0;
		int min = 0;

		if (cmbClinic.getText().equals("")) {

			MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
					| SWT.OK);
			missing.setText("No Clinic Was Selected");
			missing
			.setMessage("No clinic was selected. Please select a clinic by looking through the list of available clinics.");
			missing.open();
			viewReport = false;

		}

                  if(!chkBtnALL.getSelection() && !chkBtnPTV.getSelection() && !chkBtnTB.getSelection()){
                    MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
					| SWT.OK);
			missing.setText("Nenhum tipo de relatorio foi seleccionado");
			missing
			.setMessage("Nenhum tipo de relatorio foi seleccionado. Por favor selecione ALL, TB ou PTV.");
			missing.open();
			viewReport = false;
                }
                
		if (txtMinimumDaysLate.getText().equals("")
				|| txtMaximumDaysLate.getText().equals("")) {
			MessageBox incorrectData = new MessageBox(getShell(),
					SWT.ICON_ERROR | SWT.OK);
			incorrectData.setText("Invalid Information");
			incorrectData
			.setMessage("The minimum and maximum days late must both be numbers.");
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
					incorrectData.setText("Invalid Information");
					incorrectData
					.setMessage("The minimum and maximum days late must both be positive numbers.");
					incorrectData.open();
					txtMinimumDaysLate.setText("");
					txtMinimumDaysLate.setFocus();

					viewReport = false;
				}

				if (min >= max) {
					MessageBox incorrectData = new MessageBox(getShell(),
							SWT.ICON_ERROR | SWT.OK);
					incorrectData.setText("Invalid Information");
					incorrectData
					.setMessage("The minimum days late must be smaller than the maximum days late.");
					incorrectData.open();
					txtMinimumDaysLate.setFocus();

					viewReport = false;
				}

			} catch (NumberFormatException nfe) {
				MessageBox incorrectData = new MessageBox(getShell(),
						SWT.ICON_ERROR | SWT.OK);
				incorrectData.setText("Invalid Information");
				incorrectData
				.setMessage("The minimum and maximum days late must both be whole numbers.");
				incorrectData.open();
				txtMinimumDaysLate.setText("");
				txtMinimumDaysLate.setFocus();

				viewReport = false;

			}
		}
		
		if(viewReport) {	
			ConexaoJDBC con=new ConexaoJDBC();
			if (chkBtnALL.getSelection()) { 
				
				chamadaTelefonicaXLSs = con.getMissedAppointmentsReport(txtMinimumDaysLate.getText(),txtMaximumDaysLate.getText(),
						swtCal.getCalendar().getTime(),String.valueOf(LocalObjects.mainClinic.getId())); 
			} else if (chkBtnTB.getSelection()) {
				
				chamadaTelefonicaXLSs = con.getMissedAppointmentsPTV(txtMinimumDaysLate.getText(),txtMaximumDaysLate.getText(),
						swtCal.getCalendar().getTime(),String.valueOf(LocalObjects.mainClinic.getId()));
			} else {
				
				chamadaTelefonicaXLSs = con.getMissedAppointmentsSMI(txtMinimumDaysLate.getText(),txtMaximumDaysLate.getText(),
						swtCal.getCalendar().getTime(),String.valueOf(LocalObjects.mainClinic.getId()));
			}
			
			try {
				if(chamadaTelefonicaXLSs.size() > 0) {
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
					
					SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
					
					FileInputStream currentXls = new FileInputStream("RegistoChamadaTelefonica.xls");
					
					HSSFWorkbook workbook = new HSSFWorkbook(currentXls);
					
					HSSFSheet sheet = workbook.getSheetAt(0);
					
					HSSFCellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setBorderBottom(BorderStyle.THIN);
					cellStyle.setBorderTop(BorderStyle.THIN);
					cellStyle.setBorderLeft(BorderStyle.THIN);
					cellStyle.setBorderRight(BorderStyle.THIN);
					cellStyle.setAlignment(HorizontalAlignment.CENTER);

											
					HSSFRow healthFacility = sheet.getRow(10); 
					HSSFCell healthFacilityCell = healthFacility.createCell(2); 
					healthFacilityCell.setCellValue(LocalObjects.currentClinic.getClinicName());
					healthFacilityCell.setCellStyle(cellStyle); 
					
					HSSFRow reportPeriod = sheet.getRow(10);
					HSSFCell reportPeriodCell = reportPeriod.createCell(20);
					reportPeriodCell.setCellValue(sdf.format(DateUtils.addDays(swtCal.getCalendar().getTime(), -(Integer.parseInt(txtMaximumDaysLate.getText())))) +" à "+ 
							sdf.format(DateUtils.addDays(swtCal.getCalendar().getTime(), -(Integer.parseInt(txtMinimumDaysLate.getText())))));
					reportPeriodCell.setCellStyle(cellStyle); 

					HSSFRow reportYear = sheet.getRow(11);
					HSSFCell reportYearCell = reportYear.createCell(20);
					reportYearCell.setCellValue(sdfYear.format(swtCal.getCalendar().getTime()));
					reportYearCell.setCellStyle(cellStyle); 
					
					HSSFRow daysPeriod = sheet.getRow(11);
					HSSFCell daysCell = daysPeriod.createCell(5);
					daysCell.setCellValue("Este relatório mostra os pacientes que têm entre " + txtMinimumDaysLate.getText() + " e " + txtMaximumDaysLate.getText());
					daysCell.setCellStyle(cellStyle); 

					  for(int i=15; i<= sheet.getLastRowNum(); i++) 
					  { 
						HSSFRow row = sheet.getRow(i);
					  	deleteRow(sheet,row);  
					  }
					 
					  out = new FileOutputStream(new File("RegistoChamadaTelefonica.xls"));
					  workbook.write(out); 
					
					int rowNum = 15;
					
					for (RegistoChamadaTelefonicaXLS xls : chamadaTelefonicaXLSs) { 
						
						HSSFRow row = sheet.createRow(rowNum++);
						
						HSSFCell createCellNome = row.createCell(1);
						createCellNome.setCellValue(xls.getNome());
						createCellNome.setCellStyle(cellStyle); 
						
						HSSFCell createCellNid = row.createCell(2);
						createCellNid.setCellValue(xls.getNid());
						createCellNid.setCellStyle(cellStyle);

						HSSFCell createCellIdade = row.createCell(3);
						createCellIdade.setCellValue(xls.getIdade());
						createCellIdade.setCellStyle(cellStyle);

						HSSFCell createCellContacto = row.createCell(4); 
						createCellContacto.setCellValue(xls.getContacto());
						createCellContacto.setCellStyle(cellStyle);

						HSSFCell createCellEndereco = row.createCell(5); 
						createCellEndereco.setCellValue(xls.getEndereco());
						createCellEndereco.setCellStyle(cellStyle);

						HSSFCell createCellTarv = row.createCell(6); 
						createCellTarv.setCellValue(xls.getTarv());
						createCellTarv.setCellStyle(cellStyle);

						HSSFCell createCellTb = row.createCell(7);
						createCellTb.setCellValue(xls.getTb());
						createCellTb.setCellStyle(cellStyle);
						
						HSSFCell createCellSmi = row.createCell(8);
						createCellSmi.setCellValue(xls.getSmi());
						createCellSmi.setCellStyle(cellStyle);
						
						HSSFCell apoio = row.createCell(9);
						apoio.setCellValue("");
						apoio.setCellStyle(cellStyle);

						HSSFCell reintegracao = row.createCell(10);
						reintegracao.setCellValue("");
						reintegracao.setCellStyle(cellStyle);

						HSSFCell incontactavel = row.createCell(11);
						incontactavel.setCellValue("");
						incontactavel.setCellStyle(cellStyle);

						HSSFCell esqueceuData = row.createCell(12);
						esqueceuData.setCellValue("");
						esqueceuData.setCellStyle(cellStyle);

						HSSFCell estaDoente = row.createCell(13);
						estaDoente.setCellValue("");
						estaDoente.setCellStyle(cellStyle);

						HSSFCell transporte = row.createCell(14);
						transporte.setCellValue("");
						transporte.setCellStyle(cellStyle);

						HSSFCell viagem = row.createCell(15);
						viagem.setCellValue("");
						viagem.setCellStyle(cellStyle);

						HSSFCell obito = row.createCell(16);
						obito.setCellValue("");
						obito.setCellStyle(cellStyle);

						HSSFCell retornou = row.createCell(17);
						retornou.setCellValue("");
						retornou.setCellStyle(cellStyle);

						HSSFCell visitado = row.createCell(18);
						visitado.setCellValue("");
						visitado.setCellStyle(cellStyle);

						HSSFCell observacao = row.createCell(19);
						observacao.setCellValue("");
						observacao.setCellStyle(cellStyle);

						HSSFCell responsavel = row.createCell(20);
						responsavel.setCellValue("");
						responsavel.setCellStyle(cellStyle);
					}
					
					for(int i = 1; i < RegistoChamadaTelefonicaXLS.class.getClass().getDeclaredFields().length; i++) { 
			            sheet.autoSizeColumn(i);
			        }
					
					currentXls.close();
					
					FileOutputStream outputStream = new FileOutputStream(new File("RegistoChamadaTelefonica.xls")); 
					workbook.write(outputStream);
					workbook.close();
					
					Desktop.getDesktop().open(new File("RegistoChamadaTelefonica.xls"));
					
				} else {
					MessageBox mNoPages = new MessageBox(parent,SWT.ICON_ERROR | SWT.OK);
					mNoPages.setText("O relatório não possui páginas");
					mNoPages.setMessage("O relatório que estás a gerar não contém nenhum dado. \\ n \\ n Verifique os valores de entrada que inseriu (como datas) para este relatório e tente novamente.");
					mNoPages.open();
				}
			} catch (Exception e) {
				e.printStackTrace();
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
