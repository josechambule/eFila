package model.manager.reports;

public class PrescricaoSemFilaXLS {

	private String patientIdentifier;
	
	private String nome;
	
	private String apelido;
	
	private String uuidOpenmrs;
	
	private String dataPrescricao;

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

	public String getUuidOpenmrs() {
		return uuidOpenmrs;
	}

	public void setUuidOpenmrs(String uuidOpenmrs) {
		this.uuidOpenmrs = uuidOpenmrs;
	}

	public String getDataPrescricao() {
		return dataPrescricao;
	}

	public void setDataPrescricao(String dataPrescricao) {
		this.dataPrescricao = dataPrescricao;
	}
}
