package org.celllife.idart.database.hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role implements Comparable<Role>{

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "code")
    private String code;

    @ManyToMany(cascade = { CascadeType.PERSIST })
    @JoinTable(
            name = "rolefunction",
            joinColumns = { @JoinColumn(name = "roleid") },
            inverseJoinColumns = { @JoinColumn(name = "functionid") }
    )
    private Set<SystemFunctionality> sysFunctions;

    public Role() {
        super();
    }

    public Role(String description, String code, Set<SystemFunctionality> sysFunctions) {
        this.description = description;
        this.code = code;
        this.sysFunctions = sysFunctions;
    }

    /**
     * @param id
     * @param description
     * @param code
     */
    public Role(Integer id, String description, String code) {
        super();
        this.id = id;
        this.description = description;
        this.code = code;
    }

    public Set<SystemFunctionality> getSysFunctions() {
        return sysFunctions;
    }

    public void setSysFunctions(Set<SystemFunctionality> sysFunctions) {
        this.sysFunctions = sysFunctions;
    }

    @Override
    public int compareTo(Role o) {
        return this.getCode().compareToIgnoreCase(o.getCode());
    }

    /**
     * @return id
     */
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
}
