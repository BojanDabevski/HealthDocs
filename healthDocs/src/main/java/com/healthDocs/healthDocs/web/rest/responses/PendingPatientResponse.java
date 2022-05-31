package com.healthDocs.healthDocs.web.rest.responses;

public class PendingPatientResponse {
	private String EMBG;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean insurance;
    
    public PendingPatientResponse(String EMBG,String username,String password,String firstName, String lastName, Boolean insurance) {
    	this.EMBG = EMBG;
    	this.username = username;
    	this.password = password;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.insurance = insurance;
    }

	public String getEMBG() {
		return EMBG;
	}

	public void setEMBG(String eMBG) {
		EMBG = eMBG;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getInsurance() {
		return insurance;
	}

	public void setInsurance(Boolean insurance) {
		this.insurance = insurance;
	}
    
}
