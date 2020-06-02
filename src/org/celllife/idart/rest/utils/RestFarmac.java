package org.celllife.idart.rest.utils;

import com.google.gson.Gson;
import migracao.swingreverse.DadosPacienteFarmac;
import model.manager.AdministrationManager;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.celllife.idart.database.hibernate.Clinic;
import org.celllife.idart.database.hibernate.Prescription;
import org.celllife.idart.database.hibernate.SyncTempDispense;
import org.celllife.idart.database.hibernate.SyncTempPatient;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.rest.ApiAuthRest;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class RestFarmac {

    Properties prop = new Properties();

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

    public static String restGeAllPatients(String url, Clinic refClinic) {
        HttpResponse response = null;

        Session sess = HibernateUtil.getNewSession();
        Transaction tx = sess.beginTransaction();

        String path = url + "/sync_temp_patients?syncstatus=eq.P&clinicname=eq." + refClinic.getClinicName().replace(" ", "%20");
        try {
            response = ApiAuthRest.postgrestRequestGetAll(path);
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
                objectString = line.replace("[", "").replace("]", "");
                if (objectString.contains("{")) {
                    jsonObj = new JSONObject(objectString);
                    gson = new Gson();
                    try {
                        syncTempPatient = gson.fromJson(jsonObj.toString(), SyncTempPatient.class);
                        String updateStatus = "{\"syncstatus\":\"I\"}";
                        AdministrationManager.saveSyncTempPatient(sess, syncTempPatient);

                        restPatchPatient(url, syncTempPatient,updateStatus);
                        System.out.println(" Paciente [" + syncTempPatient + "] Refrido de " + syncTempPatient.getMainclinicname() + " carregado com sucesso");
                        break;
                    } catch (Exception e) {
                        assert tx != null;
                        tx.rollback();
                        System.out.println(" Ocorreu um erro ao gravar a informacao do Paciente [" + syncTempPatient + "] Refrido de " + syncTempPatient.getMainclinicname());
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

    public static String restGetPatient(SyncTempPatient syncTempPatient, String url) throws Exception {

        String pathuuid = url + "/sync_temp_patients?uuid=eq." + syncTempPatient.getUuid();
        String pathnidandclinic = url + "/sync_temp_patients?patientid=eq." + syncTempPatient.getPatientid() + "&mainclinic=eq." + syncTempPatient.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        try {
            httpResponse = ApiAuthRest.postgrestRequestGet(pathuuid);

            if (httpResponse != null) {
                if (httpResponse.getStatusLine().getStatusCode() != 200)
                    httpResponse = ApiAuthRest.postgrestRequestGet(pathnidandclinic);
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

    public static String restPostPatient(String url, SyncTempPatient syncTempPatient) throws UnsupportedEncodingException {

        String path = url + "/sync_temp_patients";
        HttpResponse httpResponse = null;
        String response = null;

        Gson g = new Gson();
        String restObject = g.toJson(syncTempPatient);
        StringEntity inputAddPatient = new StringEntity(restObject,"UTF-8");
        inputAddPatient.setContentType("application/json");
        try {
            httpResponse = ApiAuthRest.postgrestRequestPost(path, inputAddPatient);

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

    public static String restPutPatient(String url, SyncTempPatient syncTempPatient) throws Exception {

        String path = url + "/sync_temp_patients?id=eq." + syncTempPatient.getId() + "&mainclinic=eq." + syncTempPatient.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        Gson g = new Gson();
        String restObject = g.toJson(syncTempPatient);
        StringEntity inputAddPatient = new StringEntity(restObject,"UTF-8");
        inputAddPatient.setContentType("application/json");

        try {
            httpResponse = ApiAuthRest.postgrestRequestPut(path, inputAddPatient);

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

    public static String restPatchPatient(String url, SyncTempPatient syncTempPatient,String restObject) throws UnsupportedEncodingException {

        String path = url + "/sync_temp_patients?id=eq." + syncTempPatient.getId() + "&mainclinic=eq." + syncTempPatient.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        StringEntity inputAddPatient = new StringEntity(restObject,"UTF-8");
        inputAddPatient.setContentType("application/json");

        try {
            httpResponse = ApiAuthRest.postgrestRequestPatch(path, inputAddPatient);

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

    public static String restPatchDispense(String url, SyncTempDispense syncTempDispense, String restObject) throws UnsupportedEncodingException {

        String path = url + "/sync_temp_dispense?id=eq." + syncTempDispense.getId() + "&mainclinic=eq." + syncTempDispense.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        //  Gson g = new Gson();
        //  String restObject = g.toJson(syncTempDispense);
        StringEntity inputAddDispense = new StringEntity(restObject,"UTF-8");
        inputAddDispense.setContentType("application/json");

        try {
            httpResponse = ApiAuthRest.postgrestRequestPatch(path, inputAddDispense);

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

    public String restDeletePatient(String url, SyncTempPatient syncTempPatient) {

        String pathuuid = url + "/sync_temp_patients?uuid=eq." + syncTempPatient.getUuid();
        String pathnidandclinic = url + "/sync_temp_patients?patientid=eq." + syncTempPatient.getPatientid() + "&mainclinic=eq." + syncTempPatient.getMainclinic();
        HttpResponse httpResponse = null;
        String response = null;

        try {
            httpResponse = ApiAuthRest.postgrestRequestDelete(pathuuid);

            if (httpResponse != null) {
                if (httpResponse.getStatusLine().getStatusCode() != 200)
                    httpResponse = ApiAuthRest.postgrestRequestDelete(pathnidandclinic);
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

    public static String restGeAllDispenses(String url, Clinic mainClinic) {
        HttpResponse response = null;
        Session sess = HibernateUtil.getNewSession();
        Transaction tx = sess.beginTransaction();
        String path = url + "/sync_temp_dispense?syncstatus=eq.P&mainclinicname=eq." + mainClinic.getClinicName().replace(" ", "%20");
        try {
            response = ApiAuthRest.postgrestRequestGetAll(path);
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
                objectString = line.replace("[", "").replace("]", "");
                if (objectString.contains("{")) {
                    jsonObj = new JSONObject(objectString);
                    gson = new Gson();
                    try {
                        syncTempDispense = gson.fromJson(jsonObj.toString(), SyncTempDispense.class);
                        String updateStatus = "{\"syncstatus\":\"I\"}";

                        AdministrationManager.saveSyncTempDispense(sess, syncTempDispense);
                        restPatchDispense(url, syncTempDispense, updateStatus);
                        System.out.println(" Informacao de Levantamento do Paciente [" + syncTempDispense.getPatientid() + "] referido de " + syncTempDispense.getMainclinicname() + " carregada/actualizado com sucesso");
                        break;
                    } catch (Exception e) {
                        System.out.println(" Ocorreu um erro ao carregar a informacao do Paciente [" + syncTempDispense.getPatientid() + "] Refrido de " + syncTempDispense.getMainclinicname() + " ERRO: " + e.getMessage());
                    } finally {

                        continue;
                    }
                } else {
                    System.out.println(new Date() + " [FARMAC] INFO - Nenhumm Levantamento do paciente referido foi encontrado");
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

    public static String restPostDispense(String url, SyncTempDispense syncTempDispense) throws UnsupportedEncodingException {

        String path = url + "/sync_temp_dispense";
        HttpResponse httpResponse = null;
        String response = null;

        Gson g = new Gson();
        String restObject = g.toJson(syncTempDispense);

        StringEntity inputAddDispense = new StringEntity(restObject,"UTF-8");
        inputAddDispense.setContentType("application/json");

        try {
            httpResponse = ApiAuthRest.postgrestRequestPost(path, inputAddDispense);

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

    public static void restPostPatients(Session sess, String url) throws UnsupportedEncodingException {

        List<SyncTempPatient> syncTempPatients = AdministrationManager.getAllSyncTempPatientReadyToSend(sess);
        String result = "";
        if (syncTempPatients.isEmpty())
            System.out.println(new Date() + " [FARMAC] INFO - Nenhumm paciente foi encontrado para referir");
        else
            for (SyncTempPatient patientSync : syncTempPatients) {
                try {
                    result = restPostPatient(url, patientSync);
                    if (result.contains("Falha") && !result.contains("409")) {
                        System.out.println(new Date() + ": Ocorreu um erro ao gravar o paciente com nid " + patientSync.getPatientid() + " Erro: " + result);
                    } else {
                        System.out.println(new Date() + ":Paciente com nid " + patientSync.getPatientid() + " enviado com sucesso (" + result + ")");
                        patientSync.setSyncstatus('E');
                        AdministrationManager.saveSyncTempPatient(sess, patientSync);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    continue;
                }

            }
    }

    public static void restPostDispenses(Session sess, String url) throws UnsupportedEncodingException {

        List<SyncTempDispense> syncTempDispenses = AdministrationManager.getAllSyncTempDispenseReadyToSend(sess);

        String result = "";

        if (syncTempDispenses.isEmpty())
            System.out.println(new Date() + " [FARMAC] INFO - Nenhum Levantamento de ARV de paciente foi encontrado para enviar");
        else
            for (SyncTempDispense dispenseSync : syncTempDispenses) {

                try {
                    result = restPostDispense(url, dispenseSync);
                    if (result.contains("Falha")) {
                        System.out.println(new Date() + ": Ocorreu um erro ao enviar o Levantamento do paciente com nid " + dispenseSync.getPatientid() + " Erro: " + result);
                    } else {
                        System.out.println(new Date() + ": Levantamento do Paciente com nid " + dispenseSync.getPatientid() + " enviado com sucesso (" + result + ")");
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
                    System.out.println("Erro ao gravar informacao do Paciente [" + patient.getFirstnames() + " " + patient.getLastname() + " com NID: " + patient.getPatientid() + "]");
                } finally {
                    continue;
                }
            }
        } else {
            System.out.println(new Date() + ": [FARMAC] INFO - Nenhumm paciente referido para esta FARMAC foi encontrado");
        }
    }

    public static void setDispensesFromRest(Session sess) {

        List<SyncTempDispense> syncTempDispenses = AdministrationManager.getAllSyncTempDispenseReadyToSend(sess);

        if (!syncTempDispenses.isEmpty()) {

            for (SyncTempDispense dispense : syncTempDispenses) {
                try {
                    Prescription prescription = DadosPacienteFarmac.getPatientPrescritionFarmac(dispense);

                    DadosPacienteFarmac.saveDispenseFarmacQty0(prescription, dispense);

                    if (DadosPacienteFarmac.setDispenseRestOpenmrs(sess, prescription, dispense)) {
                        dispense.setSyncstatus('I');
                        AdministrationManager.saveSyncTempDispense(sess, dispense);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Erro ao gravar levantamento do Paciente com NID: [" + dispense.getPatientid() + "]");
                } finally {
                    continue;
                }
            }
        } else {
            System.out.println(new Date() + ": [US] INFO - Nenhumm levantamento enviado para esta US foi encontrado");
        }
    }


}

