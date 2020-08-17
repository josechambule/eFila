package org.celllife.idart.gui.reportParameters;

import model.manager.reports.ARVDrugUsageReport;
import model.manager.reports.DispensaTrimestralSemestral;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.celllife.idart.database.hibernate.Drug;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.celllife.idart.misc.iDARTUtil.getBeginningOfDay;
import static org.celllife.idart.misc.iDARTUtil.getEndOfDay;

public class ARVDrugUsageExcel implements IRunnableWithProgress {

    private final Shell parent;
    private FileOutputStream out = null;
    private String reportFileName;
    private final String stockCenterName;
    private final List<Drug> drugList;
    private final Date endDate;
    private final Date startDate;
    private String[] totalString;



    public ARVDrugUsageExcel(Shell parent, String stockCenterName,
                             List<Drug> drugList, Date theStartDate, Date theEndDate,String reportFileName) {
        this.parent = parent;
        this.stockCenterName = stockCenterName;
        this.drugList = drugList;
        this.startDate = getBeginningOfDay(theStartDate);
        this.endDate = getEndOfDay(theEndDate);
        this.reportFileName=reportFileName;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            ConexaoJDBC con = new ConexaoJDBC();

            monitor.beginTask("Por Favor, aguarde ... ", 1);

            Map<String, Object> map = new HashMap<>();
            ARVDrugUsageReport report = new ARVDrugUsageReport(parent,
                    stockCenterName,
                    drugList,
                    startDate,endDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");


            if (drugList.size() > 0) {

                monitor.beginTask("Carregando a lista... ", drugList.size());
                FileInputStream currentXls = new FileInputStream(reportFileName);

                HSSFWorkbook workbook = new HSSFWorkbook(currentXls);

                HSSFSheet sheet = workbook.getSheetAt(0);

                HSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setWrapText(true);

                HSSFRow healthFacility = sheet.getRow(10);
                HSSFCell healthFacilityCell = healthFacility.createCell(2);
                healthFacilityCell.setCellValue(LocalObjects.currentClinic.getClinicName());
                healthFacilityCell.setCellStyle(cellStyle);

                HSSFRow reportPeriod = sheet.getRow(10);
                HSSFCell reportPeriodCell = reportPeriod.createCell(6);
                reportPeriodCell.setCellValue(dateFormat.format(startDate)+" à "+ dateFormat.format(endDate));
                reportPeriodCell.setCellStyle(cellStyle);

                HSSFRow reportYear = sheet.getRow(11);
                HSSFCell reportYearCell = reportYear.createCell(6);
                reportYearCell.setCellValue(sdfYear.format(startDate));
                reportYearCell.setCellStyle(cellStyle);

                for(int i=14; i<= sheet.getLastRowNum(); i++)
                {
                    HSSFRow row = sheet.getRow(i);
                    deleteRow(sheet,row);
                }

                out = new FileOutputStream(new File(reportFileName));
                workbook.write(out);

                int rowNum = 14;
                int rowNumValues = 15;

                int i = 1;
                int conta = 0;
                HSSFRow row = sheet.createRow(rowNum);
                //Create Report Header

                HSSFCell createCellData = row.createCell(i);
                createCellData.setCellValue("Data");
                createCellData.setCellStyle(cellStyle);

                for (Drug xls : drugList) {
                    i++;
                    HSSFCell createCellDrug = row.createCell(i);
                    createCellDrug.setCellValue(xls.getName());
                    createCellDrug.setCellStyle(cellStyle);
                }

                for (String[] valor :report.getARVDrugUsagePerDay()) {
                    int i0= 1;
                    HSSFRow rowValues = sheet.createRow(rowNumValues++);
                    for(String cell : valor) {
                        HSSFCell createCellDValues = rowValues.createCell(i0++);
                        createCellDValues.setCellValue(cell);
                        createCellDValues.setCellStyle(cellStyle);
                    }

                monitor.subTask("Carregando : " + conta++ + " de " + drugList.size() + "...");

                    Thread.sleep(5);

                    monitor.worked(1);
                    if (monitor.isCanceled()) {
                        monitor.done();
                        return;
                    }
                }
                HSSFRow rowValues = sheet.getRow(rowNumValues - 1);
                HSSFCell createCellDValues = rowValues.createCell(1);
                createCellDValues.setCellValue("Total");
                createCellDValues.setCellStyle(cellStyle);


                for(int g = 1; g < Drug.class.getClass().getDeclaredFields().length; g++) {
                    sheet.autoSizeColumn(g);
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

    public List<Drug> getList(){
        return this.drugList;
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
