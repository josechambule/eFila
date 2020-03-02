/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.celllife.idart.database.hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author colaco
 */
@Entity
@Table(name = "sync_temp_dispense")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SyncTempDispense.findAll", query = "SELECT s FROM SyncTempDispense s")
    , @NamedQuery(name = "SyncTempDispense.findById", query = "SELECT s FROM SyncTempDispense s WHERE s.id = :id")
    , @NamedQuery(name = "SyncTempDispense.findByClinicalstage", query = "SELECT s FROM SyncTempDispense s WHERE s.clinicalstage = :clinicalstage")
    , @NamedQuery(name = "SyncTempDispense.findByCurrent", query = "SELECT s FROM SyncTempDispense s WHERE s.current = :current")
    , @NamedQuery(name = "SyncTempDispense.findByDate", query = "SELECT s FROM SyncTempDispense s WHERE s.date = :date")
    , @NamedQuery(name = "SyncTempDispense.findByDoctor", query = "SELECT s FROM SyncTempDispense s WHERE s.doctor = :doctor")
    , @NamedQuery(name = "SyncTempDispense.findByDuration", query = "SELECT s FROM SyncTempDispense s WHERE s.duration = :duration")
    , @NamedQuery(name = "SyncTempDispense.findByModified", query = "SELECT s FROM SyncTempDispense s WHERE s.modified = :modified")
    , @NamedQuery(name = "SyncTempDispense.findByPatient", query = "SELECT s FROM SyncTempDispense s WHERE s.patient = :patient")
    , @NamedQuery(name = "SyncTempDispense.findBySyncTempDispenseid", query = "SELECT s FROM SyncTempDispense s WHERE s.syncTempDispenseid = :syncTempDispenseid")
    , @NamedQuery(name = "SyncTempDispense.findByWeight", query = "SELECT s FROM SyncTempDispense s WHERE s.weight = :weight")
    , @NamedQuery(name = "SyncTempDispense.findByReasonforupdate", query = "SELECT s FROM SyncTempDispense s WHERE s.reasonforupdate = :reasonforupdate")
    , @NamedQuery(name = "SyncTempDispense.findByNotes", query = "SELECT s FROM SyncTempDispense s WHERE s.notes = :notes")
    , @NamedQuery(name = "SyncTempDispense.findByEnddate", query = "SELECT s FROM SyncTempDispense s WHERE s.enddate = :enddate")
    , @NamedQuery(name = "SyncTempDispense.findByDrugtypes", query = "SELECT s FROM SyncTempDispense s WHERE s.drugtypes = :drugtypes")
    , @NamedQuery(name = "SyncTempDispense.findByRegimeid", query = "SELECT s FROM SyncTempDispense s WHERE s.regimeid = :regimeid")
    , @NamedQuery(name = "SyncTempDispense.findByDatainicionoutroservico", query = "SELECT s FROM SyncTempDispense s WHERE s.datainicionoutroservico = :datainicionoutroservico")
    , @NamedQuery(name = "SyncTempDispense.findByMotivomudanca", query = "SELECT s FROM SyncTempDispense s WHERE s.motivomudanca = :motivomudanca")
    , @NamedQuery(name = "SyncTempDispense.findByLinhaid", query = "SELECT s FROM SyncTempDispense s WHERE s.linhaid = :linhaid")
    , @NamedQuery(name = "SyncTempDispense.findByPpe", query = "SELECT s FROM SyncTempDispense s WHERE s.ppe = :ppe")
    , @NamedQuery(name = "SyncTempDispense.findByPtv", query = "SELECT s FROM SyncTempDispense s WHERE s.ptv = :ptv")
    , @NamedQuery(name = "SyncTempDispense.findByTb", query = "SELECT s FROM SyncTempDispense s WHERE s.tb = :tb")
    , @NamedQuery(name = "SyncTempDispense.findByTpi", query = "SELECT s FROM SyncTempDispense s WHERE s.tpi = :tpi")
    , @NamedQuery(name = "SyncTempDispense.findByTpc", query = "SELECT s FROM SyncTempDispense s WHERE s.tpc = :tpc")
    , @NamedQuery(name = "SyncTempDispense.findByDispensatrimestral", query = "SELECT s FROM SyncTempDispense s WHERE s.dispensatrimestral = :dispensatrimestral")
    , @NamedQuery(name = "SyncTempDispense.findByTipodt", query = "SELECT s FROM SyncTempDispense s WHERE s.tipodt = :tipodt")
    , @NamedQuery(name = "SyncTempDispense.findByGaac", query = "SELECT s FROM SyncTempDispense s WHERE s.gaac = :gaac")
    , @NamedQuery(name = "SyncTempDispense.findByAf", query = "SELECT s FROM SyncTempDispense s WHERE s.af = :af")
    , @NamedQuery(name = "SyncTempDispense.findByCa", query = "SELECT s FROM SyncTempDispense s WHERE s.ca = :ca")
    , @NamedQuery(name = "SyncTempDispense.findByCcr", query = "SELECT s FROM SyncTempDispense s WHERE s.ccr = :ccr")
    , @NamedQuery(name = "SyncTempDispense.findBySaaj", query = "SELECT s FROM SyncTempDispense s WHERE s.saaj = :saaj")
    , @NamedQuery(name = "SyncTempDispense.findByFr", query = "SELECT s FROM SyncTempDispense s WHERE s.fr = :fr")
    , @NamedQuery(name = "SyncTempDispense.findByAmountpertime", query = "SELECT s FROM SyncTempDispense s WHERE s.amountpertime = :amountpertime")
    , @NamedQuery(name = "SyncTempDispense.findByDispensedate", query = "SELECT s FROM SyncTempDispense s WHERE s.dispensedate = :dispensedate")
    , @NamedQuery(name = "SyncTempDispense.findByDrugname", query = "SELECT s FROM SyncTempDispense s WHERE s.drugname = :drugname")
    , @NamedQuery(name = "SyncTempDispense.findByExpirydate", query = "SELECT s FROM SyncTempDispense s WHERE s.expirydate = :expirydate")
    , @NamedQuery(name = "SyncTempDispense.findByPatientid", query = "SELECT s FROM SyncTempDispense s WHERE s.patientid = :patientid")
    , @NamedQuery(name = "SyncTempDispense.findByPatientfirstname", query = "SELECT s FROM SyncTempDispense s WHERE s.patientfirstname = :patientfirstname")
    , @NamedQuery(name = "SyncTempDispense.findByPatientlastname", query = "SELECT s FROM SyncTempDispense s WHERE s.patientlastname = :patientlastname")
    , @NamedQuery(name = "SyncTempDispense.findByDateexpectedstring", query = "SELECT s FROM SyncTempDispense s WHERE s.dateexpectedstring = :dateexpectedstring")
    , @NamedQuery(name = "SyncTempDispense.findByPickupdate", query = "SELECT s FROM SyncTempDispense s WHERE s.pickupdate = :pickupdate")
    , @NamedQuery(name = "SyncTempDispense.findByTimesperday", query = "SELECT s FROM SyncTempDispense s WHERE s.timesperday = :timesperday")
    , @NamedQuery(name = "SyncTempDispense.findByWeekssupply", query = "SELECT s FROM SyncTempDispense s WHERE s.weekssupply = :weekssupply")
    , @NamedQuery(name = "SyncTempDispense.findByQtyinhand", query = "SELECT s FROM SyncTempDispense s WHERE s.qtyinhand = :qtyinhand")
    , @NamedQuery(name = "SyncTempDispense.findBySummaryqtyinhand", query = "SELECT s FROM SyncTempDispense s WHERE s.summaryqtyinhand = :summaryqtyinhand")
    , @NamedQuery(name = "SyncTempDispense.findByQtyinlastbatch", query = "SELECT s FROM SyncTempDispense s WHERE s.qtyinlastbatch = :qtyinlastbatch")})
