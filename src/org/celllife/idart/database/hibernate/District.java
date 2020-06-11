package org.celllife.idart.database.hibernate;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Table(name = "district")
public class District {

    @Id
    private Integer id;

    private String name;

    private String code;

    private String uuid;

    @ManyToOne
    @JoinColumn(name = "province")
    public Province province;


    public District() {
        super();
    }

    /**
     * Constructor for SimpleDomain.
     * @param name String
     * @param code String
     */
    public District(String name, String code) {
        this.name = name;
        this.code = code;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
