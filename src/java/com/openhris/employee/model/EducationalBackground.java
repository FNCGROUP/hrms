/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.model;

/**
 *
 * @author jet
 */
public class EducationalBackground extends Employee {
    
        private String course;
	private String school;
	private String dateGraduated;

	public String getCourse(){ return course; }
	public void setCourse(String course){ this.course = course; }
	
	public String getSchool(){ return school; }
	public void setSchool(String school){ this.school = school; }
	
	public String getDateGraduated(){ return dateGraduated; }
	public void setDateGraduated(String dateGraduated){ this.dateGraduated = dateGraduated; }
    
}
