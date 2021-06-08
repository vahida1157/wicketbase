package ir.iais.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author vahid
 * create on 6/6/2021
 */
@Entity
public class SSOToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Column(length = 3000)
    private String tokenValue;

    public SSOToken() {
    }

    public SSOToken(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
