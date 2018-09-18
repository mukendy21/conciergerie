package ci.deminacconciergerie.com.conciergerie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ci.deminacconciergerie.com.conciergerie.manager.HttpParse;
import io.realm.Realm;
import io.realm.RealmObject;

public class Inscription extends AppCompatActivity {

    private Realm realm;

    private TextView already_user , text_inscription;
    private ImageView close_activity_inscription;

    private EditText et_nom, et_prenom, et_email, et_tel, et_password ;
    String nom , prenoms , email , tel , password ;
    String token = "PtUxgwmz4j";

    private Button btn_sign_in;



    String finalResult ;
    String HttpURL = "http://deminacconciergerie.com/api/client/create.php";
    //String HttpURL = "http://10.100.61.14/conciergerie/UserRegistrations.php";
    Boolean CheckEditText ;

    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);




        close_activity_inscription = (ImageView)findViewById(R.id.close_activity_inscription);

        //client infos

        et_nom = (EditText)findViewById(R.id.et_nom_ins);
        et_prenom = (EditText)findViewById(R.id.et_prenom_ins);
        et_email = (EditText)findViewById(R.id.et_email_ins);
        et_tel = (EditText)findViewById(R.id.et_tel_ins);
        et_password= (EditText)findViewById(R.id.et_password_ins);


        //button

        btn_sign_in = (Button)findViewById(R.id.btn_inscription);


        already_user = (TextView)findViewById(R.id.already_user);

        // FONT TEXTVIEW
        text_inscription = (TextView)findViewById(R.id.text_inscription);
        Typeface myTypeface = Typeface.createFromAsset(getAssets() , "coneria.ttf");
        text_inscription.setTypeface(myTypeface);

        already_user.setPaintFlags(already_user.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        already_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Inscription.this , Login.class));
            }
        });


        close_activity_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(!isValidEmail(email)){


                    Snackbar snackbar = Snackbar
                            .make(btn_sign_in, R.string.email_long_valid,  Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();

                } else if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.


                    ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                    if(networkInfo != null && networkInfo.isConnected()){
                        int length_tel = tel.length();

                        if(length_tel != 8 || !isValidEmail(email)){

                            Toast.makeText(Inscription.this, R.string.number_must, Toast.LENGTH_SHORT).show();
                        }else{
                            UserRegisterFunction(token ,nom,prenoms, email, tel , password);
                        }
                       // UserRegisterFunction(nom,prenoms, email, tel , password);
                        //Toast.makeText(Inscription.this, ""+token.toString(), Toast.LENGTH_SHORT).show();




                    }else {
                        Snackbar snackbar = Snackbar
                                .make(btn_sign_in, R.string.internet,  Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }



                }
                else {

                    // If EditText is empty then this block will execute .
                    Snackbar snackbar = Snackbar
                            .make(btn_sign_in, R.string.formulaire, Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();

                }



            }


        });


    }



    public void CheckEditTextIsEmptyOrNot(){

        nom = et_nom.getText().toString().trim();
        prenoms = et_prenom.getText().toString().trim();
        email = et_email.getText().toString().trim();
        tel = et_tel.getText().toString().trim();
        password = et_password.getText().toString().trim();
        token = token.toString();

        if(TextUtils.isEmpty(nom) || TextUtils.isEmpty(prenoms)|| TextUtils.isEmpty(email) || TextUtils.isEmpty(tel) || TextUtils.isEmpty(password) )
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }


    // validating email id
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }





    public void UserRegisterFunction(final String token ,final String nom , final String prenoms , final String email , String tel , final String password){


        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Inscription.this,getString(R.string.data),null,true,true);
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


                if(message.equals("client was created.")){
                Toast.makeText(Inscription.this, R.string.inscription_register, Toast.LENGTH_LONG).show();
                startActivity(new Intent(Inscription.this, Login.class));
                finish();
                }else if (message.equals("Unable to create client.")){
                    Toast.makeText(Inscription.this, R.string.inscription_already, Toast.LENGTH_SHORT).show();
                }


                /*Snackbar snackbar = Snackbar
                        .make(btn_sign_in, message,  Snackbar.LENGTH_LONG);
                snackbar.show();*/



            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("token", params[0]);

                hashMap.put("nom",params[1]);

                hashMap.put("prenoms",params[2]);

                hashMap.put("email",params[3]);

                hashMap.put("tel",params[4]);

                hashMap.put("password",params[5]);


                /*hashMap.put("nom",params[0]);

                hashMap.put("prenoms",params[1]);

                hashMap.put("email",params[2]);

                hashMap.put("tel",params[3]);

                hashMap.put("password",params[4]);*/

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(token ,nom ,prenoms, email , tel ,password);
        //userRegisterFunctionClass.execute(nom ,prenoms, email , tel ,password);
    }



}
