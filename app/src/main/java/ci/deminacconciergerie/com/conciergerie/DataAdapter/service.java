package ci.deminacconciergerie.com.conciergerie.DataAdapter;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class service extends RealmObject{

    @PrimaryKey
    private String id;
    private String id_categorie;
    private String libelle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(String id_categorie) {
        this.id_categorie = id_categorie;
    }
}
