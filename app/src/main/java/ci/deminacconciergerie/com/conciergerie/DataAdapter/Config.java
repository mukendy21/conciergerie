package ci.deminacconciergerie.com.conciergerie.DataAdapter;

public class Config {


    //Data URL
    //public static final String DATA_URL = "http://10.100.61.14/deminac/test.php";
    public static final String DATA_URL = "http://deminacconciergerie.com/api/categorie/read.php?token=PtUxgwmz4j&id=";


    //JSON TAGS CATEGORIE
    public static final String TAG_IMAGE_URL = "img_url";
    public static final String TAG_NOM = "nom";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_ID_DESCRIPTION = "id";



    //Date URL
    //public static final String DATA_URL_LISTE_SERVICE = "http://deminac-api.herokuapp.com/service/read_categorie.php?token=PtUxgwmz4j&id_categorie=";
    public static final String DATA_URL_LISTE_SERVICE = "http://deminacconciergerie.com/api/service/read_categorie.php?token=PtUxgwmz4j&id_categorie=";
    //JSON TAGS SERVICE
    public static final String TAG_LIBELLE_SERVICE = "libelle";
    public static final String TAG_ID_LIBELLE = "id";




    //http://deminacconciergerie.com/api


    //public static final String DATA_URL_LISTE_COMMANDE ="http://deminac-api.herokuapp.com/commande/read_client.php?token=PtUxgwmz4j&id_client=";
    public static final String DATA_URL_LISTE_COMMANDE ="http://deminacconciergerie.com/api/commande/read_client.php?token=PtUxgwmz4j&id_client=";
    public static final String TAG_ID = "id";
    public static final String TAG_LIBELLE_COMMANDE = "libelle";
    public static final String TAG_DATE_PRESTATTION_COMMANDE = "date_prestation";
    public static final String TAG_BUDGET_COMMANDE = "budget";
    public static final String TAG_ETAT_COMMANDE = "etat";

}
