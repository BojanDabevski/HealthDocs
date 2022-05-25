package com.healthDocs.healthDocs.model;

import lombok.Data;

import javax.persistence.*;

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
    @OneToOne
    private Termin termin;
    private String amount;
    private String nameOfDrug;
    private String genericNameOfDrug;
    private String nalog;
    private String upat;
    public Recept(){

    }
    public Recept(User doctor, User patient,Termin termin,String amount,String nameOfDrug,String genericNameOfDrug,String nalog, String upat)
    {
        this.doctor = doctor;
        this.patient = patient;
        this.termin = termin;
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
    public Termin getTermin() {return termin;}

    public void setId(Long id) {
        Id = id;
    }

    public String getAmount() {
        return amount;
    }

    public String getNameOfDrug() {
        return nameOfDrug;
    }

    public String getGenericNameOfDrug() {
        return genericNameOfDrug;
    }

    public String getNalog() {
        return nalog;
    }

    public String getUpat() {
        return upat;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setNameOfDrug(String nameOfDrug) {
        this.nameOfDrug = nameOfDrug;
    }

    public void setGenericNameOfDrug(String genericNameOfDrug) {
        this.genericNameOfDrug = genericNameOfDrug;
    }

    public void setNalog(String nalog) {
        this.nalog = nalog;
    }

    public void setUpat(String upat) {
        this.upat = upat;
    }



}
