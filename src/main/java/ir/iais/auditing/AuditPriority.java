/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.iais.auditing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author leila
 */
public enum AuditPriority {
    HIGHEST("HIGHEST"),
    HIGH("HIGH"),
    MEDIUM("MEDIUM"),
    LOW("LOW"),
    LOWEST("LOWEST");

    private final String text;
    public static final Set<AuditPriority> ALL_PRIORITIES = new HashSet<>(Arrays.asList(AuditPriority.values()));

    private AuditPriority(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getPersianString() {
        switch (this) {
            case HIGHEST:
                return "خیلی بالا";
            case HIGH:
                return "بالا";
            case MEDIUM:
                return "متوسط";
            case LOW:
                return "پایین";
            case LOWEST:
                return "خیلی پایین";
            default:
                return "";
        }
    }

}
