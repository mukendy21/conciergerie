package ci.deminacconciergerie.com.conciergerie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ci.deminacconciergerie.com.conciergerie.DataAdapter.AdapterService;
import ci.deminacconciergerie.com.conciergerie.DataAdapter.Config;
import ci.deminacconciergerie.com.conciergerie.DataAdapter.service;
import io.realm.Realm;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Services extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnScrollChangeListener{

    private Realm realm;

    private List<service> serviceList;


    ProgressBar progressBar;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    private ImageView close_activity_service;

    //Volley Request Queue
    private RequestQueue requestQueue;


    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;


    String getId_previous_activity=null;

    private TextView txt_categorie;
    String title_categorie;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);


        Realm.init(this);
        realm = Realm.getDefaultInstance();


        close_activity_service = (ImageView)findViewById(R.id.close_activity_service);

        txt_categorie = (TextView)findViewById(R.id.txt_categorie_service);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);


        close_activity_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Services.this , Principale.class));
                finish();
            }
        });


        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
           getId_previous_activity = (String) bd.get("id");
            title_categorie = (String) bd.get("categorie");
        }

        //&Toast.makeText(this, ""+getId_previous_activity, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(this, ""+title_categorie, Toast.LENGTH_SHORT).show();

        txt_categorie.setText(title_categorie.toLowerCase());
        Typeface myTypeface = Typeface.createFromAsset(getAssets() , "coneria.ttf");
        txt_categorie.setTypeface(myTypeface);



        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_service);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //Initializing our service list
        serviceList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);




        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(Services.this);


        //initializing our adapter
        adapter = new AdapterService(serviceList, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);



        //Calling method to get data to fetch data
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            getData();


        }else {

            List<service> results = realm.where(service.class).equalTo("id_categorie", getId_previous_activity).findAll();
            serviceList.addAll(results);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        }



    }



    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL_LISTE_SERVICE + getId_previous_activity,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);

                        //Toast.makeText(Services.this, ""+Config.DATA_URL_LISTE_SERVICE+getId_previous_activity, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(Services.this, "Pas de services trouvés", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }



    //This method will get data from the web api
    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
    }


    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the service object
            service service = new service();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the service object
                service.setId(json.getString(Config.TAG_ID_LIBELLE));
                service.setLibelle(json.getString(Config.TAG_LIBELLE_SERVICE));
                service.setId_categorie(getId_previous_activity);


                realm.beginTransaction();
                realm.copyToRealmOrUpdate(service);
                realm.commitTransaction();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the service object to the list
            serviceList.add(service);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }



    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
           // getData();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
