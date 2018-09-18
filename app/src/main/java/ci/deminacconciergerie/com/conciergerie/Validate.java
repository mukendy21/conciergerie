package ci.deminacconciergerie.com.conciergerie;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ci.deminacconciergerie.com.conciergerie.DataAdapter.User;
import ci.deminacconciergerie.com.conciergerie.manager.HttpParse;
import io.realm.Realm;

public class Validate extends AppCompatActivity implements LocationListener {



    // "etat" , "description", "montant", "budget", "localite", "date_prestation", "id_client", "id_service"



    Realm realm;

    User user;


    String id = new String();

    
    private TextView et_date , et_heure , txt_libelle_service , txt_verify;
    private ImageView close_activity_validate;


    String etat = "0";
    String id_service = null;
    String  date_prestation;

    String description , budget , localite, id_client , date_s , heure_s;

    String getId_service_activity=null;
    String getlibelle_previous_activity=null;

    private EditText et_description , et_budget, et_localite;
    private Button btn_date, btn_heure, btn_validate_commande, btn_get_city;


    private int mYear, mMonth, mDay, mHour, mMinute;



    String token = "PtUxgwmz4j";
    String finalResult ;
    String HttpURL = "http://deminacconciergerie.com/api/commande/create.php";
    Boolean CheckEditText ;

    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();



