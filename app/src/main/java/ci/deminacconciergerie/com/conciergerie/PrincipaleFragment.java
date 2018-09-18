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
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

import ci.deminacconciergerie.com.conciergerie.DataAdapter.CardAdapter;
import ci.deminacconciergerie.com.conciergerie.DataAdapter.Config;
import ci.deminacconciergerie.com.conciergerie.DataAdapter.User;
import ci.deminacconciergerie.com.conciergerie.DataAdapter.categorie;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */

@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(api = Build.VERSION_CODES.M)
public class PrincipaleFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnScrollChangeListener{


    private final String TAG = "TAG";

    private Realm realm;

    //Création d'une liste de service de catégorie
    private List<categorie> categorieList;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    
    private TextView text_retry;
    private Button btn_retry;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;



     ProgressBar progressBar;


    public PrincipaleFragment() {
        // Required empty public constructor
    }






    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_principale, container, false);


        Realm.init(getContext());

        realm = Realm.getDefaultInstance();


        btn_retry = (Button)v.findViewById(R.id.btn_retry);
        text_retry = (TextView)v.findViewById(R.id.text_retry);
        
        

        progressBar = (ProgressBar)v.findViewById(R.id.progressBar1);

        //Initializing Views
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);


        //Initializing our categorie list
        categorieList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(v.getContext());



        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(PrincipaleFragment.this);

        //initializing our adapter
        adapter = new CardAdapter(categorieList, v.getContext());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);


        //Calling method to get data to fetch data
        ConnectivityManager manager = (ConnectivityManager)container.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            getData();
        }else {

            List<categorie> results = realm.where(categorie.class).findAll();
            categorieList.addAll(results);
            Log.v(TAG, categorieList.size() + "");
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
        
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });



        return v;
    }



    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        getActivity().setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response

                        Log.v(TAG, "response = " + response.toString());
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                       // Toast.makeText(getContext(), "Pas d'articles disponibles", Toast.LENGTH_SHORT).show();
                       // btn_retry.setVisibility(View.VISIBLE);
                        //text_retry.setVisibility(View.VISIBLE);
                        //text_retry.setText(text_retry.getText().toString().toUpperCase());

                        new MaterialDialog.Builder(getContext())
                                .title("Internet")
                                .content(R.string.not_load_data)
                                .positiveText(getResources().getString(R.string.yes))

                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        //Are you sure you want to log out?
                                        getData();


                                    }
                                })  .show();
                        /*Snackbar snackbar = Snackbar
                                .make(progressBar, R.string.internet,  Snackbar.LENGTH_LONG);
                        snackbar.show();*/
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
            //Creating the superhero object
            categorie categorie = new categorie();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                categorie.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                categorie.setNom(json.getString(Config.TAG_NOM));
                categorie.setDescription(json.getString(Config.TAG_DESCRIPTION));
                categorie.setId(json.getString(Config.TAG_ID_DESCRIPTION));

                // Suppression
                /*categorie cat = realm.where(categorie.class).equalTo("id", 1).findFirst();
                cat.deleteFromRealm();*/

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(categorie);
                realm.commitTransaction();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the categorie object to the list
            categorieList.add(categorie);
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
            //getData();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
