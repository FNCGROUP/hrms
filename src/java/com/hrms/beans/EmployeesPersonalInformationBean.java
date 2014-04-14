/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

import com.hrms.dbconnection.GetSQLConnection;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class EmployeesPersonalInformationBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    private String employeeId;
    private InputStream picture;
    private String dob;
    private String pob;
    private String gender;
    private String citizenship;
    private Double height;
    private Double weight;
    private String civilStatus;
    private String religion;
    private String spouseName;
    private String spouseOccupation;
    private String spouseOfficeAddress;
    private String fathersName;
    private String fathersOccupation;
    private String mothersName;
    private String mothersOccupation;
    private String parentsAddress;
    private String dialectSpeakWrite;
    private String contactPerson;
    private String skills;
    private String hobby;
    
    public void setEmployeeId(String employeeId){ this.employeeId  = employeeId; }
    public String getemployeeId(){ return employeeId; }
    
    public void setPicture(InputStream picture){ this.picture = picture; }
    public InputStream getPicture(){ return picture; }
    
    public void setDob(String dob){ this.dob = dob; }
    public String getDob(){ return dob; }
    
    public void setPob(String pob){ this.pob = pob; }
    public String getPob(){ return pob; }
    
    public void setGender(String gender){ this.gender = gender; }
    public String getGender(){ return gender; }
    
    public void setCitizenship(String citizenship){ this.citizenship = citizenship; }
    public String getCitizenship(){ return citizenship; }
    
    public void setHeight(Double height){ this.height = height; }
    public Double getHeight(){ return height; }
    
    public void setWeight(Double weight){ this.weight = weight; }
    public Double getWeight(){ return weight; }
    
    public void setCivilStatus(String civilStatus){ this.civilStatus = civilStatus; }
    public String getCivilStatus(){ return civilStatus; }
    
    public void setReligion(String religion){ this.religion = religion; }
    public String getReligion(){ return religion; }
    
    public void setSpouseName(String spouseName){ this.spouseName = spouseName; }
    public String getSpouseName(){ return spouseName; }
    
    public void setSpouseOccupation(String spouseOccupation){ this.spouseOccupation = spouseOccupation; }
    public String getSpouseOccupation(){ return spouseOccupation; }
    
    public void setSpouseOfficeAddress(String spouseOfficeAddress){ this.spouseOfficeAddress = spouseOfficeAddress; }
    public String getSpouseOfficeAddress(){ return spouseOfficeAddress; }
    
    public void setFathersName(String fathersName){ this.fathersName = fathersName; }
    public String getFathersName(){ return fathersName; }
    
    public void setFathersOccupation(String fathersOccupation){ this.fathersOccupation = fathersOccupation; }
    public String getFathersOccupation(){ return fathersOccupation; }
    
    public void setMothersName(String mothersName){ this.mothersName = mothersName; }
    public String getMothersName(){ return mothersName; }
    
    public void setMothersOccupation(String mothersOccupation){ this.mothersOccupation = mothersOccupation; }
    public String getMothersOccupation(){ return mothersOccupation; }
    
    public void setParentsAddress(String parentsAddress){ this.parentsAddress = parentsAddress; }
    public String getParentsAddress(){ return parentsAddress; }

    public void setDialectSpeakWrite(String dialectSpeakWrite){ this.dialectSpeakWrite = dialectSpeakWrite; }
    public String getDialectSpeakWrite(){ return dialectSpeakWrite; }
    
    public void setContactPerson(String contactPerson){ this.contactPerson = contactPerson; }
    public String getContactPerson(){ return contactPerson; }
    
    public void setSkills(String skills){ this.skills = skills; }
    public String getSkills(){ return skills; }
    
    public void setHobby(String hobby){ this.hobby = hobby; }
    public String getHobby(){ return hobby; }
        
    public Boolean insertEmployeePersonalInformation(String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        Statement stmt;
        PreparedStatement pstmt = null;
        ResultSet rs;
        
        String queryInsert = "INSERT INTO employee_personal_info(employeeId, dob, pob, gender, citizenship, height, weight, civilStatus, religion, "
                + "spouseName, spouseOccupation, spouseOfficeAddress, fathersName, fathersOccupation, mothersName, mothersOccupation, parentsAddress, "
                + "dialectSpeakWrite, contactPerson, skills, hobby) VALUE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(queryInsert);
            pstmt.setString(1, id);
            pstmt.setString(2, dob);
            pstmt.setString(3, pob.toLowerCase());
            pstmt.setString(4, gender);
            pstmt.setString(5, citizenship);
            pstmt.setDouble(6, height);
            pstmt.setDouble(7, weight);
            pstmt.setString(8, civilStatus.toLowerCase());
            pstmt.setString(9, religion.toLowerCase());
            pstmt.setString(10, spouseName.toLowerCase());
            pstmt.setString(11, spouseOccupation.toLowerCase());
            pstmt.setString(12, spouseOfficeAddress.toLowerCase());
            pstmt.setString(13, fathersName.toLowerCase());
            pstmt.setString(14, fathersOccupation.toLowerCase());
            pstmt.setString(15, mothersName.toLowerCase());
            pstmt.setString(16, mothersOccupation.toLowerCase());
            pstmt.setString(17, parentsAddress.toLowerCase());
            pstmt.setString(18, dialectSpeakWrite.toLowerCase());
            pstmt.setString(19, contactPerson.toLowerCase());
            pstmt.setString(20, skills.toLowerCase());
            pstmt.setString(21, hobby.toLowerCase());
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesPersonalInformationBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if (conn != null && !conn.isClosed()) {
                    pstmt.close();
                    conn.close();                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesPersonalInformationBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        return result;
    }
    
    public Boolean updateEmployeePersonalInformation(String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt = null;
        
        String queryInsert = "UPDATE employee_personal_info SET dob = ?, pob = ?, gender = ?, citizenship = ?, height = ?, weight = ?, "
                + "civilStatus = ?, religion = ?, spouseName = ?, spouseOccupation = ?, spouseOfficeAddress = ?, fathersName = ?, "
                + "fathersOccupation = ?, mothersName = ?, mothersOccupation = ?, parentsAddress = ?, dialectSpeakWrite = ?, "
                + "contactPerson = ?, skills = ?, hobby = ? WHERE employeeId = ? ";
        try {
            pstmt = conn.prepareStatement(queryInsert);            
            pstmt.setString(1, dob);
            pstmt.setString(2, pob.toLowerCase());
            pstmt.setString(3, gender);
            pstmt.setString(4, citizenship);
            pstmt.setDouble(5, height);
            pstmt.setDouble(6, weight);
            pstmt.setString(7, civilStatus.toLowerCase());
            pstmt.setString(8, religion.toLowerCase());
            pstmt.setString(9, spouseName.toLowerCase());
            pstmt.setString(10, spouseOccupation.toLowerCase());
            pstmt.setString(11, spouseOfficeAddress.toLowerCase());
            pstmt.setString(12, fathersName.toLowerCase());
            pstmt.setString(13, fathersOccupation.toLowerCase());
            pstmt.setString(14, mothersName.toLowerCase());
            pstmt.setString(15, mothersOccupation.toLowerCase());
            pstmt.setString(16, parentsAddress.toLowerCase());
            pstmt.setString(17, dialectSpeakWrite.toLowerCase());
            pstmt.setString(18, contactPerson.toLowerCase());
            pstmt.setString(19, skills.toLowerCase());
            pstmt.setString(20, hobby.toLowerCase());
            pstmt.setString(21, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesPersonalInformationBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if (conn != null && !conn.isClosed()) {
                    pstmt.close();
                    conn.close();                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeesPersonalInformationBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean getEmployeePersonalInformation(String id){
        Connection conn = getConnection.connection();
        Statement stmt;
        ResultSet rs;
        Boolean hasRows = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_personal_info WHERE employeeId = '"+id+"' ");
            while(rs.next()){
                hasRows = true;
                dob = rs.getString("dob");                
                pob = rs.getString("pob");
                gender = rs.getString("gender");
                citizenship = rs.getString("citizenship");
                
                if(rs.getString("height") == null){
                    height = 0.0;
                }else{
                    height = Double.parseDouble(rs.getString("height"));
                }
                
                if(rs.getString("weight") == null){
                    weight = 0.0;
                }else{
                    weight = Double.parseDouble(rs.getString("weight"));
                }                
                
                civilStatus = rs.getString("civilStatus");
                religion = rs.getString("religion");
                spouseName = rs.getString("spouseName");
                spouseOccupation = rs.getString("spouseOccupation");
                spouseOfficeAddress = rs.getString("spouseOfficeAddress");
                fathersName = rs.getString("fathersName");
                fathersOccupation = rs.getString("fathersOccupation");
                mothersName = rs.getString("mothersName");
                mothersOccupation = rs.getString("mothersOccupation");
                parentsAddress = rs.getString("parentsAddress");
                dialectSpeakWrite = rs.getString("dialectSpeakWrite");
                contactPerson = rs.getString("contactPerson");
                skills = rs.getString("skills");
                hobby = rs.getString("hobby");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeesPersonalInformationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return hasRows;
    }
}
