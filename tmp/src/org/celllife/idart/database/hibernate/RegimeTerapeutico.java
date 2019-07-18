package org.celllife.idart.database.hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RegimeTerapeutico {

	@Id
	@GeneratedValue
	private int regimeid;
	private String regimeesquema;
	private boolean active;
	private String regimenomeespecificado;
	private int codigoregime;

	public String getRegimenomeespecificado() {
		return regimenomeespecificado;
	}

	public void setRegimenomeespecificado(String regimenomeespecificado) {
		this.regimenomeespecificado = regimenomeespecificado;
	}

	public int getCodigoregime() {
		return codigoregime;
	}

	public void setCodigoregime(int codigoregime) {
		this.codigoregime = codigoregime;
	}

	public int getRegimeid() {
		return regimeid;
	}

	public void setRegimeid(int regimeid) {
		this.regimeid = regimeid;
	}

	public String getRegimeesquema() {
		return regimeesquema;
	}

	public void setRegimeesquema(String regimeesquema) {
		this.regimeesquema = regimeesquema;
	}

	public RegimeTerapeutico(int regimeid, String regimeesquema, boolean active) {
		super();
		this.regimeid = regimeid;
		this.regimeesquema = regimeesquema;
		this.active = active;
	}

	public RegimeTerapeutico() {
		super();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
