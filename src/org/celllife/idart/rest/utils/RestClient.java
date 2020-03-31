package org.celllife.idart.rest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.StringEntity;
import org.celllife.idart.commonobjects.iDartProperties;
import org.celllife.idart.database.hibernate.PackagedDrugs;
import org.celllife.idart.database.hibernate.PrescribedDrugs;
import org.celllife.idart.rest.ApiAuthRest;

import model.nonPersistent.Autenticacao;


/**
 * 
 * @author helio.machabane
 *
 */
public class RestClient {
	
	Properties prop = new Properties();
	//InputStream input = null;
	
	File myFile = new File("src/jdbc_auto_generated.properties");
	File input = new File("jdbc.properties");
    Properties prop_dynamic = new Properties();
  
    
	//SET VALUE FOR CONNECT TO OPENMRS
	public RestClient() {
		
		try {
			prop_dynamic.load(new FileInputStream(myFile));
			prop.load(new FileInputStream(input));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		ApiAuthRest.setURLBase(prop.getProperty("urlBase"));
		ApiAuthRest.setUsername(prop_dynamic.getProperty("userName"));
		//ApiAuthRest.setPassword(prop_dynamic.getProperty("password"));
		ApiAuthRest.setPassword(Autenticacao.senhaTemporaria);
	}
	
	public boolean postOpenMRSEncounter(String encounterDatetime, String nidUuid, String encounterType, String strFacilityUuid, 
			String filaUuid, String providerUuid, String regimeUuid, 
			String strRegimenAnswerUuid, String dispensedAmountUuid, List<PrescribedDrugs> prescribedDrugs, 
			List<PackagedDrugs> packagedDrugs, String dosageUuid, String returnVisitUuid, String strNextPickUp) throws Exception {
		
		StringEntity inputAddPerson = null;
		
		String packSize = null;
		
		String dosage;
		
		String dosage_1;
		
		String customizedDosage = null;
		
		if (prescribedDrugs.size() == 1) {
			
			//Dispensed amount
			packSize = String.valueOf(packagedDrugs.get(0).getAmount());
					
			//Dosage
			dosage = String.valueOf(prescribedDrugs.get(0).getTimesPerDay());
					
			customizedDosage = iDartProperties.TOMAR + String.valueOf((int)(prescribedDrugs.get(0).getAmtPerTime())) 
					+ iDartProperties.COMP + dosage + iDartProperties.VEZES_DIA;
			
		 	inputAddPerson = new StringEntity(
		 			"{\"encounterDatetime\": \""+encounterDatetime+"\", \"patient\": \""+nidUuid+"\", \"encounterType\": \""+encounterType+"\", "
		 			  + "\"location\":\""+strFacilityUuid+"\", \"form\":\""+filaUuid+"\", \"encounterProviders\":[{\"provider\":\""+providerUuid+"\", \"encounterRole\":\"a0b03050-c99b-11e0-9572-0800200c9a66\"}], "
		 			  + "\"obs\":[{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+regimeUuid+"\",\"value\":\""+strRegimenAnswerUuid+"\", \"comment\":\"IDART\"},{\"person\":"
		 			  + "\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+packSize+"\",\"comment\":\"IDART\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage+"\",\"comment\":\"IDART\"},{\"person\":\""+nidUuid+"\","
		 			  + "\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+returnVisitUuid+"\",\"value\":\""+strNextPickUp+"\",\"comment\":\"IDART\"}]}"
		 			);
		 	
		 	System.out.println(IOUtils.toString(inputAddPerson.getContent())); 
		 	
		 	/*inputAddPerson = new StringEntity(
		 			"{\"encounterDatetime\": \""+encounterDatetime+"\", \"patient\": \""+nidUuid+"\", \"encounterType\": \""+encounterType+"\", "
		 			  + "\"location\":\""+strFacilityUuid+"\", \"form\":\""+filaUuid+"\", \"provider\":\""+providerUuid+"\", "
		 			  + "\"obs\":[{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+regimeUuid+"\",\"value\":\""+strRegimenAnswerUuid+"\"},{\"person\":"
		 			  + "\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+packSize+"\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage+"\"},{\"person\":\""+nidUuid+"\","
		 			  + "\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+returnVisitUuid+"\",\"value\":\""+strNextPickUp+"\"}]}"
		 			);*/
		} else if (prescribedDrugs.size() > 1) {
			
			//Dosage
			dosage = String.valueOf(prescribedDrugs.get(0).getTimesPerDay());
					
			String customizedDosage_0 = iDartProperties.TOMAR + String.valueOf((int)(prescribedDrugs.get(0).getAmtPerTime())) 
					+ iDartProperties.COMP + dosage + iDartProperties.VEZES_DIA;
			
			//Dosage
			dosage_1 = String.valueOf(prescribedDrugs.get(1).getTimesPerDay());
					
			String customizedDosage_1 = iDartProperties.TOMAR + String.valueOf((int)(prescribedDrugs.get(1).getAmtPerTime())) 
					+ iDartProperties.COMP + dosage_1 + iDartProperties.VEZES_DIA;
			
		 	inputAddPerson = new StringEntity(
		 			"{\"encounterDatetime\": \""+encounterDatetime+"\", \"patient\": \""+nidUuid+"\", \"encounterType\": \""+encounterType+"\", "
		 					+ "\"location\":\""+strFacilityUuid+"\", \"form\":\""+filaUuid+"\", \"encounterProviders\":[{\"provider\":\""+providerUuid+"\", \"encounterRole\":\"a0b03050-c99b-11e0-9572-0800200c9a66\"}], "
		 			  + "\"obs\":[{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+regimeUuid+"\",\"value\":\""+strRegimenAnswerUuid+"\",\"comment\":\"IDART\"},{\"person\":"
		 			  + "\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+String.valueOf(packagedDrugs.get(1).getAmount())+"\",\"comment\":\"IDART\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+String.valueOf(packagedDrugs.get(0).getAmount())+"\",\"comment\":\"IDART\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage_0+"\",\"comment\":\"IDART\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage_1+"\",\"comment\":\"IDART\"},{\"person\":\""+nidUuid+"\","
		 			  + "\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+returnVisitUuid+"\",\"value\":\""+strNextPickUp+"\",\"comment\":\"IDART\"}]}"
		 			);
		 	
		 	/*inputAddPerson = new StringEntity(
		 			"{\"encounterDatetime\": \""+encounterDatetime+"\", \"patient\": \""+nidUuid+"\", \"encounterType\": \""+encounterType+"\", "
		 			  + "\"location\":\""+strFacilityUuid+"\", \"form\":\""+filaUuid+"\", \"provider\":\""+providerUuid+"\", "
		 			  + "\"obs\":[{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+regimeUuid+"\",\"value\":\""+strRegimenAnswerUuid+"\"},{\"person\":"
		 			  + "\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+String.valueOf(prescribedDrugs.get(1).getDrug().getPackSize())+"\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+String.valueOf(prescribedDrugs.get(0).getDrug().getPackSize())+"\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage_0+"\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage_1+"\"},{\"person\":\""+nidUuid+"\","
		 			  + "\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+returnVisitUuid+"\",\"value\":\""+strNextPickUp+"\"}]}"
		 			);*/
		}
		
		inputAddPerson.setContentType("application/json");
		 	//log.info("AddPerson = " + ApiAuthRest.getRequestPost("encounter",inputAddPerson));
		return ApiAuthRest.getRequestPost("encounter",inputAddPerson); 
	}
	
	public boolean postOpenMRSPatient(String gender, String firstName, String middleName, String lastName, String birthDate, String nid) throws Exception {
		
		StringEntity inputAddPatient;
		
		if (birthDate.isEmpty()) {
			
			inputAddPatient = new StringEntity(
					"{\"person\":"
							+"{"
							+"\"gender\": \""+gender+"\","
							+"\"names\":"
							+"[{\"givenName\": \""+firstName+"\", \"middleName\": \""+middleName+"\", \"familyName\": \""+lastName+"\"}]"
							+"},"
							+"\"identifiers\":"
							+"["
							+"{"
							+"\"identifier\": \""+nid+"\", \"identifierType\": \"e2b966d0-1d5f-11e0-b929-000c29ad1d07\","
							+"\"location\": \""+prop.getProperty("location")+"\", \"preferred\": \"true\""
							+"}"
							+"]"
							+"}"
					);
		} else {
			
			inputAddPatient = new StringEntity(
					"{\"person\":"
							+"{"
							+"\"gender\": \""+gender+"\","
							+"\"names\":"
							+"[{\"givenName\": \""+firstName+"\", \"middleName\": \""+middleName+"\", \"familyName\": \""+lastName+"\"}], \"birthdate\": \""+birthDate+"\""
							+"},"
							+"\"identifiers\":"
							+"["
							+"{"
							+"\"identifier\": \""+nid+"\", \"identifierType\": \"e2b966d0-1d5f-11e0-b929-000c29ad1d07\","
							+"\"location\": \""+prop.getProperty("location")+"\", \"preferred\": \"true\""
							+"}"
							+"]"
							+"}"
					);
		}
		
	 	
		inputAddPatient.setContentType("application/json");
 		//log.info("AddPerson = " + ApiAuthRest.getRequestPost("encounter",inputAddPerson));
		return ApiAuthRest.getRequestPost("patient",inputAddPatient); 
	}
	
	public String getOpenMRSResource(String resourceParameter) {
		String resource = null;
		try {
			resource = ApiAuthRest.getRequestGet(resourceParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}
	
	public String getOpenMRSReportingRest(String resourceParameter) {
		ApiAuthRest.setURLReportingBase(prop.getProperty("urlBaseReportingRest"));
		String resource = null;
		try {
			resource = ApiAuthRest.getReportingRequestGet(resourceParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}
}