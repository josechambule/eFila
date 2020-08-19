/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.hibernate.Session
 */
package migracao.swingreverse;

import migracao.entidades.*;
import migracao.entidadesHibernate.importPatient.PatientAttributeImportService;
import migracao.entidadesHibernate.importPatient.PatientIdentifierImportService;
import migracao.entidadesHibernate.importPatient.PatientImportService;
import migracao.entidadesHibernate.servicos.*;
import model.manager.AdministrationManager;
import model.manager.PatientManager;
import org.celllife.idart.database.hibernate.AttributeType;
import org.celllife.idart.database.hibernate.Clinic;
import org.celllife.idart.database.hibernate.IdentifierType;
import org.celllife.idart.database.hibernate.Patient;

import java.util.List;
import java.util.Vector;

public class SwingReverse {
    public static void main(String[] args) {
        try {
            
            Vector pacientesVector = new Vector();
            PatientService patientService = new PatientService();
            PersonService personService = new PersonService();
            PersonNameService personNameService = new PersonNameService();
            PersonAddressService personAddressService = new PersonAddressService();
            PatientIdentifierService patientIdentifierService = new PatientIdentifierService();
            ObsService obsService = new ObsService();
            List<Obs> obslist = obsService.findAllByConcept("1190");
            PatientImportService patientImportService = new PatientImportService();
            PatientIdentifierImportService patientIdentifierImportService = new PatientIdentifierImportService();
            PatientAttributeImportService patientAttributeImportService = new PatientAttributeImportService();
            Clinic clinic = AdministrationManager.getMainClinic(patientImportService.patientImportDao().openCurrentSessionwithTransaction());
            IdentifierType identifierType = AdministrationManager.getNationalIdentifierType(patientImportService.patientImportDao().openCurrentSessionwithTransaction());
            AttributeType attributeType = PatientManager.getAttributeTypeObject(patientImportService.patientImportDao().openCurrentSessionwithTransaction(), "ARV Start Date");
            int current = 0;
            int lengthOfTask = obslist.size();
          
            if (lengthOfTask == 0) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("#### Sem Pacientes Listados para a Migracao ####");
            }
            
            for (Obs obj : obslist) {
                ++current;
                if (patientImportService.findByPatientId(obj.getPersonId().getPersonId().toString()) != null && (patientImportService.findByPatientId(obj.getPersonId().getPersonId().toString()) == null || !obj.getComments().equalsIgnoreCase("Updated"))) continue;
                PersonName personName = personNameService.findByPersonId(obj.getPersonId().getPersonId().toString());
                PersonAddress personAddress = personAddressService.findByPersonId(obj.getPersonId().getPersonId().toString());
                PatientIdentifier patientIdentifier = patientIdentifierService.findByPatientId(obj.getPersonId().getPersonId().toString());
                Person person = personService.findById(obj.getPersonId().getPersonId().toString());
                Patient patient = DadosPaciente.InserePaciente(patientIdentifier.getIdentifier(), person, personName, personAddress, clinic, patientImportService, obj.getPersonId().getPersonId().toString(),"");
                DadosPaciente.InserePatientIdentifier(patient, identifierType, patientIdentifier.getIdentifier(), patientIdentifierImportService);
                DadosPaciente.InserePatientAttribute(patient, obj.getObsDatetime().toGMTString(), attributeType, patientAttributeImportService);
                ObsService obsServiceActualizacao = new ObsService();
                obj.setComments("Imported");
                obsServiceActualizacao.update(obj);
                System.out.println(personName.getGivenName() + " " + personName.getFamilyName() + " Paciente Inserido/Actualizado com Sucesso com o NID " + patientIdentifier.getIdentifier());
            }
            
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("" + lengthOfTask + " Pacientes Importados do OpenMRS para o IDART com sucesso!!!!!!");
        }
        catch (Exception e) {
            System.err.println("ACONTECEU UM ERRO INESPERADO, Ligue o Servidor OpenMRS e Tente Novamente ou Contacte o Administrador \n" + e);
        }
    }
}

