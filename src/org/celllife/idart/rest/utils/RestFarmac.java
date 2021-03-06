package org.celllife.idart.rest.utils;

import com.google.gson.Gson;
import migracao.swingreverse.DadosPacienteFarmac;
import model.manager.AdministrationManager;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CentralizationProperties;
import org.celllife.idart.database.hibernate.*;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.rest.ApiAuthRest;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class RestFarmac {

    Properties prop = new Properties();

    public final static Logger log = Logger.getLogger(RestFarmac.class);

    File input = new File("centralization.properties");

    public RestFarmac() {

        try {
            prop.load(new FileInputStream(input));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        ApiAuthRest.setURLBase(prop.getProperty("centralized_server_url"));
        ApiAuthRest.setUsername(prop.getProperty("rest_access_username"));
        ApiAuthRest.setPassword(prop.getProperty("rest_access_password"));
    }

    public static String restGeAllPatients(String url, Clinic refClinic, PoolingHttpClientConnectionManager pool) {
        HttpResponse response = null;

        Session sess = HibernateUtil.getNewSession();
        Transaction tx = sess.beginTransaction();

        String path = url + "/sync_temp_patients?syncstatus=eq.P&clinicuuid=eq." + refClinic.getUuid();
        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password,pool);;

            response = ApiAuthRest.postgrestRequestGetAll(path,token,pool);

            InputStream in = response.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder str = new StringBuilder();
            SyncTempPatient syncTempPatient = null;
            String line = null;
            String objectString = null;
            JSONObject jsonObj = null;
            Gson gson = null;


            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");

                if (line.startsWith("[{"))
                    line = line.replace("[{", "{");
                if (line.endsWith("}]"))
                    line = line.replace("}]", "}");

                objectString = line;
                if (objectString.contains("{")) {
                    jsonObj = new JSONObject(objectString);
                    gson = new Gson();
                    try {
                        syncTempPatient = gson.fromJson(jsonObj.toString(), SyncTempPatient.class);
                        String updateStatus = "{\"syncstatus\":\"I\"}";
                        AdministrationManager.saveSyncTempPatient(sess, syncTempPatient);

                        restPatchPatient(url, syncTempPatient, updateStatus,pool);
                       log.trace(" Paciente [" + syncTempPatient + "] Refrido de " + syncTempPatient.getMainclinicname() + " carregado com sucesso");
                        break;
                    } catch (Exception e) {
                        assert tx != null;
                        tx.rollback();
                       log.trace(" Ocorreu um erro ao gravar a informacao do Paciente [" + syncTempPatient + "] Refrido de " + syncTempPatient.getMainclinicname());
                    } finally {

                        continue;
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert tx != null;
        tx.commit();
        sess.flush();
        sess.close();

        return response.getStatusLine().toString();
    }

    public static String restGetPatient(SyncTempPatient syncTempPatient, String url,PoolingHttpClientConnectionManager pool) throws Exception {

        String pathuuid = url + "/sync_temp_patients?uuid=eq." + syncTempPatient.getUuid();
        String pathnidandclinic = url + "/sync_temp_patients?patientid=eq." + syncTempPatient.getPatientid() + "&mainclinic=eq." + syncTempPatient.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password,pool);

            httpResponse = ApiAuthRest.postgrestRequestGet(pathuuid,token,pool);

            if (httpResponse != null) {
                if (httpResponse.getStatusLine().getStatusCode() != 200)
                    httpResponse = ApiAuthRest.postgrestRequestGet(pathnidandclinic,token,pool);
            }

            if (httpResponse.getStatusLine().getStatusCode() != 200)
                response = "Falha no POSTGREST GET - Code:" + httpResponse.getStatusLine().getStatusCode();
            else
                response = "POSTGREST GET efectiado com sucesso - Code:" + httpResponse.getStatusLine().getStatusCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String restPostPatient(String url, SyncTempPatient syncTempPatient,PoolingHttpClientConnectionManager pool) throws UnsupportedEncodingException {

        String path = url + "/sync_temp_patients";
        HttpResponse httpResponse = null;
        String response = null;

        Gson g = new Gson();
        String restObject = g.toJson(syncTempPatient);
        StringEntity inputAddPatient = new StringEntity(restObject, "UTF-8");
        inputAddPatient.setContentType("application/json");
        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password,pool);

            httpResponse = ApiAuthRest.postgrestRequestPost(path, inputAddPatient,token,pool);

            if (httpResponse != null) {
                if (((float) httpResponse.getStatusLine().getStatusCode() / 200) >= 1.5)
                    response = "Falha no POSTGREST POST - Code:" + httpResponse.getStatusLine().getStatusCode();
                else
                    response = "POSTGREST POST efectiado com sucesso - Code:" + httpResponse.getStatusLine().getStatusCode();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String restPutPatient(String url, SyncTempPatient syncTempPatient, PoolingHttpClientConnectionManager pool) throws Exception {

        String path = url + "/sync_temp_patients?id=eq." + syncTempPatient.getId() + "&mainclinic=eq." + syncTempPatient.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        Gson g = new Gson();
        String restObject = g.toJson(syncTempPatient);
        StringEntity inputAddPatient = new StringEntity(restObject, "UTF-8");
        inputAddPatient.setContentType("application/json");

        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password,pool);

            httpResponse = ApiAuthRest.postgrestRequestPut(path, inputAddPatient,token,pool);

            if (httpResponse != null) {
                if (((float) httpResponse.getStatusLine().getStatusCode() / 200) >= 1.5)
                    response = "Falha no POSTGREST PUT - Code:" + httpResponse.getStatusLine().getStatusCode();
                else
                    response = "POSTGREST PUT efectiado com sucesso - Code:" + httpResponse.getStatusLine().getStatusCode();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

    public static String restPatchPatient(String url, SyncTempPatient syncTempPatient, String restObject, PoolingHttpClientConnectionManager pool) throws UnsupportedEncodingException {

        String path = url + "/sync_temp_patients?id=eq." + syncTempPatient.getId() + "&mainclinic=eq." + syncTempPatient.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        StringEntity inputAddPatient = new StringEntity(restObject, "UTF-8");
        inputAddPatient.setContentType("application/json");

        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password, pool);

            httpResponse = ApiAuthRest.postgrestRequestPatch(path, inputAddPatient,token, pool);

            if (httpResponse != null) {
                if (httpResponse.getStatusLine().getStatusCode() != 200)
                    if (httpResponse.getStatusLine().getStatusCode() != 200)
                        response = "Falha no POSTGREST PATCH - Code:" + httpResponse.getStatusLine().getStatusCode();
                    else
                        response = " POSTGREST PATCH efectiado com sucesso - Code:" + httpResponse.getStatusLine().getStatusCode();
            }
            response = "Nao foi executado o POSTGREST PATCH request";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

    public static String restPatchDispense(String url, SyncTempDispense syncTempDispense, String restObject, PoolingHttpClientConnectionManager pool) throws UnsupportedEncodingException {

        String path = url + "/sync_temp_dispense?id=eq." + syncTempDispense.getId() + "&mainclinic=eq." + syncTempDispense.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        //  Gson g = new Gson();
        //  String restObject = g.toJson(syncTempDispense);
        StringEntity inputAddDispense = new StringEntity(restObject, "UTF-8");
        inputAddDispense.setContentType("application/json");

        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password, pool);

            httpResponse = ApiAuthRest.postgrestRequestPatch(path, inputAddDispense,token, pool);

            if (httpResponse != null) {
                if (httpResponse.getStatusLine().getStatusCode() != 200)
                    if (httpResponse.getStatusLine().getStatusCode() != 200)
                        response = "Falha no POSTGREST PATCH - Code:" + httpResponse.getStatusLine().getStatusCode();
                    else
                        response = " POSTGREST PATCH efectiado com sucesso - Code:" + httpResponse.getStatusLine().getStatusCode();
            }
            response = "Nao foi executado o POSTGREST PATCH request";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

    public String restDeletePatient(String url, SyncTempPatient syncTempPatient, PoolingHttpClientConnectionManager pool) {

        String pathuuid = url + "/sync_temp_patients?uuid=eq." + syncTempPatient.getUuid();
        String pathnidandclinic = url + "/sync_temp_patients?patientid=eq." + syncTempPatient.getPatientid() + "&mainclinic=eq." + syncTempPatient.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password, pool);

            httpResponse = ApiAuthRest.postgrestRequestDelete(pathuuid,token, pool);

            if (httpResponse != null) {
                if (httpResponse.getStatusLine().getStatusCode() != 200)
                    httpResponse = ApiAuthRest.postgrestRequestDelete(pathnidandclinic,token, pool);
            }

            if (httpResponse.getStatusLine().getStatusCode() != 200)
                response = "Falha no POSTGREST DELETE - Code:" + httpResponse.getStatusLine().getStatusCode();
            else
                response = "POSTGREST DELETE efectiado com sucesso - Code:" + httpResponse.getStatusLine().getStatusCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String restGeAllDispenses(String url, Clinic mainClinic, PoolingHttpClientConnectionManager pool) {
        HttpResponse response = null;
        Session sess = HibernateUtil.getNewSession();
        Transaction tx = sess.beginTransaction();
        String path = url + "/sync_temp_dispense?syncstatus=eq.P&mainclinicuuid=eq." + mainClinic.getUuid();
        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password,pool);

            response = ApiAuthRest.postgrestRequestGetAll(path,token,pool);
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            SyncTempDispense syncTempDispense = null;
            String line = null;
            String objectString = null;
            JSONObject jsonObj = null;
            Gson gson = null;
            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");

                if (line.startsWith("[{"))
                    line = line.replace("[{", "{");
                if (line.endsWith("}]"))
                    line = line.replace("}]", "}");

                objectString = line;
                if (objectString.contains("{")) {
                    jsonObj = new JSONObject(objectString);
                    gson = new Gson();
                    try {
                        syncTempDispense = gson.fromJson(jsonObj.toString(), SyncTempDispense.class);
                        String updateStatus = "{\"syncstatus\":\"I\"}";

                        AdministrationManager.saveSyncTempDispense(sess, syncTempDispense);
                        restPatchDispense(url, syncTempDispense, updateStatus,pool);
                       log.trace(" Informacao de Levantamento do Paciente [" + syncTempDispense.getPatientid() + "] referido de " + syncTempDispense.getMainclinicname() + " carregada/actualizado com sucesso");
                        break;
                    } catch (Exception e) {
                       log.trace(" Ocorreu um erro ao carregar a informacao do Paciente [" + syncTempDispense.getPatientid() + "] Refrido de " + syncTempDispense.getMainclinicname() + " ERRO: " + e.getMessage());
                    } finally {

                        continue;
                    }
                } else {
                   log.trace(new Date() + " [FARMAC] INFO - Nenhumm Levantamento do paciente referido foi encontrado");
                }
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert tx != null;
        tx.commit();
        sess.flush();
        sess.close();

        return response.getStatusLine().toString();
    }

    public static String restPostDispense(String url, SyncTempDispense syncTempDispense,  PoolingHttpClientConnectionManager pool) throws UnsupportedEncodingException {

        String path = url + "/sync_temp_dispense";
        HttpResponse httpResponse = null;
        String response = null;

        Gson g = new Gson();
        String restObject = g.toJson(syncTempDispense);

        StringEntity inputAddDispense = new StringEntity(restObject, "UTF-8");
        inputAddDispense.setContentType("application/json");

        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password,pool);

            httpResponse = ApiAuthRest.postgrestRequestPost(path, inputAddDispense,token,pool);

            if (httpResponse != null) {
                if (((float) httpResponse.getStatusLine().getStatusCode() / 200) >= 1.5)
                    response = "Falha no POSTGREST POST - Code:" + httpResponse.getStatusLine().getStatusCode();
                else
                    response = "POSTGREST POST efectiado com sucesso - Code:" + httpResponse.getStatusLine().getStatusCode();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void restPostPatients(Session sess, String url, PoolingHttpClientConnectionManager pool) throws UnsupportedEncodingException {

        List<SyncTempPatient> syncTempPatients = AdministrationManager.getAllSyncTempPatientReadyToSend(sess);
        String result = "";
        if (syncTempPatients.isEmpty())
           log.trace(new Date() + " [FARMAC] INFO - Nenhumm paciente foi encontrado para referir");
        else
            for (SyncTempPatient patientSync : syncTempPatients) {
                try {
                    result = restPostPatient(url, patientSync,pool);
                    if (result.contains("Falha") && !result.contains("409")) {
                       log.trace(new Date() + ": Ocorreu um erro ao gravar o paciente com nid " + patientSync.getPatientid() + " Erro: " + result);
                    } else {
                       log.trace(new Date() + ":Paciente com nid " + patientSync.getPatientid() + " enviado com sucesso (" + result + ")");
                        patientSync.setSyncstatus('E');
                        AdministrationManager.saveSyncTempPatient(sess, patientSync);
                    }
                    break;
                } catch (Exception e) {
                   log.trace(e);
                } finally {
                    continue;
                }

            }
    }

    public static void restPostDispenses(Session sess, String url, PoolingHttpClientConnectionManager pool) throws UnsupportedEncodingException {

        List<SyncTempDispense> syncTempDispenses = AdministrationManager.getAllSyncTempDispenseReadyToSend(sess);

        String result = "";

        if (syncTempDispenses.isEmpty())
           log.trace(new Date() + " [FARMAC] INFO - Nenhum Levantamento de ARV de paciente foi encontrado para enviar");
        else
            for (SyncTempDispense dispenseSync : syncTempDispenses) {

                try {
                    result = restPostDispense(url, dispenseSync,pool);
                    if (result.contains("Falha")) {
                       log.trace(new Date() + ": Ocorreu um erro ao enviar o Levantamento do paciente com nid " + dispenseSync.getPatientid() + " Erro: " + result);
                    } else {
                       log.trace(new Date() + ": Levantamento do Paciente com nid " + dispenseSync.getPatientid() + " enviado com sucesso (" + result + ")");
                        dispenseSync.setSyncstatus('E');
                        AdministrationManager.saveSyncTempDispense(sess, dispenseSync);
                    }
                    break;
                } catch (Exception e) {

                } finally {

                    continue;
                }

            }
    }

    public static void setPatientsFromRest(Session sess) {

        List<SyncTempPatient> syncTempPatients = AdministrationManager.getAllSyncTempPatientReadyToSend(sess);

        if (!syncTempPatients.isEmpty()) {

            for (SyncTempPatient patient : syncTempPatients) {
                try {
                    DadosPacienteFarmac.InserePaciente(sess, patient);
                    patient.setSyncstatus('I');
                    AdministrationManager.saveSyncTempPatient(sess, patient);
                    break;
                } catch (Exception e) {
                   log.trace("Erro ao gravar informacao do Paciente [" + patient.getFirstnames() + " " + patient.getLastname() + " com NID: " + patient.getPatientid() + "]");
                } finally {
                    continue;
                }
            }
        } else {
           log.trace(new Date() + ": [FARMAC] INFO - Nenhumm paciente referido para esta FARMAC foi encontrado");
        }
    }

    public static void setDispensesFromRest(Session sess) {

        List<SyncTempDispense> syncTempDispenses = AdministrationManager.getAllSyncTempDispenseReadyToSend(sess);

        if (!syncTempDispenses.isEmpty()) {

            for (SyncTempDispense dispense : syncTempDispenses) {
                try {
                    Prescription prescription = DadosPacienteFarmac.getPatientPrescritionFarmac(dispense);

                    DadosPacienteFarmac.saveDispenseFarmacQty0(prescription, dispense);
                    DadosPacienteFarmac.setDispenseRestOpenmrs(sess, prescription, dispense);
                        dispense.setSyncstatus('I');
                        AdministrationManager.saveSyncTempDispense(sess, dispense);

                    break;
                } catch (Exception e) {
                   log.trace("Erro ao gravar levantamento do Paciente com NID: [" + dispense.getPatientid() + "]");
                } finally {
                    continue;
                }
            }
        } else {
           log.trace(new Date() + ": [US] INFO - Nenhumm levantamento enviado para esta US foi encontrado");
        }
    }

    public static List<Clinic> restGeAllClinicByProvinceAndDistrictAndFacilityType(String url, String province, String district, String facilitytype, Session session, PoolingHttpClientConnectionManager pool) {
        HttpResponse response = null;
        List<Clinic> clinicList = new ArrayList<>();
        List<Clinic> localClinics = AdministrationManager.getClinics(session);
        String path = url + "/clinic?province=eq." + province + "&district=eq." + district + "&facilitytype=eq." + facilitytype;
        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password, pool);

            response = ApiAuthRest.postgrestRequestGetAll(path,token,pool);
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();

            Clinic clinic = null;
            String line = null;
            String objectString = null;
            JSONObject jsonObj = null;
            Gson gson = null;


            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");
                if (line.startsWith("[{"))
                    line = line.replace("[{", "{");
                if (line.endsWith("}]"))
                    line = line.replace("}]", "}");

                objectString = line;

                if (objectString.contains("{")) {
                    jsonObj = new JSONObject(objectString);
                    gson = new Gson();
                    try {
                        clinic = gson.fromJson(jsonObj.toString(), Clinic.class);
                        clinic.setFacilityType(jsonObj.getString("facilitytype"));
                        clinic.setClinicName(jsonObj.getString("clinicname"));
                        clinic.setSubDistrict(jsonObj.getString("subdistrict"));
                        boolean existClinic = false;
                        for (Clinic localClinic : localClinics) {
                            if (localClinic.getUuid().equals(clinic.getUuid())) {
                                existClinic = true;
                                break;
                            }
                        }

                        if (!existClinic)
                            clinicList.add(clinic);
                        break;
                    } catch (Exception e) {
                       log.trace(" Ocorreu um erro ao adicionar a clinic [" + clinic.getClinicName() + "]");
                    } finally {
                        if (reader != null)
                            reader.close();
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clinicList;
    }

    public static List<Drug> restGeAllDrugsByDeseaseTypeAndStatus(String url, String deseaseType, boolean status, Session session, PoolingHttpClientConnectionManager pool) {
        HttpResponse response = null;
        List<Drug> drugList = new ArrayList<>();
        List<Drug> localDrugs = AdministrationManager.getDrugs(session);
        String path = url + "/drug?select=*,form(*)&tipodoenca=eq." + deseaseType + "&active=eq." + status;
        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password,pool);

            StringBuilder str = ApiAuthRest.postgrestRequestGetBuffer(path,token,pool);
            Drug drug = new Drug();
            String objectString = null;
            JSONObject jsonObj = null;
            Gson gson = null;

            String[] lines = str.toString().split("\\n");

            for (String line : lines) {
                if (line.startsWith("[{"))
                    line = line.replace("[{", "{");
                if (line.endsWith("}]"))
                    line = line.replace("}]", "}");

                objectString = line.replaceFirst("form", "formid");

                if (objectString.contains("{")) {
                    jsonObj = new JSONObject(objectString);
                    gson = new Gson();
                    try {
                        drug = gson.fromJson(jsonObj.toString(), Drug.class);
                        drug.setDispensingInstructions1(jsonObj.getString("dispensinginstructions1"));
                        drug.setDispensingInstructions2(jsonObj.getString("dispensinginstructions2"));
                        drug.setPackSize(jsonObj.getInt("packsize"));
                        drug.setSideTreatment(jsonObj.getString("sidetreatment").charAt(0));
                        drug.setDefaultAmnt(jsonObj.getInt("defaultamnt"));
                        drug.setTipoDoenca(jsonObj.getString("tipodoenca"));
                        drug.setDefaultTimes(jsonObj.getInt("defaulttimes"));

                        boolean existDrug = false;
                        for (Drug localDrug : localDrugs) {
                            if (localDrug.getAtccode().equals(drug.getAtccode())) {
                                existDrug = true;
                                break;
                            }
                        }
                        if (!existDrug)
                            drugList.add(drug);
                    } catch (Exception e) {
                       log.trace(" Ocorreu um erro ao adicionar o Medicamento [" + drug.getName() + "] " + e.getMessage());
                    } finally {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           log.trace(e.getMessage());
        }

        return drugList;
    }

    public static List<RegimeTerapeutico> restGeAllRegimenByStatus(String url, boolean status, Session session, PoolingHttpClientConnectionManager pool) {
        HttpResponse response = null;
        List<RegimeTerapeutico> regimeTerapeuticoList = new ArrayList<>();
        List<RegimeTerapeutico> localRegimeTerapeutico = AdministrationManager.getRegimeTerapeutico(session);
        String path = url + "/regimeterapeutico?active=eq." + status;
        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password,pool);

            StringBuilder str = ApiAuthRest.postgrestRequestGetBuffer(path,token, pool);
            RegimeTerapeutico regimeTerapeutico = new RegimeTerapeutico();
            String objectString = null;
            JSONObject jsonObj = null;
            Gson gson = null;

            String[] lines = str.toString().split("\\n");

            for (String line : lines) {
                str.append(line + "\n");
                if (line.startsWith("[{"))
                    line = line.replace("[{", "{");
                if (line.endsWith("}]"))
                    line = line.replace("}]", "}");

                objectString = line;

                if (objectString.contains("{")) {
                    jsonObj = new JSONObject(objectString);
                    gson = new Gson();
                    try {
                        regimeTerapeutico = gson.fromJson(jsonObj.toString(), RegimeTerapeutico.class);

                        boolean existRegimen = false;
                        for (RegimeTerapeutico localRegimen : localRegimeTerapeutico) {
                            if (localRegimen.getCodigoregime().equalsIgnoreCase(regimeTerapeutico.getCodigoregime())) {
                                existRegimen = true;
                                break;
                            }
                        }

                        if (!existRegimen)
                            regimeTerapeuticoList.add(regimeTerapeutico);
                        break;
                    } catch (Exception e) {
                       log.trace(" Ocorreu um erro ao adicionar o Medicamento [" + regimeTerapeutico.getRegimeesquema() + "] " + e.getMessage());
                    } finally {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           log.trace(e.getMessage());
        }

        return regimeTerapeuticoList;
    }

    public static List<RegimenDrugs> restGeAllRegimenDrugsByRegimen(String url, RegimeTerapeutico regimeTerapeutico, Session session, PoolingHttpClientConnectionManager pool) {
        HttpResponse response = null;
        List<RegimenDrugs> regimenDrugsList = new ArrayList<>();
        String path = url + "/regimendrugs?select=*,drug(*,form(*))&regimen=eq."+regimeTerapeutico.getRegimeid();
        try {
            String token = restGetpermission(url, CentralizationProperties.rest_access_username,CentralizationProperties.rest_access_password, pool);

            StringBuilder str = ApiAuthRest.postgrestRequestGetBuffer(path,token, pool);
            RegimenDrugs regimenDrugs = new RegimenDrugs();
            String objectString = null;
            JSONObject jsonObj = null;
            Gson gson = null;

            String[] lines = str.toString().split("\\n");

            for (String line : lines) {
                if (line.startsWith("[{"))
                    line = line.replace("[{", "{");
                if (line.endsWith("}]"))
                    line = line.replace("}]", "}");

                objectString = line.replaceFirst("drug", "drugid");
                objectString = objectString.replaceFirst("regimen", "regimenid");
                objectString = objectString.replaceFirst("form", "formid");

                if (objectString.contains("{")) {
                    jsonObj = new JSONObject(objectString);
                    gson = new Gson();
                    try {
                        regimenDrugs = gson.fromJson(jsonObj.toString(), RegimenDrugs.class);
                        regimenDrugs.getDrug().setDispensingInstructions1(jsonObj.getJSONObject("drug").getString("dispensinginstructions1"));
                        regimenDrugs.getDrug().setDispensingInstructions2(jsonObj.getJSONObject("drug").getString("dispensinginstructions2"));
                        regimenDrugs.getDrug().setPackSize(jsonObj.getJSONObject("drug").getInt("packsize"));
                        regimenDrugs.getDrug().setSideTreatment(jsonObj.getJSONObject("drug").getString("sidetreatment").charAt(0));
                        regimenDrugs.getDrug().setDefaultAmnt(jsonObj.getJSONObject("drug").getInt("defaultamnt"));
                        regimenDrugs.getDrug().setTipoDoenca(jsonObj.getJSONObject("drug").getString("tipodoenca"));
                        regimenDrugs.getDrug().setDefaultTimes(jsonObj.getJSONObject("drug").getInt("defaulttimes"));
                        regimenDrugsList.add(regimenDrugs);
                    } catch (Exception e) {
//                       log.trace(" Ocorreu um erro ao adicionar o Medicamento [" + regimenDrugs.getDrug().getName() + "] do Regime Terapeutico [" +regimeTerapeutico.getRegimeesquema()+" ]"+ e.getMessage());
                      log.trace(e.getMessage());
                    } finally {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
           log.trace(e.getMessage());
        }

        return regimenDrugsList;
    }


    public static void setCentralPatients(Session sess) {

        List<SyncTempPatient> syncTempPatients = AdministrationManager.getAllSyncTempPatientReadyToSave(sess);

        if (!syncTempPatients.isEmpty()) {

            for (SyncTempPatient patient : syncTempPatients) {
                try {
                    DadosPacienteFarmac.InserePaciente(sess, patient);
                    patient.setSyncstatus('E');
                    AdministrationManager.saveSyncTempPatient(sess, patient);
                    break;
                } catch (Exception e) {
                   log.trace(new Date() + ": [Central] INFO - Erro ao gravar informacao do Paciente [" + patient.getFirstnames() + " " + patient.getLastname() + " com NID: " + patient.getPatientid() + "] provrniente de " + patient.getMainclinicname());
                } finally {
                    continue;
                }
            }
        } else {
           log.trace(new Date() + ": [Central] INFO - Nenhumm paciente referido para FARMAC foi encontrado");
        }
    }

    public static void setCentralDispenses(Session sess) {

        List<SyncTempDispense> syncTempDispenses = AdministrationManager.getAllSyncTempDispenseReadyToSave(sess);

        if (!syncTempDispenses.isEmpty()) {

            for (SyncTempDispense dispense : syncTempDispenses) {
                try {
                    Prescription prescription = DadosPacienteFarmac.getPatientPrescritionFarmac(dispense);
                    if(prescription != null) {
                        DadosPacienteFarmac.saveDispenseFarmacQty0(prescription, dispense);
                        dispense.setSyncstatus('E');
                    }else
                        dispense.setSyncstatus('W');
                    AdministrationManager.saveSyncTempDispense(sess, dispense);
                    break;
                } catch (Exception e) {
                   log.trace(new Date() + ": [Central] INFO - Erro ao gravar levantamento do Paciente com NID: [" + dispense.getPatientid() + "] proveniente de " + dispense.getMainclinicname());
                } finally {
                    continue;
                }
            }
        } else {
           log.trace(new Date() + ": [Central] INFO - Nenhumm levantamento enviado para US foi encontrado");
        }
    }

    public static String restGetpermission(String url, String username, String pass, PoolingHttpClientConnectionManager pool) throws UnsupportedEncodingException {
        String path = url + "/rpc/login";
        StringBuilder httpResponse = null;
        JSONObject jsonObj = null;
        String result = null;
        String updateStatus = "{\"username\":\""+username+"\"," +
                                "\"pass\":\""+pass+"\"}";

        StringEntity inputCheckAccess = new StringEntity(updateStatus, "UTF-8");
        inputCheckAccess.setContentType("application/json");

        try {
            httpResponse = ApiAuthRest.postgrestRequestPostBuffer(path, inputCheckAccess,pool);

            String[] lines = httpResponse.toString().split("\\n");

            for (String line : lines) {
                if (line.startsWith("[{"))
                    line = line.replace("[{", "{");
                if (line.endsWith("}]"))
                    line = line.replace("}]", "}");

                if (line.contains("{")) {
                    jsonObj = new JSONObject(line);
                    try {
                        result = jsonObj.get("token").toString();
                    } catch (Exception e) {
                       log.trace(e.getMessage());
                    } finally {
                        continue;
                    }
                }

            }
//           log.trace(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

