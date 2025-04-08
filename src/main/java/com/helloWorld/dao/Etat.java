package main.java.com.helloWorld.dao;

import java.util.Date;

public class Etat extends BaseModelDao {
    private int id_prevision;
    private double montant_prevu;
    private double montant_depense;
    private double montant_restant;
    private Date date_mise_a_jour;

    public Etat() {
    }

    public Etat(int id_prevision, double montant_prevu, double montant_depense, double montant_restant, Date date_mise_a_jour) {
        this.id_prevision = id_prevision;
        this.montant_prevu = montant_prevu;
        this.montant_depense = montant_depense;
        this.montant_restant = montant_restant;
        this.date_mise_a_jour = date_mise_a_jour;
    }

    public int getId_prevision() {
        return id_prevision;
    }

    public void setId_prevision(int id_prevision) {
        this.id_prevision = id_prevision;
    }

    public double getMontant_prevu() {
        return montant_prevu;
    }

    public void setMontant_prevu(double montant_prevu) {
        this.montant_prevu = montant_prevu;
    }

    public double getMontant_depense() {
        return montant_depense;
    }

    public void setMontant_depense(double montant_depense) {
        this.montant_depense = montant_depense;
    }

    public double getMontant_restant() {
        return montant_restant;
    }

    public void setMontant_restant(double montant_restant) {
        this.montant_restant = montant_restant;
    }

    public Date getDate_mise_a_jour() {
        return date_mise_a_jour;
    }

    public void setDate_mise_a_jour(Date date_mise_a_jour) {
        this.date_mise_a_jour = date_mise_a_jour;
    }

    @Override
    public String toString() {
        return "Etat{" +
                "id=" + getId() +
                ", id_prevision=" + id_prevision +
                ", montant_prevu=" + montant_prevu +
                ", montant_depense=" + montant_depense +
                ", montant_restant=" + montant_restant +
                ", date_mise_a_jour=" + date_mise_a_jour +
                '}';
    }
}