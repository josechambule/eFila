package org.celllife.idart.database.hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "systemfunctionality")
public class SystemFunctionality implements Comparable<SystemFunctionality>{

    public static final String ADMINISTRATION = "ADMINISTRATION";
    public static final String PACIENT_ADMINISTRATION = "PACIENT_ADMINISTRATION";
    public static final String STOCK_ADMINISTRATION = "STOCK_ADMINISTRATION";
    public static final String REPORTS = "REPORTS";

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "code")
    private String code;

    @ManyToMany(mappedBy = "sysFunctions")
    private Set<Role> roles;

    public SystemFunctionality() {
    }

    public SystemFunctionality(Integer id, String description, String code) {
        this.id = id;
        this.description = description;
        this.code = code;
    }

    public SystemFunctionality(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int compareTo(SystemFunctionality o) {
        return this.getCode().compareToIgnoreCase(o.getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SystemFunctionality)) return false;
        SystemFunctionality that = (SystemFunctionality) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, code);
    }
}
