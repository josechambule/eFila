package model.manager.reports;

public class PacienteReferidoXLS {

    private String nid;

    private String nome;

    private String idade;

    private String dataultimaPrescricao;

    private String regimaterapeutico;

    private String tipoDispensa;

    private String dataProximoLevantamento;

    private String datareferencia;

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getDataultimaPrescricao() {
        return dataultimaPrescricao;
    }

    public void setDataultimaPrescricao(String dataultimaPrescricao) {
        this.dataultimaPrescricao = dataultimaPrescricao;
    }

    public String getRegimaterapeutico() {
        return regimaterapeutico;
    }

    public void setRegimaterapeutico(String regimaterapeutico) {
        this.regimaterapeutico = regimaterapeutico;
    }

    public String getTipoDispensa() {
        return tipoDispensa;
    }

    public void setTipoDispensa(String tipoDispensa) {
        this.tipoDispensa = tipoDispensa;
    }

    public String getDataProximoLevantamento() {
        return dataProximoLevantamento;
    }

    public void setDataProximoLevantamento(String dataProximoLevantamento) {
        this.dataProximoLevantamento = dataProximoLevantamento;
    }

    public String getDatareferencia() {
        return datareferencia;
    }

    public void setDatareferencia(String datareferencia) {
        this.datareferencia = datareferencia;
    }

    public String getFarmaciaReferencia() {
        return farmaciaReferencia;
    }

    public void setFarmaciaReferencia(String farmaciaReferencia) {
        this.farmaciaReferencia = farmaciaReferencia;
    }

    private String farmaciaReferencia;
}
