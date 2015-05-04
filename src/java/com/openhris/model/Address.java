/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

/**
 *
 * @author jet
 */
public class Address extends Employee{
    
    private int addressId;
    private String type;
    private String street;
    private String city;
    private String province;
    private String zipCode;    

    public int getAddressId(){ return addressId; }
    public void setAddressId(int addressId){ this.addressId = addressId; }
    
    public String getType(){ return type; }
    public void setType(String type){ this.type = type; }
	
    public String getStreet(){ return street; }
    public void setStreet(String street){ this.street = street; }
	
    public String getCity(){ return city; }
    public void setCity(String city){ this.city = city; }
	
    public String getProvince(){ return province; }
    public void setProvince(String province){ this.province = province; }
	
    public String getZipCode(){ return zipCode; }
    public void setZipcode(String zipCode){ this.zipCode = zipCode; }
    
}
