package org.celllife.idart.gui.reportParameters;

import model.manager.AdministrationManager;
import model.manager.StockManager;
import model.manager.reports.BalanceteDiarioXLS;
import model.manager.reports.HistoricoLevantamentoXLS;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.celllife.idart.database.hibernate.Clinic;
import org.celllife.idart.database.hibernate.Drug;
import org.celllife.idart.database.hibernate.Stock;
import org.celllife.idart.database.hibernate.StockCenter;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.hibernate.Session;
import org.vafada.swtcalendar.SWTCalendar;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BalanceteDiarioExcel implements IRunnableWithProgress {

    private List<BalanceteDiarioXLS> listBalanceteDiarioXLS;
    private final Shell parent;
    private FileOutputStream out = null;
    private SWTCalendar swtCal;
    private String reportFileName;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date theStartDate;
    private Date theEndDate;
    private Clinic clinic;
    private Drug drug;

    SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    public BalanceteDiarioExcel(Shell parent, String reportFileName, Clinic clinic, Date theStartDate, Date theEndDate, Drug drug) {
        this.listBalanceteDiarioXLS = listBalanceteDiarioXLS;
        this.parent = parent;
        this.swtCal = swtCal;
        this.reportFileName = reportFileName;
        this.theStartDate = theStartDate;
        this.theEndDate = theEndDate;
        this.clinic = clinic;
        this.drug = drug;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            Session session = HibernateUtil.getNewSession();
            ConexaoJDBC con=new ConexaoJDBC();

            StockCenter stockCenter = AdministrationManager.getStockCenter(session,clinic.getClinicName());

           List<Stock> stocks = StockManager.getCurrentStockForDrug(session,drug,stockCenter);

            monitor.beginTask("Por Favor, aguarde ... ", 1);

            listBalanceteDiarioXLS = con.getBalanceteDiarioXLS(sdf.format(theStartDate), sdf.format(theEndDate),drug,stockCenter);

            if(listBalanceteDiarioXLS.size() > 0) {
                // Tell the user what you are doing
                monitor.beginTask("Carregando a lista... ", listBalanceteDiarioXLS.size());

                FileInputStream currentXls = new FileInputStream(reportFileName);

                HSSFWorkbook workbook = new HSSFWorkbook(currentXls);

                HSSFSheet sheet = workbook.getSheetAt(0);

                HSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);

                HSSFRow healthFacility = sheet.getRow(1);
                HSSFCell healthFacilityCell = healthFacility.createCell(2);
                healthFacilityCell.setCellValue(LocalObjects.currentClinic.getClinicName());
                healthFacilityCell.setCellStyle(cellStyle);

                HSSFRow reportPeriod = sheet.getRow(2);
                HSSFCell reportPeriodCell = reportPeriod.createCell(5);
                reportPeriodCell.setCellValue(sdf.format(theStartDate) + " à " + sdf.format(theEndDate));
                reportPeriodCell.setCellStyle(cellStyle);

                HSSFRow reportCodigoFNM = sheet.getRow(4);
                HSSFCell CodigoFNMCell = reportCodigoFNM.createCell(1);
                CodigoFNMCell.setCellValue(drug.getAtccode());
                CodigoFNMCell.setCellStyle(cellStyle);

                HSSFRow reportMedicamento = sheet.getRow(4);
                HSSFCell reportMedicamentoCell = reportMedicamento.createCell(2);
                reportMedicamentoCell.setCellValue(drug.getName());
                reportMedicamentoCell.setCellStyle(cellStyle);

                HSSFRow reportComprimidosNoPacote = sheet.getRow(4);
                HSSFCell reportComprimidosNoPacoteCell = reportComprimidosNoPacote.createCell(4);
                reportComprimidosNoPacoteCell.setCellValue(drug.getPackSize());
                reportComprimidosNoPacoteCell.setCellStyle(cellStyle);

                HSSFRow reportprazo = sheet.getRow(4);
                HSSFCell reportprazoCell = reportprazo.createCell(5);
                reportprazoCell.setCellValue(stocks.get(0).getExpiryDate().getMonth()+"/"+stocks.get(0).getExpiryDate().getYear());
                reportprazoCell.setCellStyle(cellStyle);


                for (int i = 6; i <= sheet.getLastRowNum(); i++) {
                    HSSFRow row = sheet.getRow(i);
                    deleteRow(sheet, row);
                }

                out = new FileOutputStream(new File(reportFileName));
                workbook.write(out);

                int rowNum = 6;
                int i = 0;
                for (BalanceteDiarioXLS xls : listBalanceteDiarioXLS) {
                    i++;
                    HSSFRow row = sheet.createRow(rowNum++);

                    HSSFCell createCellDataMovimento = row.createCell(1);
                    createCellDataMovimento.setCellValue(xls.getDataMovimento());
                    createCellDataMovimento.setCellStyle(cellStyle);

                    HSSFCell createCellEntradas = row.createCell(2);
                    createCellEntradas.setCellValue(xls.getEntrance());
                    createCellEntradas.setCellStyle(cellStyle);

                    HSSFCell createCellAjustesPerdas = row.createCell(3);
                    createCellAjustesPerdas.setCellValue(xls.getLostAjust());
                    createCellAjustesPerdas.setCellStyle(cellStyle);

                    HSSFCell createCellDispensados = row.createCell(4);
                    createCellDispensados.setCellValue(xls.getOutgoing());
                    createCellDispensados.setCellStyle(cellStyle);

                    HSSFCell createCellStrock = row.createCell(5);
                    createCellStrock.setCellValue(xls.getStock());
                    createCellStrock.setCellStyle(cellStyle);

                    HSSFCell createCellNotas = row.createCell(6);
                    createCellNotas.setCellValue(xls.getNotes());
                    createCellNotas.setCellStyle(cellStyle);

                    // Optionally add subtasks
                    monitor.subTask("Carregando : " + i + " de " + listBalanceteDiarioXLS.size() + "...");

                    Thread.sleep(5);

                    // Tell the monitor that you successfully finished one item of "workload"-many
                    monitor.worked(1);
                    // Check if the user pressed "cancel"
                    if (monitor.isCanceled()) {
                        monitor.done();
                        return;
                    }
                }

                for (int i0 = 1; i0 < HistoricoLevantamentoXLS.class.getClass().getDeclaredFields().length; i0++) {
                    sheet.autoSizeColumn(i0);
                }

                monitor.done();
                currentXls.close();

                FileOutputStream outputStream = new FileOutputStream(new File(reportFileName));
                workbook.write(outputStream);
                workbook.close();

                Desktop.getDesktop().open(new File(reportFileName));
                session.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public List<BalanceteDiarioXLS> getList(){
        return this.listBalanceteDiarioXLS;
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
