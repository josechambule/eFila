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


import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.celllife.idart.gui.platform.GenericReportGui;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.celllife.idart.misc.iDARTUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.vafada.swtcalendar.SWTCalendar;
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;

import model.manager.reports.LivroRegistoDiarioXLS;

/**
 */
public class LivroRegistoDiario extends GenericReportGui {
	
	private Group grpDateRange;
	
	private Group grpTipoTarv;

	private SWTCalendar calendarStart;

	private SWTCalendar calendarEnd;

	private Button chkBtnInicio;

	private Button chkBtnManutencao;
	
	private Button chkBtnAlteraccao;
	
	private List<LivroRegistoDiarioXLS> livroRegistoDiarios;
	
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
	public LivroRegistoDiario(Shell parent, boolean activate) {
		super(parent, REPORTTYPE_MIA, activate);
		this.parent = parent;
	}

	/**
	 * This method initializes newMonthlyStockOverview
	 */
	@Override
	protected void createShell() {
		Rectangle bounds = new Rectangle(100, 50, 600, 510);
		buildShell(REPORT_LIVRO_ELETRONICO_ARV, bounds);
		// create the composites
		createMyGroups();
	}

	private void createMyGroups() {


		createGrpDateInfo();
	}

	/**
	 * This method initializes compHeader
	 *
	 */
	@Override
	protected void createCompHeader() {
		iDartImage icoImage = iDartImage.REPORT_STOCKCONTROLPERCLINIC;
		buildCompdHeader(REPORT_LIVRO_ELETRONICO_ARV, icoImage);
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

		if (iDARTUtil.before(calendarEnd.getCalendar().getTime(), calendarStart.getCalendar().getTime())){
			showMessage(MessageDialog.ERROR, "Data de término antes da data de início","Você selecionou uma data de término anterior à data de início.\nSelecione uma data de término após a data de início.");
			return;
		}
		
		if(chkBtnInicio.getSelection()==false && chkBtnManutencao.getSelection()==false && chkBtnAlteraccao.getSelection()==false)
		{
			showMessage(MessageDialog.ERROR, "Seleccionar Tipo Tarv","Seleccione pelo menos um tipo TARV.");
			return;
			
		} else {
			
			try {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
				 
				Date theStartDate = calendarStart.getCalendar().getTime(); 
			
				Date theEndDate=  calendarEnd.getCalendar().getTime();

				Calendar c = Calendar.getInstance(Locale.US);
				c.setLenient(true);
				c.setTime(theStartDate);

				if(Calendar.MONDAY == c.get(Calendar.DAY_OF_WEEK)){
					c.add(Calendar.DAY_OF_WEEK, -2);
					theStartDate = c.getTime();
				}

				model.manager.reports.LivroRegistoDiario report = 
						new model.manager.reports.LivroRegistoDiario(getShell(), theStartDate, theEndDate,chkBtnInicio.getSelection(),chkBtnManutencao.getSelection(),chkBtnAlteraccao.getSelection());
				viewReport(report);
			} catch (Exception e) {
				getLog().error("Exception while running Historico levantamento report",e);
			}
		}

	}
	
