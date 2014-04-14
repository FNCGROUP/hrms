/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

import com.hrms.dbconnection.GetSQLConnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class TradeNameBean {
    
    private String tradeName;
    private Integer corporateId;
    private String sssId;
    private String hdmfId;
    private String phicId;
    private String tinId;
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    public void getTradeContributionId(int id){
        Connection conn = getConnection.connection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT * FROM trade_table WHERE id = "+id+" ");
            while(rs.next()){
                sssId = rs.getString("sssNo");
                hdmfId = rs.getString("hdmfNo");
                phicId = rs.getString("phicNo");
                tinId = rs.getString("tinNo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setTradeName(String tradeName){ this.tradeName = tradeName; }
    public String getTradeName(){ return tradeName; }
    
    public void setCorporateId(int corporateId){ this.corporateId = corporateId; }
    public Integer getCorporateId(){ return corporateId; }
    
    public void setSssId(String sssId){ this.sssId = sssId; }
    public String getSssId(){ return sssId; }
    
    public void setHdmfId(String hdmfId){ this.hdmfId = hdmfId; }
    public String getHdmfId(){ return hdmfId; }
    
    public void setPhicId(String phicId){ this.phicId = phicId; }
    public String getPhicId(){ return phicId; }
    
    public void setTinId(String tinId){ this.tinId = tinId; }
    public String getTinId(){ return tinId; }
    
    public boolean updateTradeName(String tradeName, int tradeId){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String queryUpdate = " UPDATE trade_name SET name = ? WHERE id = ? ";
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, tradeName);
            pstmt.setInt(2, tradeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateSssIdNo(int rowId, String idNo){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String queryUpdate = " UPDATE trade_name SET sssNo = ? WHERE id = ? ";
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, idNo);
            pstmt.setInt(2, rowId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateHdmfIdNo(int rowId, String idNo){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String queryUpdate = " UPDATE trade_name SET hdmfNo = ? WHERE id = ? ";
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, idNo);
            pstmt.setInt(2, rowId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updatePhicIdNo(int rowId, String idNo){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String queryUpdate = " UPDATE trade_name SET phicNo = ? WHERE id = ? ";
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, idNo);
            pstmt.setInt(2, rowId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateTinIdNo(int rowId, String idNo){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String queryUpdate = " UPDATE trade_name SET tinNo = ? WHERE id = ? ";
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, idNo);
            pstmt.setInt(2, rowId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
