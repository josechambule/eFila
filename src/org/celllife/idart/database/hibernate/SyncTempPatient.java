/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.celllife.idart.database.hibernate;

import org.celllife.idart.misc.iDARTUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 *
 * @author colaco
 */
@Entity
@XmlRootElement
@Table(name = "sync_temp_patients")
public class SyncTempPatient {

    @Id
    private Integer id;
    private Boolean accountStatus;
    private String address1;
    private String address2;
    private String address3;
    private String cellphone;
    private Date dateOfBirth;
    private Integer clinic;
    private String clinicName;
    private Integer mainClinic;
    private String mainClinicName;
    private String nextOfKinName;
    private String nextOfKinPhone;
    private String firstNames;
    private String homePhone;
    private String lastname;
    private char modified;
    private String patientId;
    private String province;
    private char sex;
    private String workPhone;
    private String race;
    private String uuid;
    private String datainiciotarv;

    public SyncTempPatient() {
        super();
        this.id = -1;
    }

    /**
     * Method getAccountStatus.
     *
     * @return Boolean
     * @deprecated use use getAccountStatusWithCheck
     */
    @Deprecated
    public Boolean getAccountStatus() {
        return accountStatus;
    }

    /**
     * Method getAddress1.
     *
     * @return String
     */
    public String getAddress1() {
        if (address1 == null) {
            return "";
        }
        return address1;
    }

    /**
     * Method getAddress2.
     *
     * @return String
     */
    public String getAddress2() {
        if (address2 == null) {
            return "";
        }
        return address2;
    }

    /**
     * Method getAddress3.
     *
     * @return String
     */
    public String getAddress3() {
        if (address3 == null) {
            return "";
        }
        return address3;
    }

    /**
     * Method to concatenate the address fields into a single address
     *
     * @return
     */
    public String getFullAddress() {
        return ((address1 == null || "".equals(address1)) ? "" : address1)
                + ((address2 == null || "".equals(address2)) ? "" : "; "
                + address2)
                + ((address3 == null || "".equals(address3)) ? "" : "; "
                + address3);
    }

    /**
     * Method getAge.
     *
     * @return int
     */
    public int getAge() {
        return getAgeAt(null);
    }

    public int getAgeAt(Date date) {
        return iDARTUtil.getAgeAt(getDateOfBirth() == null ? new Date()
                : getDateOfBirth(), date);
    }

    /**
     * Method getCellphone.
     *
     * @return String
     */
    public String getCellphone() {
        if (cellphone == null) {
            return "";
        }
        return cellphone;
    }

    /**
     * Method getClinic.
     *
     * @return Clinic
     */
    public Integer getCurrentClinic() {
        return clinic;
    }

    /**
     * Method getDateOfBirth.
     *
     * @return Date
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Method getFirstNames.
     *
     * @return String
     */
    public String getFirstNames() {
        if (firstNames == null) {
            return "";
        }
        return firstNames;
    }

    /**
     * Method getHomePhone.
     *
     * @return String
     */
    public String getHomePhone() {
        if (homePhone == null) {
            return "";
        }
        return homePhone;
    }

    /**
     * Method getId.
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Method getLastname.
     *
     * @return String
     */
    public String getLastname() {
        if (lastname == null) {
            return "";
        }
        return lastname;
    }

    /**
     * Method getModified.
     *
     * @return char
     */
    public char getModified() {
        return modified;
    }

    /**
     * Method getNextOfKinName.
     *
     * @return String
     */
    public String getNextOfKinName() {
        if (nextOfKinName == null) {
            return "";
        }
        return nextOfKinName;
    }

    /**
     * Method getNextOfKinPhone.
     *
     * @return String
     */
    public String getNextOfKinPhone() {
        if (nextOfKinPhone == null) {
            return "";
        }
        return nextOfKinPhone;
    }

    /**
     * Method getPatientId.
     *
     * @return String
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Method getProvince.
     *
     * @return String
     */
    public String getProvince() {
        if (province == null) {
            return "";
        }
        return province;
    }

