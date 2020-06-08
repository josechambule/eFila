package org.celllife.idart.database.hibernate;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;

@Entity
public class RegimeTerapeutico {

	@Id
	@GeneratedValue
	private int regimeid;
	private String regimeesquema;
	private boolean active;
	private String regimenomeespecificado;
	private String codigoregime;
	private String regimeesquemaidart;        
        /*
        * 
        * Removi 2 atributos: linhaT e regimenDrugs
        * Metodo para buscar regime terapeutico - Idart antigo 
        * Modified by : Colaco Nhongo
        * Modifica date: 14/01/2020
        */      
//    @ManyToOne
//    @JoinColumn(name = "linhaid")
//    private LinhaT linhaT;

    @OneToMany
	@JoinColumn(name = "regimen")
	@IndexColumn(name = "regimenDrugsIndex")
	@Cascade( { org.hibernate.annotations.CascadeType.ALL,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<RegimenDrugs> regimenDrugs;
    
	public String getRegimenomeespecificado() {
		return regimenomeespecificado;
	}

	public void setRegimenomeespecificado(String regimenomeespecificado) {
		this.regimenomeespecificado = regimenomeespecificado;
	}

	public String getCodigoregime() {
		return codigoregime;
	}

	public void setCodigoregime(String codigoregime) {
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
        
	public RegimeTerapeutico(int regimeid, String regimeesquema, boolean active,
                String regimenomeespecificado, String codigoregime,String regimeesquemaidart,String pediatrico) {
		super();
		this.regimeid = regimeid;
		this.regimeesquema = regimeesquema;
		this.active = active;
                this.codigoregime = codigoregime;
                this.regimeesquemaidart = regimeesquemaidart;
                this.regimenomeespecificado=regimenomeespecificado;
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

	public String getRegimeesquemaidart() {
		return regimeesquemaidart;
	}

	public void setRegimeesquemaidart(String regimeesquemaidart) {
		this.regimeesquemaidart = regimeesquemaidart;
	}
	
//    public LinhaT getLinhaT() {
//        return linhaT;
//    }

//    public void setLinhaT(LinhaT linhaT) {
//        this.linhaT = linhaT;
//    }

    /**
	 * Method getRegimenDrugs.
	 * @return List<RegimenDrugs>
	 */
	public List<RegimenDrugs> getRegimenDrugs() {
		return regimenDrugs;
	}

	/**
	 * Method setRegimenDrugs.
	 * @param regimenDrugs List<RegimenDrugs>
	 */
	public void setRegimenDrugs(List<RegimenDrugs> regimenDrugs) {
		this.regimenDrugs = regimenDrugs;
	}
        
    	/**
	 * Method equals.
         * @param regime
	 * @return boolean
	 */
	public boolean equals(RegimeTerapeutico regime) {
		boolean noMatch = false;
		if (this.getRegimenDrugs().size() == regime.getRegimenDrugs().size()) {
			for (Iterator<RegimenDrugs> iter = this.getRegimenDrugs()
					.iterator(); iter.hasNext();) {
				Drug currentDrug = (iter.next()).getDrug();
				for (Iterator<RegimenDrugs> iterator = regime
						.getRegimenDrugs().iterator(); iterator.hasNext();) {
					if ((iterator.next()).getDrug().equals(
							currentDrug))
						noMatch = true;
				}
				if (!noMatch)
					return false;
			}

			return true;

		}

		return false;
	}
        
	@Override
	public String toString() {
		return regimeesquema + " " + regimeesquemaidart;
	}
}
