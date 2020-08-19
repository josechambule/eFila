package org.celllife.idart.database.hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;


@Entity
@Table(name = "country")
public class Country {

    @Id
    private Integer id;

    private String name;

    private String code;

    private String uuid;


    public Country() {
        super();
    }

    /**
     * Constructor for SimpleDomain.
     * @param name String
     * @param code String
     */
    public Country(String name, String code) {
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
