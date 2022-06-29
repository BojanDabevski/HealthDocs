package com.healthDocs.healthDocs.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity

public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToOne
    private User doctor;
    @OneToOne
    private User patient;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime dateAndTime;
    private String location;
    @Enumerated(value= EnumType.STRING)
    private TerminType type;
    public Termin(){

    }
    
    public TerminType getTerminType() {
        return type;
    }
    public Termin(User doctor, User patient, LocalDateTime dateAndTime, String location, TerminType type) {
        this.doctor = doctor;
        this.patient = patient;
        this.dateAndTime = dateAndTime;
        this.location = location;
        this.type=type;
    }

    public User getDoctor() {
        return doctor;
    }

    public User getPatient() {
        return patient;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public String getFormattedDateAndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.dateAndTime.format(formatter);
    }
    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

	public TerminType getType() {
		return type;
	}

	public void setType(TerminType type) {
		this.type = type;
	}

	public void setDoctor(User doctor) {
		this.doctor = doctor;
	}

	public void setPatient(User patient) {
		this.patient = patient;
	}
}