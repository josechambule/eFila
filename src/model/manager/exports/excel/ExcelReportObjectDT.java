package model.manager.exports.excel;

import java.util.Date;
import java.util.List;

import model.manager.exports.PackageExportObjectDT;

public class ExcelReportObjectDT {
	private Date startDate;
	
	private Date endDate;
	
	private boolean showBatchInfo;
	
	private String pharmacy;
	
	private List<PackageExportObjectDT> columns;
	
	private String path;

	private List<PackageExportObjectDT> endColumns;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setColumns(List<PackageExportObjectDT> columns) {
		this.columns = columns;
	}

	public List<PackageExportObjectDT> getColumns() {
		return columns;
	}

	/**
	 * @return the showBatchInfo
	 */
	public boolean isShowBatchInfo() {
		return showBatchInfo;
	}

	/**
	 * @param showBatchInfo
	 *            the showBatchInfo to set
	 */
	public void setShowBatchInfo(boolean showBatchInfo) {
		this.showBatchInfo = showBatchInfo;
	}

	/**
	 * @return the pharmacy
	 */
	public String getPharmacy() {
		return pharmacy;
	}

	/**
	 * @param pharmacy the pharmacy to set
	 */
	public void setPharmacy(String pharmacy) {
		this.pharmacy = pharmacy;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public List<PackageExportObjectDT> getEndColumns() {
		return endColumns;
	}
	
	public void setEndColumns( List<PackageExportObjectDT> endColumns){
		this.endColumns = endColumns;
	}

}
