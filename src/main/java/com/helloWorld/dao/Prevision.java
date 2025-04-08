package main.java.com.helloWorld.dao;

public class Prevision extends BaseModelDao {
    private String libelle;
    private double montant;

    public Prevision() {
    }

    public Prevision(String libelle, double montant) {
        this.libelle = libelle;
        this.montant = montant;
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

    @Override
    public String toString() {
        return "Prevision{" +
                "id=" + getId() +
                ", libelle='" + libelle + '\'' +
                ", montant=" + montant +
                '}';
    }
}