/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migracao.swingreverse;

import migracao.entidadesHibernate.importPatient.PatientAttributeImportService;
import migracao.entidadesHibernate.importPatient.PatientIdentifierImportService;
import migracao.entidadesHibernate.importPatient.PatientImportService;
import org.celllife.idart.database.hibernate.*;

/**
 *
 * @author colaco
 */
public class DadosPacienteFarmac {

    public static Patient InserePaciente(SyncTempPatient patientSync, Clinic clinic) {

        Patient patient = null;
        PatientIdentifierImportService patientIdentifierImportService = new PatientIdentifierImportService();
        PatientImportService patientImportService = new PatientImportService();
        PatientIdentifier importedPatientIdentifier = patientIdentifierImportService.findByIdentifier(patientSync.getPatientId());

        if (clinic.getClinicName().equalsIgnoreCase(patientSync.getClinicName())) {

            if (importedPatientIdentifier == null) {
                patient = new Patient();
            } else {
                patient = importedPatientIdentifier.getPatient();
            }

            patient.setFirstNames(patientSync.getFirstNames());
            patient.setAccountStatus(Boolean.FALSE);
            patient.setAddress1(patientSync.getAddress1());
            patient.setAddress2(patientSync.getAddress2());
            patient.setAddress3(patientSync.getAddress3());
            patient.setCellphone(patientSync.getCellphone());
            patient.setDateOfBirth(patientSync.getDateOfBirth());
            patient.setClinic(clinic);
            patient.setNextOfKinName(patientSync.getNextOfKinName());
            patient.setNextOfKinPhone(patientSync.getNextOfKinPhone());
            patient.setHomePhone(patientSync.getHomePhone());
            patient.setLastname(patientSync.getLastname());
            patient.setModified(patientSync.getModified());
            patient.setPatientId(patientSync.getPatientId());
            patient.setProvince(patientSync.getProvince());
            patient.setSex(patientSync.getSex());
            patient.setWorkPhone(null);
            patient.setRace(patientSync.getMainClinicName());
            patient.setUuidopenmrs(patientSync.getUuid());

            if (importedPatientIdentifier == null) {
                patientImportService.persist(patient);
            } else {
                patientImportService.update(patient);
            }
            return patient;
        } else {
            return null;
        }
    }

    public static void InserePatientIdentifier(Patient p, IdentifierType identifierType) {
        PatientIdentifierImportService identifierImportService = new PatientIdentifierImportService();
        PatientIdentifier importedPatientIdentifier = identifierImportService.findByIdentifier(p.getPatientId());
        if (importedPatientIdentifier == null) {
            PatientIdentifier patientIdentifierLocal = new PatientIdentifier();
            patientIdentifierLocal.setPatient(p);
            patientIdentifierLocal.setType(identifierType);
            patientIdentifierLocal.setValue(p.getPatientId());
            identifierImportService.persist(patientIdentifierLocal);
        } else {
            importedPatientIdentifier.setValue(p.getPatientId());
            importedPatientIdentifier.setValueEdit(importedPatientIdentifier.getValue());
            identifierImportService.update(importedPatientIdentifier);
        }
    }

    public static void InserePatientAttribute(Patient patient, String dataInicioTarv, AttributeType attributeType) {
        String pacienteId = "" + patient.getId();
        PatientAttributeImportService patientAttributeImportService = new PatientAttributeImportService();
        PatientAttribute importpatientAttribute = patientAttributeImportService.findByPatientId(pacienteId);
        PatientAttribute patientAttribute = importpatientAttribute == null ? new PatientAttribute() : importpatientAttribute;
        patientAttribute.setPatient(patient);
        patientAttribute.setValue(dataInicioTarv);
        patientAttribute.setType(attributeType);
        if (importpatientAttribute == null) {
            patientAttributeImportService.persist(patientAttribute);
        } else {
            patientAttributeImportService.update(patientAttribute);
        }
    }

}
