package model.manager.reports;

public class SecondLinePatients {

	private String patientIdentifier;
	
	private String nome;
	
	private int idade;
	
	private String therapeuticScheme;
	
	private String line;
	
	private String artType;

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

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getTherapeuticScheme() {
		return therapeuticScheme;
	}

	public void setTherapeuticScheme(String therapeuticScheme) {
		this.therapeuticScheme = therapeuticScheme;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getArtType() {
		return artType;
	}

	public void setArtType(String artType) {
		this.artType = artType;
	}
}
