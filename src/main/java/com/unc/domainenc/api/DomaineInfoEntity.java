package com.unc.domainenc.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DomaineInfoEntity {
    private String nom;
    private String extension;
    private String gestionnaire;
    private String beneficiaire;
    private List<String> dns;
    private String dateCreation;
    private String dateExpiration;
    private Integer nbDaysBeforeExpires;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public List<String> getDns() {
        return dns;
    }

    public void setDns(List<String> dns) {
        this.dns = dns;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Integer getNbDaysBeforeExpires() {
        return nbDaysBeforeExpires;
    }

    public void setNbDaysBeforeExpires(Integer nbDaysBeforeExpires) {
        this.nbDaysBeforeExpires = nbDaysBeforeExpires;
    }
}