	@Override
	protected void cmdViewReportXlsWidgetSelected() {
				
		if (iDARTUtil.before(calendarEnd.getCalendar().getTime(), calendarStart.getCalendar().getTime())){
			showMessage(MessageDialog.ERROR, "Data de término antes da data de início","Você selecionou uma data de término anterior à data de início.\\nSelecione uma data de término após a data de início.");
			return;
		}
		
		if(chkBtnInicio.getSelection()==false && chkBtnManutencao.getSelection()==false && chkBtnAlteraccao.getSelection()==false)
		{
			showMessage(MessageDialog.ERROR, "Seleccionar Tipo Tarv","Seleccione pelo menos um tipo TARV.");
			return;
			
		} else {
			
			try {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
				
				SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

				 
				Date theStartDate = calendarStart.getCalendar().getTime(); 
			
				Date theEndDate=  calendarEnd.getCalendar().getTime(); 
				
				//theStartDate = sdf.parse(strTheDate);
				
				livroRegistoDiarios = new ArrayList<LivroRegistoDiarioXLS>(); 
				
				try {
					ConexaoJDBC con=new ConexaoJDBC();
					
					livroRegistoDiarios = con.getLivroRegistoDiarioXLS(chkBtnInicio.getSelection(), chkBtnManutencao.getSelection(), 
							chkBtnAlteraccao.getSelection(), sdf.format(theStartDate), sdf.format(theEndDate));
					
					if(livroRegistoDiarios.size() > 0) {
						
						FileInputStream currentXls = new FileInputStream("Reports/LivroRegistoDiarioARV.xls");
						
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
						HSSFCell reportPeriodCell = reportPeriod.createCell(16);
						reportPeriodCell.setCellValue(sdf.format(theStartDate) +" à "+ sdf.format(theEndDate));
						reportPeriodCell.setCellStyle(cellStyle); 

						HSSFRow reportYear = sheet.getRow(11);
						HSSFCell reportYearCell = reportYear.createCell(16);
						reportYearCell.setCellValue(sdfYear.format(theStartDate));
						reportYearCell.setCellStyle(cellStyle); 

						  for(int i=15; i<= sheet.getLastRowNum(); i++) 
						  { 
							HSSFRow row = sheet.getRow(i);
						  	deleteRow(sheet,row);  
						  }
						 
						  out = new FileOutputStream(new File("Reports/LivroRegistoDiarioARV.xls"));
						  workbook.write(out); 
						
						int rowNum = 15;
						
						for (LivroRegistoDiarioXLS xls : livroRegistoDiarios) { 
							
							HSSFRow row = sheet.createRow(rowNum++);
							
							HSSFCell createCellNid = row.createCell(1);
							createCellNid.setCellValue(xls.getPatientIdentifier());
							createCellNid.setCellStyle(cellStyle); 
							
							HSSFCell createCellNome = row.createCell(2);
							createCellNome.setCellValue(xls.getNome() + " " + xls.getApelido());
							createCellNome.setCellStyle(cellStyle);
							
							HSSFCell zeroQuatro = row.createCell(3);
							zeroQuatro.setCellValue(xls.getZeroQuatro());
							zeroQuatro.setCellStyle(cellStyle);
							
							HSSFCell cincoNove = row.createCell(4);
							cincoNove.setCellValue(xls.getCincoNove());
							cincoNove.setCellStyle(cellStyle);
							
							HSSFCell dezCatorze = row.createCell(5);
							dezCatorze.setCellValue(xls.getDezCatorze());
							dezCatorze.setCellStyle(cellStyle);

							HSSFCell maiorQuinze = row.createCell(6);
							maiorQuinze.setCellValue(xls.getMaiorQuinze());
							maiorQuinze.setCellStyle(cellStyle);
							
							HSSFCell createCellTipoTarv = row.createCell(7);
							createCellTipoTarv.setCellValue(xls.getTipoTarv());
							createCellTipoTarv.setCellStyle(cellStyle);
	
							HSSFCell createCellRegimeTerapeutico = row.createCell(8); 
							createCellRegimeTerapeutico.setCellValue(xls.getRegimeTerapeutico());
							createCellRegimeTerapeutico.setCellStyle(cellStyle);
							
							HSSFCell produtos = row.createCell(9); 
							produtos.setCellValue(xls.getProdutos());
							produtos.setCellStyle(cellStyle);
							
							HSSFCell quantidade = row.createCell(10); 
							quantidade.setCellValue(xls.getQuantidade());
							quantidade.setCellStyle(cellStyle);
	
							HSSFCell createCellTipoDispensa = row.createCell(11); 
							createCellTipoDispensa.setCellValue(xls.getTipoDispensa());
							createCellTipoDispensa.setCellStyle(cellStyle);
							
							HSSFCell linhaNome = row.createCell(12); 
							linhaNome.setCellValue(xls.getLinha());
							linhaNome.setCellStyle(cellStyle);
							
							HSSFCell createCellDataLevantamento = row.createCell(13); 
							createCellDataLevantamento.setCellValue(xls.getDataLevantamento());
							createCellDataLevantamento.setCellStyle(cellStyle);
	
							HSSFCell createCellDataProximoLevantamento = row.createCell(14);
							createCellDataProximoLevantamento.setCellValue(xls.getDataProximoLevantamento());
							createCellDataProximoLevantamento.setCellStyle(cellStyle);
							
							HSSFCell ppe = row.createCell(15);
							ppe.setCellValue(xls.getPpe());
							ppe.setCellStyle(cellStyle);
							
							HSSFCell prep = row.createCell(16);
							prep.setCellValue(xls.getPrep());
							prep.setCellStyle(cellStyle);
							
							HSSFCell criancaExposta = row.createCell(17);
							criancaExposta.setCellValue("");
							criancaExposta.setCellStyle(cellStyle);
						}
						
						for(int i = 1; i < LivroRegistoDiarioXLS.class.getClass().getDeclaredFields().length; i++) { 
				            sheet.autoSizeColumn(i);
				        }
						
						currentXls.close();
						
						FileOutputStream outputStream = new FileOutputStream(new File("Reports/LivroRegistoDiarioARV.xls")); 
						workbook.write(outputStream);
						workbook.close();
						
						Desktop.getDesktop().open(new File("Reports/LivroRegistoDiarioARV.xls"));
						
					} else {
						MessageBox mNoPages = new MessageBox(parent,SWT.ICON_ERROR | SWT.OK);
						mNoPages.setText("O relatório não possui páginas");
						mNoPages.setMessage("O relatório que estás a gerar não contém nenhum dado. \\ n \\ n Verifique os valores de entrada que inseriu (como datas) para este relatório e tente novamente.");
						mNoPages.open();
					}
										
				} catch (SQLException | ClassNotFoundException e) { 
					e.printStackTrace();
				}
				
			} catch (Exception e) {
				getLog().error("Erro ao executar o relatorio Registo Diario de ARVs",e);
			}
		}
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



	@Override
	protected void setLogger() {
		setLog(Logger.getLogger(this.getClass()));
	}

	
	private void createGrpDateRange() {
		
		//Group tipo tarv
		grpTipoTarv = new Group(getShell(), SWT.NONE);
		grpTipoTarv.setText("Tipo Tarv:");
		grpTipoTarv.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grpTipoTarv.setBounds(new Rectangle(55, 90, 520, 50));
		grpTipoTarv.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		
		//chk button Inicio
		chkBtnInicio= new Button(grpTipoTarv, SWT.CHECK);
		chkBtnInicio.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 1,1));
		chkBtnInicio.setBounds(new Rectangle(50, 20, 100, 20));
		chkBtnInicio.setText("Início");
		chkBtnInicio.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		chkBtnInicio.setSelection(false);
		
