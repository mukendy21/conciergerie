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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(api = Build.VERSION_CODES.M)
public class Principale extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    Realm realm;


   /* //Creating a List of superheroes
    private List<categorie> categorieList;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;*/




    TextView text_email_connect;
    TextView text_etat_connecting;


    User user;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        realm = Realm.getDefaultInstance();
         user = realm.where(User.class).findFirst();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();







        Intent intent = getIntent();
        Bundle info = intent.getExtras();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final View headerLayout = navigationView.getHeaderView(0);
        text_email_connect = (TextView)headerLayout.findViewById(R.id.text_email_connect);
        text_etat_connecting = (TextView)headerLayout.findViewById(R.id.text_etat_connecting);


        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();


        if(networkInfo != null && networkInfo.isConnected()){
            text_etat_connecting.setText(R.string.connect);

        }else {
            text_etat_connecting.setText(R.string.disconnect);
        }

       // String getEmail = null;
         if(info != null){
            /* getEmail = (String)info.get("userEmail");

             user = new User();
             user.setEmail(getEmail);
             realm.beginTransaction();
             realm.copyToRealm(user);
             realm.commitTransaction();
             text_email_connect.setText(user.getEmail());*/
        }

       // text_email_connect.setText(user.getEmail());

        text_email_connect.setText(user.getEmail());

        navigationView.setNavigationItemSelectedListener(this);




        setTitle(R.string.nos_services);
        PrincipaleFragment fragment = new PrincipaleFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment , "fragment1");
        fragmentTransaction.commit();


       /* //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //Initializing our categorie list
        categorieList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method to get data to fetch data
        getData();

        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(Principale.this);

        //initializing our adapter
        adapter = new CardAdapter(categorieList, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);*/




    }

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
   /* private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + String.valueOf(requestCount),
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
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(Principale.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            categorieList.add(categorie);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }*/





    //This method would check that the recyclerview scroll has reached the bottom or not
   /* private boolean isLastItemDisplaying(RecyclerView recyclerView) {
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
    }*/





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principale, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            new MaterialDialog.Builder(Principale.this)
                    .title(R.string.logout)
                    .content(R.string.logout_long)
                    .positiveText(getBaseContext().getResources().getString(R.string.yes))
                    .negativeText(getBaseContext().getResources().getString(R.string.no))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            //Are you sure you want to log out?

                            realm.beginTransaction();
                            realm.delete(User.class);
                            realm.commitTransaction();

                            finish();
                            startActivity(new Intent(Principale.this, Login.class));

                        }
                    })


                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_services) {
            // Handle the camera action

            setTitle(R.string.nos_services);
            PrincipaleFragment fragment = new PrincipaleFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment , "fragment1");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_commandes) {

           // startActivity(new Intent(Principale.this , Commandes.class));

            setTitle(R.string.mes_commandes);
            CommandeFragment fragment = new CommandeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment , "fragment2");
            fragmentTransaction.commit();

        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } */else if (id == R.id.nav_account) {

            setTitle(R.string.mon_compte);
            CompteFragment fragment = new CompteFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment , "fragment3");
            fragmentTransaction.commit();
        }else if(id == R.id.nav_disconnect){

            new MaterialDialog.Builder(Principale.this)
                    .title(R.string.logout)
                    .content(R.string.logout_long)
                    .positiveText(getBaseContext().getResources().getString(R.string.yes))
                    .negativeText(getBaseContext().getResources().getString(R.string.no))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            //Are you sure you want to log out?

                            realm.beginTransaction();
                            realm.delete(User.class);
                            realm.commitTransaction();

                            finish();
                            startActivity(new Intent(Principale.this, Login.class));

                        }
                    })


                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .show();
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
