/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.utilities;

import com.hrms.classes.SalaryComputation;
import com.hrms.modules.EmployeesInfoModule;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class ConvertionUtilities implements Serializable {
    
    public Double convertStringToDouble(String num){
        Double val = null;
        if(num != null){
            val = Double.parseDouble(num);
        }
        return val;
    }
    
    public Double formatValue(Double val){
        double formattedValue;
        DecimalFormat formatter = new DecimalFormat( "#,###,##0.00" );
        String newValue = formatter.format(val);
        formattedValue = Double.parseDouble(newValue);
        return formattedValue;
    }
    
    public String formatValueWithComma(Double val){
        DecimalFormat formatter = new DecimalFormat( "#,###,##0.00" );
        String formattedValue = null;
        if(val != null){
            formattedValue = formatter.format(val);
        }
        return formattedValue;
    }
    
    public String convertDateFormat(String date){
        DateFormat currentDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateConvert = null;
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeesInfoModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;
    }
    
    public String convertDateFormatWithTime(String date){
        DateFormat currentDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateConvert = null;
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeesInfoModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;
    }
    
    public String convertDateFormatYearMonth(String date){
        DateFormat currentDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM");
        String dateConvert = null;
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeesInfoModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;
    }
    
    public Integer convertBooleanValue(String value){
        Integer val = 0;
        if(value.equals("true")){
            val = 1;
        }else{
            val = 0;
        }
        return val;
    }
    
    public String removeCommaFromString(String val){
        String regex = "(?<=\\d),(?=\\d)";

        String output = val.replaceAll(regex, "");
        return output;
    }
    
    public Boolean checkInputIfDouble(String num){
        boolean result = false;
        try{
            Double val = Double.parseDouble(num);
            result = true;
        }catch (Exception e){
            e.getMessage();
        }        
        return result;
    }
    
    public Date parsingDate(String str){
        Date dateToFormat = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateToFormat = (Date) formatter.parse(str);
        } catch (ParseException ex) {
            Logger.getLogger(ConvertionUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateToFormat;
    }
    
    public Date parsingDateWithTime(String str){
        Date dateToFormat = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateToFormat = (Date) formatter.parse(str);
        } catch (ParseException ex) {
            Logger.getLogger(ConvertionUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateToFormat;
    }
    
    public Boolean convertStringToBoolean(String string){
        return Boolean.parseBoolean(string);
    }
    
    public Boolean checkInputIfInteger(String num){
        boolean result = false;
        try{
            Integer val = Integer.parseInt(num);
            result = true;
        }catch (Exception e){
            e.getMessage();
        }        
        return result;
    }    
    
}
