package model.manager.exports.columns;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public enum DrugsDispensedEnumDT {
	
	dateDispensed("Data de Levantamento", Date.class, 17 ),
	
	clinic("Unidade Sanitaria", String.class, 15),
	
	patientFirstName("Nome do Paciente", String.class, 15),
	
	patientLastName("Apelido", String.class, 15),
	
	patientId("NID", String.class, 15),
	
	sex("Genero", Character.class, 6),
	
	dateOfBirth("Data de Nasciemnto", Date.class, 17),
	
	age("Idade em {0,date,medium}", Integer.class, 8), 
	
	regimen("Regime Terapeutico", String.class, 10); 
	
	private String title;

	private Class dataType;
	
	private int columnWidth;
	
	private static Date endDate = new Date();

	/**
	 * @param title
	 * @param displayName
	 * @param dataType
	 */
	private DrugsDispensedEnumDT(String title, Class dataType, int columnWidth) {
		this.title = title;
		this.dataType = dataType;
		this.columnWidth = columnWidth;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}
	
	public static DrugsDispensedEnumDT[] getDefaults(){
		return new DrugsDispensedEnumDT[] { dateDispensed, clinic,
				patientFirstName, patientLastName, patientId, sex };
	}
	
	public static List<DrugsDispensedEnumDT> getCompulsory() {
		return Arrays.asList(new DrugsDispensedEnumDT[] { dateDispensed, clinic, patientId });
	}

	public String getTitle() {
		return MessageFormat.format(title, endDate);
	}
	
	public int getColumnWidth() {
		return columnWidth;
	}
	
	public Class getDataType() {
		return dataType;
	}
	
	public static void setEndDate(Date endDate) {
		DrugsDispensedEnumDT.endDate = endDate;
	}
}
