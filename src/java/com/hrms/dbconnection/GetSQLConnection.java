/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.dbconnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class GetSQLConnection {
    
    Credentials credentials = new Credentials();
    
    public Connection connection(){
        Connection conn;
        try {
            InputStream input = this.getClass().getResourceAsStream("/com/openhris/resources/config.properties");
            Properties prop = new Properties();
            prop.load(input);
            
//            System.out.println("username: "+prop.getProperty("dbusername"));
//            System.out.println("password: "+prop.getProperty("dbpassword"));
//            System.out.println("dburl: "+prop.getProperty("dburl"));
//            System.out.println("driver: "+prop.getProperty("dbdriver"));
            
//            Class.forName(credentials.getDriver()).newInstance();
//            conn = DriverManager.getConnection(credentials.getUrl(), credentials.getUsername(), credentials.getPassword());
            
            Class.forName(prop.getProperty("dbdriver")).newInstance();
            conn = DriverManager.getConnection(prop.getProperty("dburl"), prop.getProperty("dbusername"), prop.getProperty("dbpassword"));
            
            return conn;
        } catch (InstantiationException ex) {
            Logger.getLogger(GetSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GetSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(GetSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(GetSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
