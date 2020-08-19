package model.manager.exports;

import model.manager.exports.columns.DrugDetailsEnumDT;
import model.manager.exports.columns.DrugsDispensedEnumDT;
import model.manager.exports.columns.PackageDetailsEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * This object will be used to represent the columns in the drug dispensed
 * report
 * 
 */
public class DrugDispensedObjectDT extends AbstractExportObjectDT implements Cloneable{

	private int drugid = 0;
	
	private DrugsDispensedEnumDT columnEnum;

	private Map<String, Integer> batchTotalsMap;

	private int packSize;

	/**
	 * 
	 */
	public DrugDispensedObjectDT() {
		super();
	}

	/**
	 * @param title
	 * @param displayName
	 * @param dataType
	 */
	public DrugDispensedObjectDT(String title, Class dataType) {
		super(title, dataType);
	}

	public DrugDispensedObjectDT(DrugsDispensedEnumDT enu) {
		super(enu.getTitle(), enu.getDataType());
		setColumnIndex(-1);
		setColumnWidth(enu.getColumnWidth());
		this.columnEnum = enu;
		drugid = 0;
	}

	/**
	 * @return the id
	 */
	public int getDrugId() {
		return drugid;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.drugid = id;
	}

	public Map<String, Integer> getBatchTotalsMap(){
		return batchTotalsMap;
	}
	
	public int getTotalUnitsDispensed(){
		Integer total = 0;
		if (batchTotalsMap != null) {
			for (Integer batchtotal : batchTotalsMap.values()) {
				total += batchtotal;
			}
		}
		return total;
	}

	@Override
	public Object getData(DataExportFunctions functions, int index) {
		if (columnEnum != null){
			switch (columnEnum) {
			case dateDispensed:
				return functions.getPackageDetailForCurrentPackage(PackageDetailsEnum.DATE_DISPENSED.toString());
			case clinic:
				return functions.getPatientField("Patient", "clinic.clinicName");
			case patientFirstName:
				return functions.getPatientField("Patient", "firstNames");
			case patientLastName:
				return functions.getPatientField("Patient", "lastname");
			case patientId:
				return functions.getPatientField("Patient", "patientId");
			case sex:
				return functions.getPatientField("Patient", "sex");
			case dateOfBirth:
				return functions.getPatientField("Patient", "dateOfBirth");
			case age:
				return functions.getPaitentAgeAtEndDate();
//			case regimen:
//				return functions.getPackageDetailForCurrentPackage(PackageDetailsEnum.REGIMEN.name());
			default:
				return null;
			}
		} else {
			if (drugid > 0){
				if (index == 0) {
					String quantity = (String) functions.getExportDrugDetailCurrentPackage(DrugDetailsEnumDT.QUANTITYDISPENSED.name(),drugid);
					String batch = (String) functions.getExportDrugDetailCurrentPackage(DrugDetailsEnumDT.BATCHNUMBER.name(),drugid);
					updateDrugTotals(quantity, batch);
					return quantity;
				}
				if (index == 1) {
					return functions.getExportDrugDetailCurrentPackage(DrugDetailsEnumDT.BATCHNUMBER.name(),drugid);
				}
			}
		}
		return null;
	}

	private void updateDrugTotals(String quantity, String batch) {
		if (batch.isEmpty() || quantity.isEmpty())
			return;
		
		if (batchTotalsMap == null)
			batchTotalsMap = new HashMap<String, Integer>();

		String[] batches = batch.split(",");
		Integer[] quantities;
		String[] qtyStrings = quantity.split(",");
		quantities = new Integer[qtyStrings.length];
		for (int i = 0; i < qtyStrings.length; i++) {
			quantities[i] = Integer.parseInt(qtyStrings[i]);
		}
		
		for (int i = 0; i < batches.length; i++) {
			if (batchTotalsMap.containsKey(batches[i])){
				batchTotalsMap.put(batches[i], batchTotalsMap.get(batches[i])+quantities[i]);
			}else {
				batchTotalsMap.put(batches[i],quantities[i]);
			}
		}
	}

	public void setDrugPackSize(int packSize) {
		this.packSize = packSize;
	}
	
	public int getDrugPackSize() {
		return packSize;
	}
	
}
