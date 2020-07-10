package model.manager.reports;

public class HistoricoLevantamentoXLS {
	
	private String patientIdentifier;
	
	private String nome;
	
	private String apelido;
	
	private String tipoTarv;
	
	private String regimeTerapeutico;
	
	private String tipoDispensa;
	
	private String dataLevantamento;
	
	private String dataProximoLevantamento;

	private String clinic;

	public String getPatientIdentifier() {
		return patientIdentifier;
	}
	
	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getTipoTarv() {
		return tipoTarv;
	}

	public void setTipoTarv(String tipoTarv) {
		this.tipoTarv = tipoTarv;
	}

	public String getRegimeTerapeutico() {
		return regimeTerapeutico;
	}

	public void setRegimeTerapeutico(String regimeTerapeutico) {
		this.regimeTerapeutico = regimeTerapeutico;
	}

	public String getTipoDispensa() {
		return tipoDispensa;
	}

	public void setTipoDispensa(String tipoDispensa) {
		this.tipoDispensa = tipoDispensa;
	}

	public String getDataLevantamento() {
		return dataLevantamento;
	}

	public void setDataLevantamento(String dataLevantamento) {
		this.dataLevantamento = dataLevantamento;
	}

	public String getDataProximoLevantamento() {
		return dataProximoLevantamento;
	}

	public void setDataProximoLevantamento(String dataProximoLevantamento) {
		this.dataProximoLevantamento = dataProximoLevantamento;
	}
	public String getClinic() {
		return clinic;
	}

	public void setClinic(String clinic) {
		this.clinic = clinic;
	}
}
