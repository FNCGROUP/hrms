/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

/**
 *
 * @author jetdario
 */
public class Allowances extends Employee {
    
    private double communication;
    private String comEntryType;
    private double perDiem;
    private String perDiemEntryType;
    private double cola;
    private String colaEntryType;
    private double meal;
    private String mealEntryType;
    private double transportation;
    private String transEntryType;
    private double others;
    private String othersEntryType;
    private double allowanceForLiquidation;

    public double getCommunication() {
        return communication;
    }

    public String getComEntryType() {
        return comEntryType;
    }

    public double getPerDiem() {
        return perDiem;
    }

    public String getPerDiemEntryType() {
        return perDiemEntryType;
    }

    public double getCola() {
        return cola;
    }

    public String getColaEntryType() {
        return colaEntryType;
    }

    public double getMeal() {
        return meal;
    }

    public String getMealEntryType() {
        return mealEntryType;
    }

    public double getTransportation() {
        return transportation;
    }

    public String getTransEntryType() {
        return transEntryType;
    }

    public double getOthers() {
        return others;
    }

    public String getOthersEntryType() {
        return othersEntryType;
    }

    public double getAllowanceForLiquidation() {
        return allowanceForLiquidation;
    }

    public void setCommunication(double communication) {
        this.communication = communication;
    }

    public void setComEntryType(String comEntryType) {
        this.comEntryType = comEntryType;
    }

    public void setPerDiem(double perDiem) {
        this.perDiem = perDiem;
    }

    public void setPerDiemEntryType(String perDiemEntryType) {
        this.perDiemEntryType = perDiemEntryType;
    }

    public void setCola(double cola) {
        this.cola = cola;
    }

    public void setColaEntryType(String colaEntryType) {
        this.colaEntryType = colaEntryType;
    }

    public void setMeal(double meal) {
        this.meal = meal;
    }

    public void setMealEntryType(String mealEntryType) {
        this.mealEntryType = mealEntryType;
    }

    public void setTransportation(double transportation) {
        this.transportation = transportation;
    }

    public void setTransEntryType(String transEntryType) {
        this.transEntryType = transEntryType;
    }

    public void setOthers(double others) {
        this.others = others;
    }

    public void setOthersEntryType(String othersEntryType) {
        this.othersEntryType = othersEntryType;
    }

    public void setAllowanceForLiquidation(double allowanceForLiquidation) {
        this.allowanceForLiquidation = allowanceForLiquidation;
    }
    
}
