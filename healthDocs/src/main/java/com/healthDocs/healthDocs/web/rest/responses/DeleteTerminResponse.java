package com.healthDocs.healthDocs.web.rest.responses;

public class DeleteTerminResponse {

	private Boolean deletionSuccessful;
	
	public DeleteTerminResponse(Boolean deletionSuccessful) {
		this.deletionSuccessful = deletionSuccessful;
	}

	public Boolean getDeletionSuccessful() {
		return deletionSuccessful;
	}

	public void setDeletionSuccessful(Boolean deletionSuccessful) {
		this.deletionSuccessful = deletionSuccessful;
	}
	
	
}
