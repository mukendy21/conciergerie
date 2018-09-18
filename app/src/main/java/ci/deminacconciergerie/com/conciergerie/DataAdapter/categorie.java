package ci.deminacconciergerie.com.conciergerie.DataAdapter;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class categorie extends RealmObject{

    @PrimaryKey
    private String id;
    private String imageUrl;
    private String nom;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