    //allow
    LocationManager locationManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);


        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();


        txt_verify = (TextView)findViewById(R.id.txt_verify);

        close_activity_validate = (ImageView)findViewById(R.id.close_activity_validate) ;

        et_date = (TextView) findViewById(R.id.et_date);
        et_heure = (TextView) findViewById(R.id.et_heure);
        txt_libelle_service = (TextView)findViewById(R.id.text_title_services);


        //police
        Typeface myTypeface = Typeface.createFromAsset(getAssets() , "coneria.ttf");
        txt_libelle_service.setTypeface(myTypeface);



        et_description = (EditText)findViewById(R.id.et_description);
        et_localite = (EditText)findViewById(R.id.et_location);
        et_budget = (EditText)findViewById(R.id.et_budget);


        btn_date = (Button)findViewById(R.id.btn_date);
        btn_heure = (Button)findViewById(R.id.btn_heure);
        btn_validate_commande = (Button)findViewById(R.id.btn_validate_commande);
        btn_get_city = (Button)findViewById(R.id.btn_get_city);



        et_localite.addTextChangedListener(new EmptyTextWatcher()
        {

            @Override
            public void onEmptyField()
            {
                btn_get_city.setEnabled(true);
            }

            @Override
            public void onFilledField()
            {
                btn_get_city.setEnabled(false);
            }
        });



        //alllow permission

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        btn_get_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();





                if(networkInfo != null && networkInfo.isConnected()){
                    getLocation();
                    //Toast.makeText(Validate.this, "ok", Toast.LENGTH_SHORT).show();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(btn_validate_commande, R.string.internet,  Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();
                }


            }
        });



        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            getId_service_activity = (String) bd.get("id_service");
            getlibelle_previous_activity = (String) bd.get("libelle_service");
        }

        txt_libelle_service.setText(getlibelle_previous_activity.toLowerCase());
        id_service = getId_service_activity;



       // DateFormat dateFormat = new SimpleDateFormat("yyyy/M/dd HH:mm:ss");
       // DateFormat dateFormat = new SimpleDateFormat("yyyy/M/dd");
      /*  DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
        Date date = new Date();

        System.out.println(dateFormat.format(date));

        try {
            Date date1 = dateFormat.parse("19/9/2018");
            if (date.after(date1)){
                et_description.setText("ok");
            }else{
                et_description.setText("nok");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

       // Toast.makeText(this, ""+dateFormat.format(date), Toast.LENGTH_SHORT).show();


        close_activity_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Validate.this, Services.class));
                finish();
            }
        });

        


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current final String   et_date_prestation Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Validate.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                et_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                et_date.setText(year+ "/"+ (monthOfYear + 1)+ "/"+dayOfMonth);

                                DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
                                try {
                                    Date date1 = dateFormat.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    Date date = new Date();
                                    String date_format_string = dateFormat.format(date);
                                    Date date2 = dateFormat.parse(date_format_string);
                                    if(date1.after(date2)){
                                        et_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                    if (date1.before(date2)){
                                        et_date.setText("Date");
                                        //Toast.makeText(Validate.this, ""+date_format_string, Toast.LENGTH_SHORT).show();
                                        Snackbar snackbar = Snackbar
                                                .make(btn_validate_commande, R.string.incorrect_date,  Snackbar.LENGTH_LONG);

                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(Color.RED);
                                        snackbar.show();


                                    } if (date1.equals(date2)){
                                        et_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });






        btn_heure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);


                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Validate.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                et_heure.setText(hourOfDay + ":" + minute);

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });





        btn_validate_commande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date_prestation = et_date.getText().toString()+" "+et_heure.getText().toString();

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.


                    id = user.getId().toString();




                    ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()){
                        id_client = id;
                        CommandeRegisterFunction(token , etat ,description ,budget, localite , date_prestation ,id_client , id_service);


                        //txt_verify.setText("Token: "+token+" etat: "+etat+" description: "+description+" budget: "+budget+" localite: "+localite+" date_prestation: "+date_prestation+ " id_client: "+id_client+" id_service: "+id_service);

                    }else {
                        Snackbar snackbar = Snackbar
                                .make(btn_validate_commande, R.string.internet,  Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }



                }
                else {

                    // If EditText is empty then this block will execute .
                    Snackbar snackbar = Snackbar
                            .make(btn_validate_commande, R.string.formulaire, Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();

                }

            }
        });
    }



    public abstract class EmptyTextWatcher implements TextWatcher
    {
        public abstract void onEmptyField();

        public abstract void onFilledField();

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (s.toString().trim().length() == 0)
            {
                onEmptyField();
            } else
            {
                onFilledField();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }



        @Override
        public void afterTextChanged(Editable s)
        {

        }

    }




    public void CheckEditTextIsEmptyOrNot(){

        localite = et_localite.getText().toString();
        budget = et_budget.getText().toString();
        description = et_description.getText().toString();
        token = token.toString();
        date_s = et_date.getText().toString();
        heure_s = et_heure.getText().toString();


        if(TextUtils.isEmpty(localite) || TextUtils.isEmpty(date_prestation)|| TextUtils.isEmpty(description) || TextUtils.isEmpty(budget) || date_s.equals("Date") || heure_s.equals("Heure"))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }

    //allow
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
       // et_localite.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            et_localite.setText(et_localite.getText() + "\n"+addresses.get(0).getAddressLine(0));
                    // addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(Validate.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(btn_validate_commande, R.string.active_gps,  Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }










    // "etat" , "description", "montant", "budget", "localite", "date_prestation", "id_client", "id_service"

    public void CommandeRegisterFunction(final String token , final String etat ,  final String description , final String budget , final String localite, final String date_prestation , final String id_client , final String id_service){



        class CommandeRegisterFunctionClass extends AsyncTask<String,Void,String> {




            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Validate.this,getString(R.string.data),null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();



                String message = null;


                try {
                    JSONObject reader = new JSONObject(httpResponseMsg);
                    message = reader.getString("message");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(message.equals("commande was created.")){
                    Toast.makeText(Validate.this, R.string.commande_registred, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Validate.this, Principale.class));
                    finish();
                }else{
                    Toast.makeText(Validate.this, R.string.impossible_commande, Toast.LENGTH_SHORT).show();
                }



               // Toast.makeText(Validate.this,message.toString(), Toast.LENGTH_LONG).show();

            }


            @Override
            protected String doInBackground(String... params) {


                hashMap.put("token", params[0]);

                hashMap.put("etat", params[1]);

                hashMap.put("description",params[2]);

                hashMap.put("budget",params[3]);

                hashMap.put("localite",params[4]);

                hashMap.put("date_prestation",params[5]);

                hashMap.put("id_client",params[6]);

                hashMap.put("id_service",params[7]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        CommandeRegisterFunctionClass commandeRegisterFunctionClass = new CommandeRegisterFunctionClass();

        commandeRegisterFunctionClass.execute(token , etat ,description ,budget, localite , date_prestation ,id_client , id_service);

    }
}


