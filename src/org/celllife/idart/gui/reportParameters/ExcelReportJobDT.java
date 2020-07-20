package org.celllife.idart.gui.reportParameters;

import model.manager.excel.conversion.exceptions.ReportException;
import model.manager.exports.excel.ExcelExporterDT;
import model.manager.exports.excel.ExcelReportObjectDT;
import org.celllife.idart.misc.AbstractCancellableJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.program.Program;

/**
 * This class is used to run the data export and show a progress dialog.
 */
public class ExcelReportJobDT extends AbstractCancellableJob {

	private final ExcelReportObjectDT reportObject;
	private final ExcelExporterDT exporter;

	/**
	 * @param deo
	 *            DataExportObject to export.
	 * @param exporter 
	 * @param fileName
	 *            File to write data to.
	 */
	public ExcelReportJobDT(ExcelReportObjectDT deo, ExcelExporterDT exporter) {
		this.reportObject = deo;
		this.exporter = exporter;
	}

	@Override
	public void performJob(final IProgressMonitor monitor)
			throws ReportException {
		exporter.setMonitor(monitor);
		exporter.generate(reportObject);
		Program.launch(reportObject.getPath());
	}
}
