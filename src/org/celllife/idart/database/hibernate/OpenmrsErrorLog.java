package org.celllife.idart.database.hibernate;

import java.util.Date;

import javax.persistence.*;

@Entity
public class OpenmrsErrorLog {

	@Id
	@GeneratedValue
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "patient")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "prescription")
	private Prescription prescription;
	
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

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
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
