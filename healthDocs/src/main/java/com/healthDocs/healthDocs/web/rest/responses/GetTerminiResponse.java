package com.healthDocs.healthDocs.web.rest.responses;

import java.util.List;

import com.healthDocs.healthDocs.model.Termin;

public class GetTerminiResponse {
private List<Termin> termini;

public GetTerminiResponse(List<Termin> termini) {
	this.termini = termini;
}

public List<Termin> getTermini() {
	return termini;
}

public void setTermini(List<Termin> termini) {
	this.termini = termini;
}


}
