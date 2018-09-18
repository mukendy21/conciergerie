package ci.deminacconciergerie.com.conciergerie;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import ci.deminacconciergerie.com.conciergerie.DataAdapter.AdapterCommande;
import ci.deminacconciergerie.com.conciergerie.DataAdapter.Config;
import ci.deminacconciergerie.com.conciergerie.DataAdapter.User;
import ci.deminacconciergerie.com.conciergerie.DataAdapter.commande;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(api = Build.VERSION_CODES.M)
public class CommandeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnScrollChangeListener{


    private Realm realm;

    User user;

    private List<commande> commandeList;




    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    private TextView text_firt_nothing;
    private TextView text_nothing;
    private TextView btn_noting;


    //Volley Request Queue
    private RequestQueue requestQueue;


    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;


    ProgressBar progressBar;


    public CommandeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_commande, container, false);

        Realm.init(getContext());

        realm = Realm.getDefaultInstance();

        user = realm.where(User.class).findFirst();


        progressBar = (ProgressBar)v.findViewById(R.id.progressBar3);

        text_firt_nothing = (TextView) v.findViewById(R.id.image_nothing);
        text_nothing = (TextView)v.findViewById(R.id.text_nothing);
        btn_noting = (Button)v.findViewById(R.id.btn_nothing);

        String error_commande = "Aucune commande trouvée";

        text_nothing.setText(error_commande.toString().toUpperCase());


        btn_noting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Principale.class));
                getActivity().finish();
            }
        });

        //Initializing Views
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_commandes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        //Initializing our commande list
        commandeList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());

        //Calling method to get data to fetch data
       // getData();


        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(CommandeFragment.this);


        //initializing our adapter
        adapter = new AdapterCommande(commandeList, getContext());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


        ConnectivityManager manager = (ConnectivityManager)container.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            getData();
        }else {
            List<commande> results = realm.where(commande.class).equalTo("id_client", user.getId()).findAll();
            commandeList.addAll(results);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        }



        return v;

    }





    String id;

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar


        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        getActivity().setProgressBarIndeterminateVisibility(true);


        //récupérer l'id de l'utilisateur
        id = user.getId().toString();

        //Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL_LISTE_COMMANDE + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(getActivity(), "Pas de services trouvés", Toast.LENGTH_SHORT).show();
                        text_firt_nothing.setVisibility(View.VISIBLE);
                        btn_noting.setVisibility(View.VISIBLE);
                        text_nothing.setVisibility(View.VISIBLE);

                    }
                });

        //Returning the reqimage_nothinguest
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
            //Creating the superhero object
            commande commande = new commande();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                commande.setId(json.getString(Config.TAG_ID));
                commande.setDate_prestation(json.getString(Config.TAG_DATE_PRESTATTION_COMMANDE));
                commande.setBudget(json.getString(Config.TAG_BUDGET_COMMANDE));
                commande.setEtat(json.getString(Config.TAG_ETAT_COMMANDE));
                commande.setLibelle(json.getString(Config.TAG_LIBELLE_COMMANDE));
                commande.setId_client(user.getId());

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(commande);
                realm.commitTransaction();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            commandeList.add(commande);
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
