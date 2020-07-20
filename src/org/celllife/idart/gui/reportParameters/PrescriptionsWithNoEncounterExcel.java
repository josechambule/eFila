package org.celllife.idart.gui.reportParameters;

import model.manager.reports.LivroRegistoDiarioXLS;
import model.manager.reports.PrescricaoSemFilaXLS;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.vafada.swtcalendar.SWTCalendar;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrescriptionsWithNoEncounterExcel implements IRunnableWithProgress {

    private List<PrescricaoSemFilaXLS> prescricaoSemFilaXLSs;
    private final Shell parent;
    private FileOutputStream out = null;
    private SWTCalendar swtCal;
    private String reportFileName;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SWTCalendar calendarStart;
    private SWTCalendar calendarEnd;

    SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    public PrescriptionsWithNoEncounterExcel(Shell parent, String reportFileName, SWTCalendar calendarStart, SWTCalendar calendarEnd) {
        this.parent = parent;
        this.reportFileName = reportFileName;
        this.calendarStart = calendarStart;
        this.calendarEnd = calendarEnd;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {

            ConexaoJDBC con=new ConexaoJDBC();

            monitor.beginTask("Por Favor, aguarde ... ", 1);

            prescricaoSemFilaXLSs = con.getQueryPrescricoeSemDispensasXLS(sdf.format(calendarStart.getCalendar().getTime()), sdf.format(calendarEnd.getCalendar().getTime()));

            if(prescricaoSemFilaXLSs.size() > 0) {
                // Tell the user what you are doing
                monitor.beginTask("Carregando a lista... ", prescricaoSemFilaXLSs.size());

                FileInputStream currentXls = new FileInputStream(reportFileName);

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
                HSSFCell reportPeriodCell = reportPeriod.createCell(4);
                reportPeriodCell.setCellValue(sdf.format(calendarStart.getCalendar().getTime()) +" à "+ sdf.format(calendarEnd.getCalendar().getTime()));
                reportPeriodCell.setCellStyle(cellStyle);

                HSSFRow reportYear = sheet.getRow(11);
                HSSFCell reportYearCell = reportYear.createCell(4);
                reportYearCell.setCellValue(sdfYear.format(calendarStart.getCalendar().getTime()));
                reportYearCell.setCellStyle(cellStyle);

                for(int i=14; i<= sheet.getLastRowNum(); i++)
                {
                    HSSFRow row = sheet.getRow(i);
                    deleteRow(sheet,row);
                }

                //extracted(sheet);

                out = new FileOutputStream(new File(reportFileName));
                workbook.write(out);

                int rowNum = 14;
                int i = 0;
                for (PrescricaoSemFilaXLS xls : prescricaoSemFilaXLSs) {
                    i++;
                    HSSFRow row = sheet.createRow(rowNum++);

                    /*
                     * sheet.addMergedRegion(CellRangeAddress.valueOf("C" + (rowNum) + ":D" +
                     * (rowNum))); sheet.addMergedRegion(CellRangeAddress.valueOf("E" + (rowNum) +
                     * ":F" + (rowNum))); sheet.addMergedRegion(CellRangeAddress.valueOf("G" +
                     * (rowNum) + ":H" + (rowNum)));
                     */

                    HSSFCell createCellNid = row.createCell(1);
                    createCellNid.setCellValue(xls.getPatientIdentifier());
                    createCellNid.setCellStyle(cellStyle);

                    HSSFCell createCellNome = row.createCell(2);
                    createCellNome.setCellValue(xls.getNome() + " " + xls.getApelido());
                    createCellNome.setCellStyle(cellStyle);

                    HSSFCell createCellUuid = row.createCell(3);
                    createCellUuid.setCellValue(xls.getUuidOpenmrs());
                    createCellUuid.setCellStyle(cellStyle);

                    HSSFCell dataPrescricao = row.createCell(4);
                    dataPrescricao.setCellValue(xls.getDataPrescricao());
                    dataPrescricao.setCellStyle(cellStyle);

                    // Optionally add subtasks
                    monitor.subTask("Carregando : " + i + " de " + prescricaoSemFilaXLSs.size() + "...");

                    Thread.sleep(5);

                    // Tell the monitor that you successfully finished one item of "workload"-many
                    monitor.worked(1);
                    // Check if the user pressed "cancel"
                    if (monitor.isCanceled()) {
                        monitor.done();
                        return;
                    }
                }

                for (int i0 = 1; i0 < PrescricaoSemFilaXLS.class.getClass().getDeclaredFields().length; i0++) {
                    sheet.autoSizeColumn(i0);
                }

                monitor.done();
                currentXls.close();

                FileOutputStream outputStream = new FileOutputStream(new File(reportFileName));
                workbook.write(outputStream);
                workbook.close();

                Desktop.getDesktop().open(new File(reportFileName));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<PrescricaoSemFilaXLS> getList(){
        return this.prescricaoSemFilaXLSs;
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
