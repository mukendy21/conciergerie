package ci.deminacconciergerie.com.conciergerie.DataAdapter;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class commande extends RealmObject {

    @PrimaryKey
    private String id;
    private String id_client;
    private String id_service;
    private String date_prestation;
    private String  budget;
    private String etat;
    private String libelle;


    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getId_service() {
        return id_service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId_service(String id_service) {
        this.id_service = id_service;
    }

    public String getDate_prestation() {
        return date_prestation;
    }

    public void setDate_prestation(String date_prestation) {
        this.date_prestation = date_prestation;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }


    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }
}
