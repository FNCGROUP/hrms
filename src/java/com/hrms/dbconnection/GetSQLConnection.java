/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
            Class.forName(credentials.getDriver()).newInstance();
            conn = DriverManager.getConnection(credentials.getUrl(), credentials.getUsername(), credentials.getPassword());
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
        }
    }
    
}
