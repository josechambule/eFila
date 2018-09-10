package org.celllife.idart.rest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.http.entity.StringEntity;
import org.celllife.idart.commonobjects.iDartProperties;
import org.celllife.idart.database.hibernate.PackagedDrugs;
import org.celllife.idart.database.hibernate.PrescribedDrugs;
import org.celllife.idart.rest.ApiAuthRest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 
 * @author helio.machabane
 *
 */
public class RestClient {
	
	Properties prop = new Properties();
	InputStream input = null;
	private boolean pacTarv;
	
	//SET VALUE FOR CONNECT TO OPENMRS
	public RestClient() {
		input = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		
		try {
			prop.load(input);
			
			System.out.println(prop.getProperty("password")); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ApiAuthRest.setURLBase(prop.getProperty("urlBase"));
		ApiAuthRest.setUsername(prop.getProperty("userName"));
		ApiAuthRest.setPassword(prop.getProperty("password"));
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
		 			  + "\""+regimeUuid+"\",\"value\":\""+strRegimenAnswerUuid+"\"},{\"person\":"
		 			  + "\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+packSize+"\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage+"\"},{\"person\":\""+nidUuid+"\","
		 			  + "\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+returnVisitUuid+"\",\"value\":\""+strNextPickUp+"\"}]}"
		 			);
		 	
		 	System.out.println(inputAddPerson); 
		 	
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
		 			  + "\""+regimeUuid+"\",\"value\":\""+strRegimenAnswerUuid+"\"},{\"person\":"
		 			  + "\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+String.valueOf(packagedDrugs.get(1).getAmount())+"\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+dispensedAmountUuid+"\","
		 			  + "\"value\":\""+String.valueOf(packagedDrugs.get(0).getAmount())+"\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage_0+"\"},{\"person\":\""+nidUuid+"\",\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":"
		 			  + "\""+dosageUuid+"\",\"value\":\""+customizedDosage_1+"\"},{\"person\":\""+nidUuid+"\","
		 			  + "\"obsDatetime\":\""+encounterDatetime+"\",\"concept\":\""+returnVisitUuid+"\",\"value\":\""+strNextPickUp+"\"}]}"
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
	
	public boolean isPatientInTarv (String personUuid) throws JSONException, Exception {
		
		JSONObject jsonObject = new JSONObject(ApiAuthRest.getRequestGet(iDartProperties.REST_PROGRAM_ENROLLMENT+personUuid));

		JSONArray jsonArray = (JSONArray) jsonObject.get("results");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject results = (JSONObject) jsonArray.get(i);

			if ((String.valueOf(results.get("display"))).trim().equals("SERVICO TARV - TRATAMENTO") && 
					(String.valueOf(results.get("dateCompleted"))).trim().equals("null")) {
				pacTarv = true; 
				break;
			}
		}
		return pacTarv;
	}
}