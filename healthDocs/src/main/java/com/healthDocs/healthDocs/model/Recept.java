package com.healthDocs.healthDocs.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//- model recepta(doza, ime na lek, genericko ime na lek, nalog,upat…)(ist princip na termin)
@Data
@Entity
public class Recept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToOne
    private User doctor;
    @OneToOne
    private User patient;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime dateAndTime;
    private String amount;
    private String nameOfDrug;
    private String genericNameOfDrug;
    private String nalog;
    private String upat;

    public Recept() {

    }

    public Recept(User doctor, User patient, LocalDateTime dateAndTime, String amount, String nameOfDrug, String genericNameOfDrug, String nalog, String upat) {
        this.doctor = doctor;
        this.patient = patient;
        this.dateAndTime = dateAndTime;
        this.amount = amount;
        this.nameOfDrug = nameOfDrug;
        this.genericNameOfDrug = genericNameOfDrug;
        this.nalog = nalog;
        this.upat = upat;

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
}
