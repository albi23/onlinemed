package com.onlinemed.model.dto;

/**
 * A class that maps data related to drug info
 */
public record DrugInfo(String drugName, String urlEquivalent, String drugForm, String dose,
                       String box, String payment, String patientPrice, String fullPrice) {
}
