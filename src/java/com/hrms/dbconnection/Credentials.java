/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.dbconnection;

/**
 *
 * @author jet
 */
public class Credentials {
    
    private String driver = "com.mysql.jdbc.Driver";
//    private String driver = "org.postgresql.Driver"
    private String url = "jdbc:mysql://localhost:3306/fnc_hris_db";
//    private String url = "jdbc:mysql://192.168.1.199:3306/fnc_hris_db";
//    private String url = "jdbc:postgresql://localhost:5432/ancom2013"
//    private String url = "jdbc:mysql://ec2-174-129-38-223.compute-1.amazonaws.com/3306/hrms";
    private String username = "fnc_hris";
    private String password = "fncgroups09";
    
    public String getDriver(){
        return driver;
    }
    
    public String getUrl(){
        return url;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
}
