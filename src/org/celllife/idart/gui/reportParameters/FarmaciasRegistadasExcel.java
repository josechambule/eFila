package org.celllife.idart.gui.reportParameters;

import model.manager.reports.FarmaciasRegistadasXLS;
import model.manager.reports.HistoricoLevantamentoXLS;
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

public class FarmaciasRegistadasExcel implements IRunnableWithProgress {

    private List<FarmaciasRegistadasXLS> farmaciasRegistadasXLSList;
    private final Shell parent;
    private FileOutputStream out = null;
    private String reportFileName;

    public FarmaciasRegistadasExcel(Shell parent, String reportFileName) {
        this.farmaciasRegistadasXLSList = farmaciasRegistadasXLSList;
        this.parent = parent;
        this.reportFileName = reportFileName;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {

            ConexaoJDBC con=new ConexaoJDBC();

            monitor.beginTask("Por Favor, aguarde ... ", 1);

            farmaciasRegistadasXLSList = con.getFarmaciasRegistadasXLS();

            if(farmaciasRegistadasXLSList.size() > 0) {
                // Tell the user what you are doing
                monitor.beginTask("Carregando a lista... ", farmaciasRegistadasXLSList.size());

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

                for (int i = 13; i <= sheet.getLastRowNum(); i++) {
                    HSSFRow row = sheet.getRow(i);
                    deleteRow(sheet, row);
                }

                out = new FileOutputStream(new File(reportFileName));
                workbook.write(out);

                int rowNum = 13;
                int i = 0;
                for (FarmaciasRegistadasXLS xls : farmaciasRegistadasXLSList) {
                    i++;
                    HSSFRow row = sheet.createRow(rowNum++);

                    HSSFCell createCellCode = row.createCell(1);
                    createCellCode.setCellValue(xls.getCode());
                    createCellCode.setCellStyle(cellStyle);

                    HSSFCell createCellNome = row.createCell(2);
                    createCellNome.setCellValue(xls.getClinicName());
                    createCellNome.setCellStyle(cellStyle);

                    HSSFCell createCellTipoFarmacia = row.createCell(3);
                    createCellTipoFarmacia.setCellValue(xls.getFacilityType());
                    createCellTipoFarmacia.setCellStyle(cellStyle);

                    HSSFCell createCellProvincia = row.createCell(4);
                    createCellProvincia.setCellValue(xls.getProvince());
                    createCellProvincia.setCellStyle(cellStyle);

                    HSSFCell createCellDistrito = row.createCell(5);
                    createCellDistrito.setCellValue(xls.getDistrict());
                    createCellDistrito.setCellStyle(cellStyle);

                    HSSFCell createCellContacto = row.createCell(6);
                    createCellContacto.setCellValue(xls.getContact());
                    createCellContacto.setCellStyle(cellStyle);

                    HSSFCell createCellFiliacao= row.createCell(7);
                    createCellFiliacao.setCellValue(xls.getDependence());
                    createCellFiliacao.setCellStyle(cellStyle);

                    HSSFCell createCellUiid = row.createCell(8);
                    createCellUiid.setCellValue(xls.getUuid());
                    createCellUiid.setCellStyle(cellStyle);

                    // Optionally add subtasks
                    monitor.subTask("Carregando : " + i + " de " + farmaciasRegistadasXLSList.size() + "...");

                    Thread.sleep(5);

                    // Tell the monitor that you successfully finished one item of "workload"-many
                    monitor.worked(1);
                    // Check if the user pressed "cancel"
                    if (monitor.isCanceled()) {
                        monitor.done();
                        return;
                    }
                }

                for (int i0 = 1; i0 < FarmaciasRegistadasXLS.class.getClass().getDeclaredFields().length; i0++) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public List<FarmaciasRegistadasXLS> getList(){
        return this.farmaciasRegistadasXLSList;
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