public class SyncTempDispense implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "clinicalstage")
    private Integer clinicalstage;
    @Column(name = "current")
    private Character current;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "doctor")
    private Integer doctor;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "modified")
    private Character modified;
    @Basic(optional = false)
    @Column(name = "patient")
    private int patient;
    @Column(name = "sync_temp_dispenseid")
    private String syncTempDispenseid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "weight")
    private Double weight;
    @Column(name = "reasonforupdate")
    private String reasonforupdate;
    @Column(name = "notes")
    private String notes;
    @Column(name = "enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enddate;
    @Column(name = "drugtypes")
    private String drugtypes;
    @Column(name = "regimeid")
    private String regimeid;
    @Column(name = "datainicionoutroservico")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainicionoutroservico;
    @Column(name = "motivomudanca")
    private String motivomudanca;
    @Column(name = "linhaid")
    private String linhaid;
    @Column(name = "ppe")
    private Character ppe;
    @Column(name = "ptv")
    private Character ptv;
    @Column(name = "tb")
    private Character tb;
    @Column(name = "tpi")
    private Character tpi;
    @Column(name = "tpc")
    private Character tpc;
    @Basic(optional = false)
    @Column(name = "dispensatrimestral")
    private int dispensatrimestral;
    @Column(name = "tipodt")
    private String tipodt;
    @Column(name = "gaac")
    private Character gaac;
    @Column(name = "af")
    private Character af;
    @Column(name = "ca")
    private Character ca;
    @Column(name = "ccr")
    private Character ccr;
    @Column(name = "saaj")
    private Character saaj;
    @Column(name = "fr")
    private Character fr;
    @Column(name = "amountpertime")
    private String amountpertime;
    @Column(name = "dispensedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dispensedate;
    @Column(name = "drugname")
    private String drugname;
    @Column(name = "expirydate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirydate;
    @Column(name = "patientid")
    private String patientid;
    @Column(name = "patientfirstname")
    private String patientfirstname;
    @Column(name = "patientlastname")
    private String patientlastname;
    @Column(name = "dateexpectedstring")
    private String dateexpectedstring;
    @Column(name = "pickupdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickupdate;
    @Column(name = "timesperday")
    private Integer timesperday;
    @Column(name = "weekssupply")
    private Integer weekssupply;
    @Column(name = "qtyinhand")
    private String qtyinhand;
    @Column(name = "summaryqtyinhand")
    private String summaryqtyinhand;
    @Column(name = "qtyinlastbatch")
    private String qtyinlastbatch;

    public SyncTempDispense() {
    }

    public SyncTempDispense(Integer id) {
        this.id = id;
    }

    public SyncTempDispense(Integer id, int patient, int dispensatrimestral) {
        this.id = id;
        this.patient = patient;
        this.dispensatrimestral = dispensatrimestral;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClinicalstage() {
        return clinicalstage;
    }

    public void setClinicalstage(Integer clinicalstage) {
        this.clinicalstage = clinicalstage;
    }

    public Character getCurrent() {
        return current;
    }

    public void setCurrent(Character current) {
        this.current = current;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDoctor() {
        return doctor;
    }

    public void setDoctor(Integer doctor) {
        this.doctor = doctor;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Character getModified() {
        return modified;
    }

    public void setModified(Character modified) {
        this.modified = modified;
    }

    public int getPatient() {
        return patient;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }

    public String getSyncTempDispenseid() {
        return syncTempDispenseid;
    }

    public void setSyncTempDispenseid(String syncTempDispenseid) {
        this.syncTempDispenseid = syncTempDispenseid;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getReasonforupdate() {
        return reasonforupdate;
    }

    public void setReasonforupdate(String reasonforupdate) {
        this.reasonforupdate = reasonforupdate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getDrugtypes() {
        return drugtypes;
    }

    public void setDrugtypes(String drugtypes) {
        this.drugtypes = drugtypes;
    }

    public String getRegimeid() {
        return regimeid;
    }

    public void setRegimeid(String regimeid) {
        this.regimeid = regimeid;
    }

    public Date getDatainicionoutroservico() {
        return datainicionoutroservico;
    }

    public void setDatainicionoutroservico(Date datainicionoutroservico) {
        this.datainicionoutroservico = datainicionoutroservico;
    }

    public String getMotivomudanca() {
        return motivomudanca;
    }

    public void setMotivomudanca(String motivomudanca) {
        this.motivomudanca = motivomudanca;
    }

    public String getLinhaid() {
        return linhaid;
    }

    public void setLinhaid(String linhaid) {
        this.linhaid = linhaid;
    }

    public Character getPpe() {
        return ppe;
    }

    public void setPpe(Character ppe) {
        this.ppe = ppe;
    }

    public Character getPtv() {
        return ptv;
    }

    public void setPtv(Character ptv) {
        this.ptv = ptv;
    }

    public Character getTb() {
        return tb;
    }

    public void setTb(Character tb) {
        this.tb = tb;
    }

    public Character getTpi() {
        return tpi;
    }

    public void setTpi(Character tpi) {
        this.tpi = tpi;
    }

    public Character getTpc() {
        return tpc;
    }

    public void setTpc(Character tpc) {
        this.tpc = tpc;
    }

    public int getDispensatrimestral() {
        return dispensatrimestral;
    }

    public void setDispensatrimestral(int dispensatrimestral) {
        this.dispensatrimestral = dispensatrimestral;
    }

    public String getTipodt() {
        return tipodt;
    }

    public void setTipodt(String tipodt) {
        this.tipodt = tipodt;
    }

    public Character getGaac() {
        return gaac;
    }

    public void setGaac(Character gaac) {
        this.gaac = gaac;
    }

    public Character getAf() {
        return af;
    }

    public void setAf(Character af) {
        this.af = af;
    }

    public Character getCa() {
        return ca;
    }

    public void setCa(Character ca) {
        this.ca = ca;
    }

    public Character getCcr() {
        return ccr;
    }

    public void setCcr(Character ccr) {
        this.ccr = ccr;
    }

    public Character getSaaj() {
        return saaj;
    }

    public void setSaaj(Character saaj) {
        this.saaj = saaj;
    }

    public Character getFr() {
        return fr;
    }

    public void setFr(Character fr) {
        this.fr = fr;
    }

    public String getAmountpertime() {
        return amountpertime;
    }

    public void setAmountpertime(String amountpertime) {
        this.amountpertime = amountpertime;
    }

    public Date getDispensedate() {
        return dispensedate;
    }

    public void setDispensedate(Date dispensedate) {
        this.dispensedate = dispensedate;
    }

    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public Date getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(Date expirydate) {
        this.expirydate = expirydate;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getPatientfirstname() {
        return patientfirstname;
    }

    public void setPatientfirstname(String patientfirstname) {
        this.patientfirstname = patientfirstname;
    }

    public String getPatientlastname() {
        return patientlastname;
    }

    public void setPatientlastname(String patientlastname) {
        this.patientlastname = patientlastname;
    }

    public String getDateexpectedstring() {
        return dateexpectedstring;
    }

    public void setDateexpectedstring(String dateexpectedstring) {
        this.dateexpectedstring = dateexpectedstring;
    }

    public Date getPickupdate() {
        return pickupdate;
    }

    public void setPickupdate(Date pickupdate) {
        this.pickupdate = pickupdate;
    }

    public Integer getTimesperday() {
        return timesperday;
    }

    public void setTimesperday(Integer timesperday) {
        this.timesperday = timesperday;
    }

    public Integer getWeekssupply() {
        return weekssupply;
    }

    public void setWeekssupply(Integer weekssupply) {
        this.weekssupply = weekssupply;
    }

    public String getQtyinhand() {
        return qtyinhand;
    }

    public void setQtyinhand(String qtyinhand) {
        this.qtyinhand = qtyinhand;
    }

    public String getSummaryqtyinhand() {
        return summaryqtyinhand;
    }

    public void setSummaryqtyinhand(String summaryqtyinhand) {
        this.summaryqtyinhand = summaryqtyinhand;
    }

    public String getQtyinlastbatch() {
        return qtyinlastbatch;
    }

    public void setQtyinlastbatch(String qtyinlastbatch) {
        this.qtyinlastbatch = qtyinlastbatch;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SyncTempDispense)) {
            return false;
        }
        SyncTempDispense other = (SyncTempDispense) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.celllife.idart.database.hibernate.SyncTempDispense[ id=" + id + " ]";
    }

}
