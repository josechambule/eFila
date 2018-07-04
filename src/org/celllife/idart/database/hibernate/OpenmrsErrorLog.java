package org.celllife.idart.database.hibernate;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OpenmrsErrorLog {

	@Id
	@GeneratedValue
	private Integer id;
	
	private Integer patient;
	
	private Integer prescription;
	
	private Date pickupdate;
	
	private Date returnpickupdate;
	
	private String errordescription;
	
	private Date datecreated;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPatient() {
		return patient;
	}

	public void setPatient(Integer patient) {
		this.patient = patient;
	}

	public Integer getPrescription() {
		return prescription;
	}

	public void setPrescription(Integer prescription) {
		this.prescription = prescription;
	}

	public Date getPickupdate() {
		return pickupdate;
	}

	public void setPickupdate(Date pickupdate) {
		this.pickupdate = pickupdate;
	}

	public Date getReturnpickupdate() {
		return returnpickupdate;
	}

	public void setReturnpickupdate(Date returnpickupdate) {
		this.returnpickupdate = returnpickupdate;
	}

	public String getErrordescription() {
		return errordescription;
	}

	public void setErrordescription(String errordescription) {
		this.errordescription = errordescription;
	}

	public Date getDatacreated() {
		return datecreated;
	}

	public void setDatacreated(Date datecreated) {
		this.datecreated = datecreated;
	}
}
