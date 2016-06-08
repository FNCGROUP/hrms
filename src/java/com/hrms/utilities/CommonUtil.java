/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.utilities;

import com.hrms.modules.EmployeesInfoModule;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class CommonUtil {
    
    
    public static boolean checkInputIfDouble(String num){
        boolean result = false;
        try{
            Double val = Double.parseDouble(num);
            result = true;
        }catch (Exception e){
            e.getMessage();
        }        
        return result;
    }
    
    public static double convertStringToDouble(String num){
        double val = 0;
        try{
            if(num != null){
                val = Double.parseDouble(num);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        
        return val;
    }
    
    public static int convertStringToInteger(String num){
        int val = 0;
        try{
            if(num != null){
                val = Integer.parseInt(num);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        
        return val;
    }
    
    public static double formatNumValueWithComma(double val){
        double formattedValue;
        DecimalFormat formatter = new DecimalFormat( "#,###,##0.00" );
        String newValue = formatter.format(val);
        formattedValue = Double.parseDouble(newValue);
        return formattedValue;
    }
    
    public static String convertDateFormatForTableData(String date){
        DateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateConvert = "2009-08-24";
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;
    }
    
    /**
     * 
     * @param date = EEE MMM dd HH:mm:ss z yyyy     * 
     * @return formattedDate = yyyy-MM-dd
     */    
    public static String convertDateFormat(String date){
        DateFormat currentDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateConvert = "2009-08-24";
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;
    }
    
    /**
     * 
     * @param date = EEE MMM dd HH:mm:ss z yyyy
     * @return return formattedDate = yyyy-MM-dd HH:mm:ss
     */
    public static String convertDateFormatWithTime(String date){
        DateFormat currentDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateConvert = null;
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;
    }
    
    /**
     * 
     * @param date = EEE MMM dd HH:mm:ss z yyyy
     * @return formattedDate = yyyy-MM
     */
    public static String convertDateFormatYearMonth(String date){
        DateFormat currentDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM");
        String dateConvert = null;
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;
    }
    
    /**
     * 
     * @param date = EEE MMM dd HH:mm:ss z yyyy
     * @return formatted date September 1978
     */    
    public static String convertDateFormatForCalendar(String date){
	DateFormat currentDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("MMMMM yyyy");
        String dateConvert = null;
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;    
    }
    
    /**
     * 
     * @param value BooleanString
     * @return Integer 1 || 0
     */
    public static int convertBooleanValueToInt(String value){
        int val = 0;
        if(value.equals("true")){
            val = 1;
        }else{
            val = 0;
        }
        return val;
    }
    
    /**
     * 
     * @param val String with comma
     * @return String value w/out comma
     */
    public static String removeCommaFromString(String val){
        String regex = "(?<=\\d),(?=\\d)";
        String output = val.replaceAll(regex, "");
        return output;
    }
    
    /**
     * 
     * @param date = yyyy-MM-dd
     * @return parsed String to Date
     */
    public static Date parsingDate(String date){
        Date dateToFormat = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(date != null){
                dateToFormat = (Date) formatter.parse(date);
            }
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateToFormat;
    }
    
    /**
     * 
     * @param date = yyyy-MM-dd HH:mm:ss
     * @return parsed String to Date
     */
    public Date parsingDateWithTime(String date){
        Date dateToFormat = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateToFormat = (Date) formatter.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateToFormat;
    }
    
    public static boolean convertStringToBoolean(String string){
        if(string.equals("true")){
            return true;
        } else {
            return false;
        }        
    }
    
    public static Boolean checkInputIfInteger(String num){
        boolean result = false;
        try{
            Integer val = Integer.parseInt(num);
            result = true;
        }catch (Exception e){
            e.getMessage();
        }        
        return result;
    }
    
    /**
     * 
     * @param date format EEE - MMM dd, yyyy
     * @return 
     */
    public static Date parseDateWithDifferentFormat(String date) throws ParseException{
        DateFormat currentDateFormat = new SimpleDateFormat("EEE - MMM dd, yyyy");
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateConvert = null;
        try {
            Date newDate = currentDateFormat.parse(date);
            dateConvert = newDateFormat.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return currentDateFormat.parse(dateConvert);
    }
    
    public static double roundOffToTwoDecimalPlaces(double amount){
        DecimalFormat df = new DecimalFormat("##0.00");
        return new Double(df.format(amount));
    }
    
    public static boolean checkForDuplicatedBranch(String str){
        boolean result = false;
        if(str.equals("boracay") || str.equals("head office") || str.equals("eastwood") || str.equals("baguio") 
                || str.equals("fairview") || str.equals("sta. lucia") || str.equals("on-call and trainees") || 
                str.equals("on-call and trainees (boracay)") || str.equals("on-call and trainees (head office)") || 
                str.equals("on-call and trainees (eastwood)") || str.equals("on-call and trainees (baguio)") || 
                str.equals("on-call and trainees (fairview)") || str.equals("on-call and trainees (sta. lucia)")){
            result = true;
        }
        return result;
    }

    public static String replaceSingleQuote(String str){
        return str.replace("'", "\\'");
    }
}
