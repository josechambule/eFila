package model.manager.reports;

public class MmiaRegimeTerapeutico {

    private String codigo;

    private String regimeTerapeutico;

    private String totalDoentes;

    private String totalDoentesFarmaciaComunitaria;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getRegimeTerapeutico() {
        return regimeTerapeutico;
    }

    public void setRegimeTerapeutico(String regimeTerapeutico) {
        this.regimeTerapeutico = regimeTerapeutico;
    }

    public String getTotalDoentes() {
        return totalDoentes;
    }

    public void setTotalDoentes(String totalDoentes) {
        this.totalDoentes = totalDoentes;
    }

    public String getTotalDoentesFarmaciaComunitaria() {
        return totalDoentesFarmaciaComunitaria;
    }

    public void setTotalDoentesFarmaciaComunitaria(String totalDoentesFarmaciaComunitaria) {
        this.totalDoentesFarmaciaComunitaria = totalDoentesFarmaciaComunitaria;
    }
}