    /**
     * Method getRace.
     *
     * @return String
     */
    public String getRace() {
        if (race == null) {
            return "";
        }
        return race;
    }

    /**
     * Method getUuid.
     *
     * @return String
     */
    public String getUuid() {
        if (uuid == null) {
            return "";
        }
        return uuid;
    }

    /**
     * Method getSex.
     *
     * @return char
     */
    public char getSex() {
        return sex;
    }

    /**
     * Method getWorkPhone.
     *
     * @return String
     */
    public String getWorkPhone() {
        if (workPhone == null) {
            return "";
        }
        return workPhone;
    }

    /**
     * Method getClinicName.
     *
     * @return String
     */
    public String getClinicName() {
        if (clinicName == null) {
            return "";
        }
        return clinicName;
    }

    /**
     * Method getMainClinic.
     *
     * @return Clinic
     */
    public Integer getMainClinic() {
        return mainClinic;
    }

    /**
     * Method getMainClinicName.
     *
     * @return String
     */
    public String getMainClinicName() {
        if (mainClinicName == null) {
            return "";
        }
        return mainClinicName;
    }

    /**
     * Method getDataInicioTarv.
     *
     * @return String
     */
    public String getDataInicioTarv() {
        if (datainiciotarv == null) {
            return "";
        }
        return datainiciotarv;
    }

    /**
     * Method setAccountStatus.
     *
     * @param accountStatus Boolean
     */
    public void setAccountStatus(Boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    /**
     * Method setAddress1.
     *
     * @param address1 String
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * Method setAddress2.
     *
     * @param address2 String
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * Method setAddress3.
     *
     * @param address3 String
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     * Method setCellphone.
     *
     * @param cellphone String
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    /**
     * Method setClinic.
     *
     * @param clinic Clinic
     */
    public void setClinic(Integer clinic) {
        this.clinic = clinic;
    }

    /**
     * Method setDateOfBirth.
     *
     * @param dateOfBirth Date
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Method setFirstNames.
     *
     * @param firstNames String
     */
    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    /**
     * Method setHomePhone.
     *
     * @param homePhone String
     */
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    /**
     * Method setId.
     *
     * @param id int
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Method setLastname.
     *
     * @param lastname String
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Method setModified.
     *
     * @param modified char
     */
    public void setModified(char modified) {
        this.modified = modified;
    }

    /**
     * Method setNextOfKinName.
     *
     * @param nextOfKinName String
     */
    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    /**
     * Method setNextOfKinPhone.
     *
     * @param nextOfKinPhone String
     */
    public void setNextOfKinPhone(String nextOfKinPhone) {
        this.nextOfKinPhone = nextOfKinPhone;
    }

    /**
     * Method setPatientId.
     *
     * @param patientId String
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Method setProvince.
     *
     * @param province String
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Method setRace.
     *
     * @param race String
     */
    public void setRace(String race) {
        this.race = race;
    }

    /**
     * Method setUuid.
     *
     * @param uuid String
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Method setSex.
     *
     * @param sex char
     */
    public void setSex(char sex) {
        this.sex = sex;
    }

    /**
     * Method setWorkPhone.
     *
     * @param workPhone String
     */
    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    /**
     * Method setClinicName.
     *
     * @param clinicName
     */
    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    /**
     * Method setMainClinic.
     *
     * @param clinic
     */
    public void setMainClinic(Integer clinic) {
        this.mainClinic = clinic;
    }

    /**
     * Method setMainClinicName.
     *
     * @param mainClinicName
     */
    public void setMainClinicName(String mainClinicName) {
        this.mainClinicName = mainClinicName;
    }

    /**
     * Method setDataInicioTarv.
     *
     * @param datainiciotarv
     */
    public void setDataInicioTarv(String datainiciotarv) {
        this.datainiciotarv = datainiciotarv;
    }

    @Override
    public String toString() {
        return getFirstNames() + " " + getLastname();
    }
}
