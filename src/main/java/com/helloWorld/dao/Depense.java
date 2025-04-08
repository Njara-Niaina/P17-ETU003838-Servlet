package main.java.com.helloWorld.dao;

import java.util.Date;

public class Depense extends BaseModelDao {
    private int id_prevision;
    private String libelle;
    private double montant;
    private Date date_enregistrement;

    public Depense() {
    }

    public Depense(int id_prevision, String libelle, double montant, Date date_enregistrement) {
        this.id_prevision = id_prevision;
        this.libelle = libelle;
        this.montant = montant;
        this.date_enregistrement = date_enregistrement;
    }

    public int getId_prevision() {
        return id_prevision;
    }

    public void setId_prevision(int id_prevision) {
        this.id_prevision = id_prevision;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDate_enregistrement() {
        return date_enregistrement;
    }

    public void setDate_enregistrement(Date date_enregistrement) {
        this.date_enregistrement = date_enregistrement;
    }

    @Override
    public String toString() {
        return "Depense{" +
                "id=" + getId() +
                ", id_prevision=" + id_prevision +
                ", libelle='" + libelle + '\'' +
                ", montant=" + montant +
                ", date_enregistrement=" + date_enregistrement +
                '}';
    }
}