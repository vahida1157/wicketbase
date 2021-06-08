/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.authentication.sso;

/**
 * @author saeed
 */
public class SSOException extends RuntimeException {

    public SSOException() {
    }

    public SSOException(String message) {
        super(message);
    }

}
