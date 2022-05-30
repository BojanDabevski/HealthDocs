package com.healthDocs.healthDocs.service;

import java.util.List;

import com.healthDocs.healthDocs.model.PendingPatient;
import com.healthDocs.healthDocs.model.User;

public interface PendingPatientService {
List<PendingPatient> findAll();
void deleteById(Long Id);
void createPendingPatient(String EMBG, String username, String password, String firstName, String lastName, Boolean insurance);
User approvePendingPatient(Long Id);
}
