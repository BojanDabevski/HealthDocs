package com.healthDocs.healthDocs.web.rest.responses;

import com.healthDocs.healthDocs.model.Termin;

public class CreateReceptResponse {
    private String amount;
    private String nameOfDrug;
    private String genericNameOfDrug;
    private String nalog;
    private String upat;
    private String error;
    
    public CreateReceptResponse(String amount,String nameOfDrug,String genericNameOfDrug,String nalog,String upat) {
    	this.amount = amount;
    	this.nameOfDrug = nameOfDrug;
    	this.genericNameOfDrug = genericNameOfDrug;
    	this.nalog = nalog;
    	this.upat = upat;
    }

	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getNameOfDrug() {
		return nameOfDrug;
	}
	public void setNameOfDrug(String nameOfDrug) {
		this.nameOfDrug = nameOfDrug;
	}
	public String getGenericNameOfDrug() {
		return genericNameOfDrug;
	}
	public void setGenericNameOfDrug(String genericNameOfDrug) {
		this.genericNameOfDrug = genericNameOfDrug;
	}
	public String getNalog() {
		return nalog;
	}
	public void setNalog(String nalog) {
		this.nalog = nalog;
	}
	public String getUpat() {
		return upat;
	}
	public void setUpat(String upat) {
		this.upat = upat;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
    
}
