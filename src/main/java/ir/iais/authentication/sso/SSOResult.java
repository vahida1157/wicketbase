/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

import java.util.List;

/**
 * @author karam
 */
public class SSOResult {

    private Integer status;
    private List<String> message;
    private Object object;

    public SSOResult(Integer status, List<String> message, Object object) {
        this.status = status;
        this.message = message;
        this.object = object;
    }

    public Integer getStatus() {
        return status;
    }

    public List<String> getMessage() {
        return message;
    }

    public Object getObject() {
        return object;
    }
}
