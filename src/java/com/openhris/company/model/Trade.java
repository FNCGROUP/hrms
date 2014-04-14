/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.company.model;

/**
 *
 * @author jet
 */
public class Trade extends Company {
    
        private String tradeName;
	private int tradeId;
	private String sssId;
	private String hdmfId;
	private String phicId;
	private String tinId;
	
	public String getTradeName(){ return tradeName; }
	public void setTradeName(String tradeName){ this.tradeName = tradeName; }
	
	public int getTradeId(){ return tradeId; }
	public void setTradeId(int tradeId){ this.tradeId = tradeId; }
	
	public String getSssId(){ return sssId; }
	public void setSssId(String sssId){ this.sssId = sssId; }
	
	public String getHdmfId(){ return hdmfId; }
	public void setHdmfId(String hdmfId){ this.hdmfId = hdmfId; }
	
	public String getPhicId(){ return phicId; }
	public void setPhicId(String phicId){ this.phicId = phicId; }
	
	public String getTinId(){ return tinId; }
	public void setTinId(String tinId){ this.tinId = tinId; }
    
}
