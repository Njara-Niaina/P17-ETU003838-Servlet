package main.java.com.helloWorld.dao;

public class EtatPrevision extends BaseModelDao {
    private String prevision_libelle;
    private double montant_prevu;
    private double montant_depense;
    private double montant_restant;

    public EtatPrevision() {
    }

    public EtatPrevision(String prevision_libelle, double montant_prevu, double montant_depense, double montant_restant) {
        this.prevision_libelle = prevision_libelle;
        this.montant_prevu = montant_prevu;
        this.montant_depense = montant_depense;
        this.montant_restant = montant_restant;
    }

    public String getPrevision_libelle() {
        return prevision_libelle;
    }

    public void setPrevision_libelle(String prevision_libelle) {
        this.prevision_libelle = prevision_libelle;
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

    @Override
    public String toString() {
        return "EtatPrevision{" +
                "id=" + getId() +
                ", prevision_libelle='" + prevision_libelle + '\'' +
                ", montant_prevu=" + montant_prevu +
                ", montant_depense=" + montant_depense +
                ", montant_restant=" + montant_restant +
                '}';
    }
}