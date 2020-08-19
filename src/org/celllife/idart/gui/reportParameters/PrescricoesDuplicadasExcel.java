package org.celllife.idart.gui.reportParameters;

import model.manager.OpenmrsErrorLogManager;
import model.manager.reports.PrescricoesDuplicadasXLS;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.dao.ConexaoJDBC;
import org.celllife.idart.database.hibernate.OpenmrsErrorLog;
import org.celllife.idart.database.hibernate.StockCenter;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.hibernate.Session;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrescricoesDuplicadasExcel implements IRunnableWithProgress {

    private List<PrescricoesDuplicadasXLS> prescricoesDuplicadasXLSList;
    private final Shell parent;
    private FileOutputStream out = null;
    private String reportFileName;
    private Date theStartDate;
    private Date theEndDate;
    private StockCenter clinic;

    SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public PrescricoesDuplicadasExcel(Shell parent, String reportFileName, StockCenter clinic, Date theStartDate, Date theEndDate) {
        this.prescricoesDuplicadasXLSList = prescricoesDuplicadasXLSList;
        this.parent = parent;
        this.reportFileName = reportFileName;
        this.theStartDate = theStartDate;
        this.theEndDate = theEndDate;
        this.clinic = clinic;
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            Session session = HibernateUtil.getNewSession();
            ConexaoJDBC con=new ConexaoJDBC();

            monitor.beginTask("Por Favor, aguarde ... ", 1);

            prescricoesDuplicadasXLSList = con.getPrescricoesDuplicadas(sdf.format(theStartDate), sdf.format(theEndDate),clinic);

            if(prescricoesDuplicadasXLSList.size() > 0) {
                // Tell the user what you are doing
                monitor.beginTask("Carregando a lista... ", prescricoesDuplicadasXLSList.size());

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

                HSSFRow reportPeriod = sheet.getRow(11);
                HSSFCell reportPeriodCell = reportPeriod.createCell(4);
                reportPeriodCell.setCellValue(sdf.format(theStartDate) + " à " + sdf.format(theEndDate));
                reportPeriodCell.setCellStyle(cellStyle);

                for (int i = 14; i <= sheet.getLastRowNum(); i++) {
                    HSSFRow row = sheet.getRow(i);
                    deleteRow(sheet, row);
                }

                out = new FileOutputStream(new File(reportFileName));
                workbook.write(out);

                int rowNum = 14;
                int i = 0;
                for (PrescricoesDuplicadasXLS xls : prescricoesDuplicadasXLSList) {
                    i++;
                    HSSFRow row = sheet.createRow(rowNum++);

                    HSSFCell createCellNid = row.createCell(1);
                    createCellNid.setCellValue(xls.getNid());
                    createCellNid.setCellStyle(cellStyle);

                    HSSFCell createCellNome = row.createCell(2);
                    createCellNome.setCellValue(xls.getNome());
                    createCellNome.setCellStyle(cellStyle);

                    HSSFCell createCelldataPrescricao = row.createCell(3);
                    createCelldataPrescricao.setCellValue(xls.getDataPrescricao());
                    createCelldataPrescricao.setCellStyle(cellStyle);

                    HSSFCell createCellTipoPaciente  = row.createCell(4);
                    createCellTipoPaciente.setCellValue(xls.getTipoPaciente());
                    createCellTipoPaciente.setCellStyle(cellStyle);

                    HSSFCell createCellRegimeTerapeutico  = row.createCell(5);
                    createCellRegimeTerapeutico.setCellValue(xls.getRegimeTerapeutico());
                    createCellRegimeTerapeutico.setCellStyle(cellStyle);


                    HSSFCell createCellLinha = row.createCell(6);
                    createCellLinha.setCellValue(xls.getLinha());
                    createCellLinha.setCellStyle(cellStyle);


                    HSSFCell createCelltipoDispensa = row.createCell(7);
                    createCelltipoDispensa.setCellValue(xls.getTipoDispensa());
                    createCelltipoDispensa.setCellStyle(cellStyle);


                    HSSFCell createCellNotas = row.createCell(8);
                    createCellNotas.setCellValue(xls.getNotas());
                    createCellNotas.setCellStyle(cellStyle);

                    // Optionally add subtasks
                    monitor.subTask("Carregando : " + i + " de " + prescricoesDuplicadasXLSList.size() + "...");

                    Thread.sleep(5);

                    // Tell the monitor that you successfully finished one item of "workload"-many
                    monitor.worked(1);
                    // Check if the user pressed "cancel"
                    if (monitor.isCanceled()) {
                        monitor.done();
                        return;
                    }
                }

                for (int i0 = 1; i0 < PrescricoesDuplicadasXLS.class.getClass().getDeclaredFields().length; i0++) {
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

    public List<PrescricoesDuplicadasXLS> getList(){
        return this.prescricoesDuplicadasXLSList;
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