		//chk button  Manter
		chkBtnManutencao= new Button(grpTipoTarv, SWT.CHECK);
		chkBtnManutencao.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 1,1));
		chkBtnManutencao.setBounds(new Rectangle(350, 20, 100, 20));
		chkBtnManutencao.setText("Manutenção");
		chkBtnManutencao.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		chkBtnManutencao.setSelection(false);
		
		//chk button Alterar
		chkBtnAlteraccao= new Button(grpTipoTarv, SWT.CHECK);
		chkBtnAlteraccao.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false, 1,1));
		chkBtnAlteraccao.setBounds(new Rectangle(200, 20, 100, 20));
		chkBtnAlteraccao.setText("Alteração");
		chkBtnAlteraccao.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		chkBtnAlteraccao.setSelection(false);
		
		grpDateRange = new Group(getShell(), SWT.NONE);
		grpDateRange.setText("Período:");
		grpDateRange.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grpDateRange.setBounds(new Rectangle(55, 160, 520, 201));
		grpDateRange.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		Label lblStartDate = new Label(grpDateRange, SWT.CENTER | SWT.BORDER);
		lblStartDate.setBounds(new org.eclipse.swt.graphics.Rectangle(40, 30,
				180, 20));
		lblStartDate.setText("Data Início:");
		lblStartDate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		Label lblEndDate = new Label(grpDateRange, SWT.CENTER | SWT.BORDER);
		lblEndDate.setBounds(new org.eclipse.swt.graphics.Rectangle(300, 30,
				180, 20));
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
}
